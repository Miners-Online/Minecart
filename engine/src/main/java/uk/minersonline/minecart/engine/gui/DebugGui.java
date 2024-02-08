package uk.minersonline.minecart.engine.gui;

import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Entity;
import dev.dominion.ecs.engine.IntEntity;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.components.AbstractComponent;


public class DebugGui implements GuiInstance {
    private Entity selected;
    @Override
    public void drawGui(Scene scene) {
        ImGui.setNextWindowSize(250, 440, ImGuiCond.FirstUseEver);
        if (ImGui.begin("Objects")) {
            Results<Entity> results = scene.getDominion().findAllEntities();
            for (Entity entity : results) {
                String name = entity.get(String.class);
                if (ImGui.selectable(name, entity == selected)) {
                    if (selected == entity) {
                        selected = null;
                    } else {
                        selected = entity;
                    }
                }
            }
        }
        ImGui.end();

        ImGui.setNextWindowSize(250, 440, ImGuiCond.FirstUseEver);
        if (ImGui.begin("Properties")) {
            if (selected != null) {
                IntEntity entity = (IntEntity) selected;
                for (Object object : entity.getComponentArray()) {
                    if (object instanceof AbstractComponent component) {
                        component.drawGui();
                    }
                }
            }
        }
        ImGui.end();
    }
}
