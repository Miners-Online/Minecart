package uk.minersonline.minecart.engine.gui;

import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.engine.IntEntity;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import uk.minersonline.minecart.engine.render.objects.Texture;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.components.AbstractComponent;

import java.util.Map;


public class DebugGui implements GuiInstance {
    private Entity selected;
    private final ImBoolean demoEnabled;
    private final ImBoolean objectsEnabled;
    private final ImBoolean propertiesEnabled;
    private final ImBoolean texturesEnabled;

    public DebugGui() {
        this.demoEnabled = new ImBoolean(false);
        this.objectsEnabled = new ImBoolean(false);
        this.propertiesEnabled = new ImBoolean(false);
        this.texturesEnabled = new ImBoolean(false);
    }

    @Override
    public void drawGui(Scene scene) {
        ImGui.beginMainMenuBar();
        if (ImGui.beginMenu("Show")) {
            ImGui.menuItem("Demo Window", "", this.demoEnabled);
            ImGui.menuItem("Objects Window", "", this.objectsEnabled);
            ImGui.menuItem("Properties Window", "", this.propertiesEnabled);
            ImGui.menuItem("Textures Window", "", this.texturesEnabled);
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();

        if (this.demoEnabled.get()) {
            ImGui.showDemoWindow(this.demoEnabled);
        }

        if (this.objectsEnabled.get()) {
            ImGui.setNextWindowSize(250, 440, ImGuiCond.FirstUseEver);
            if (ImGui.begin("Objects", objectsEnabled)) {
                Results<Entity> results = scene.getDominion().findAllEntities();
                for (Entity entity : results) {
                    String name = entity.get(String.class);
                    if (ImGui.selectable(name, entity == this.selected)) {
                        if (this.selected == entity) {
                            this.selected = null;
                        } else {
                            this.selected = entity;
                            this.propertiesEnabled.set(true);
                        }
                    }
                }
            }
            ImGui.end();
        }

        if (this.propertiesEnabled.get()) {
            ImGui.setNextWindowSize(250, 440, ImGuiCond.FirstUseEver);
            if (ImGui.begin("Properties", propertiesEnabled)) {
                if (this.selected != null) {
                    IntEntity entity = (IntEntity) this.selected;
                    for (Object object : entity.getComponentArray()) {
                        if (object instanceof AbstractComponent component) {
                            component.drawGui();
                        }
                    }
                }
            }
            ImGui.end();
        }

        if (this.texturesEnabled.get()) {
            ImGui.setNextWindowSize(440, 250, ImGuiCond.FirstUseEver);
            if (ImGui.begin("Textures", texturesEnabled)) {
                Map<String, Texture> textureMap = scene.getCache().getTextureMap();
                for (Texture texture : textureMap.values()) {
                    ImGui.image(texture.getTextureId(), 64, 64);
                    ImGui.sameLine();
                    ImGui.text(texture.getTexturePath());
                }
            }
            ImGui.end();
        }
    }
}
