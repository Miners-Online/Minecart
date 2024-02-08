package uk.minersonline.minecart.engine.scene.components;

import imgui.ImGui;
import imgui.type.ImFloat;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TransformComponent extends AbstractComponent {
    private final Matrix4f modelMatrix = new Matrix4f();
    private final Vector3f position;
    private final Quaternionf rotation;
    private float scale;
    private float[] _position;
    private float[] _rotation;
    private final ImFloat _scale;

    public TransformComponent(Vector3f position, Quaternionf rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;

        this._position = new float[]{position.x, position.y, position.z};
        this._rotation = new float[]{rotation.x, rotation.y, rotation.z};
        this._scale = new ImFloat(scale);
    }

    public TransformComponent(Vector3f position, Quaternionf rotation) {
        this(position, rotation, 1);
    }

    public TransformComponent(Vector3f position) {
        this(position, new Quaternionf(), 1);
    }

    public Matrix4f getModelMatrix() {
        return this.modelMatrix;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Quaternionf getRotation() {
        return this.rotation;
    }

    public float getScale() {
        return this.scale;
    }

    public final void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void updateModelMatrix() {
        this.modelMatrix.translationRotateScale(this.position, this.rotation, this.scale);
    }

    @Override
    public void drawGui() {
        if (this._position[0] != this.position.x || this._position[1] != this.position.y || this._position[2] != this.position.z) {
            this._position = new float[]{this.position.x, this.position.y, this.position.z};
        }
        if (this._rotation[0] != this.rotation.x || this._rotation[1] != this.rotation.y || this._rotation[2] != this.rotation.z) {
            this._rotation = new float[]{this.rotation.x, this.rotation.y, this.rotation.z};
        }
        if (this._scale.get() != this.scale) {
            this._scale.set(this.scale);
        }

        ImGui.text("Transform Component");
        ImGui.separator();
        ImGui.inputFloat("Scale", _scale);
        ImGui.inputFloat3("Position", _position);
        ImGui.inputFloat3("Rotation", _rotation);

        position.x = _position[0];
        position.y = _position[1];
        position.z = _position[2];
        rotation.x = _rotation[0];
        rotation.y = _rotation[1];
        rotation.z = _rotation[2];
        scale = _scale.get();
    }
}
