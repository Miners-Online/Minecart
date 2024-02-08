package uk.minersonline.minecart.engine.scene.components;

import imgui.ImGui;
import imgui.type.ImString;

public class ModelComponent extends AbstractComponent{
    private final String modelId;

    public ModelComponent(String modelId) {
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }

    @Override
    public void drawGui() {
        ImGui.text("Model Component");
        ImGui.separator();
        ImGui.beginDisabled();
        ImGui.inputText("Model", new ImString(modelId));
        ImGui.endDisabled();
    }
}
