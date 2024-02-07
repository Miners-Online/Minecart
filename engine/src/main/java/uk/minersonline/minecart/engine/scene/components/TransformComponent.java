package uk.minersonline.minecart.engine.scene.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TransformComponent {
    private final Matrix4f modelMatrix = new Matrix4f();
    private final Vector3f position;
    private final Quaternionf rotation;
    private float scale;

    public TransformComponent(Vector3f position, Quaternionf rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public TransformComponent(Vector3f position, Quaternionf rotation) {
        this.position = position;
        this.rotation = rotation;
        this.scale = 1;
    }

    public TransformComponent(Vector3f position) {
        this.position = position;
        this.rotation = new Quaternionf();
        this.scale = 1;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public final void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z, float angle) {
        this.rotation.fromAxisAngleRad(x, y, z, angle);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void updateModelMatrix() {
        modelMatrix.translationRotateScale(position, rotation, scale);
    }
}
