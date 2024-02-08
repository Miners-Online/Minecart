package uk.minersonline.minecart.engine.gui;

import imgui.ImGui;

public class DebugGui implements GuiInstance {
    @Override
    public void drawGui() {
        ImGui.begin("Debug");

        ImGui.end();
    }
}
