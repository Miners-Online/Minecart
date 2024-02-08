package uk.minersonline.minecart.engine.gui;

import dev.dominion.ecs.api.Results;
import dev.dominion.ecs.api.Entity;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import uk.minersonline.minecart.engine.scene.Scene;


public class DebugGui implements GuiInstance {
    private Entity selected;
    @Override
    public void drawGui(Scene scene) {
        ImGui.setNextWindowSize(500, 440, ImGuiCond.FirstUseEver);
        if (ImGui.begin("Objects")) {
            ImGui.beginChild("left pane", 150, 0);
            Results<Entity> results = scene.getDominion().findAllEntities();
            for (Entity entity : results) {
                String name = entity.get(String.class);
                if (ImGui.selectable(name, entity == selected)) {
                    selected = entity;
                }
            }
            ImGui.endChild();
        }
        ImGui.end();
    }
}
