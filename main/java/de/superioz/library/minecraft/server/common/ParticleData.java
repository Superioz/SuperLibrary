package de.superioz.library.minecraft.server.common;

/**
 * Created on 02.02.2016.
 */
public class ParticleData {

    private float x;
    private float y;
    private float z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float data;
    private int amount;

    public ParticleData(float x, float y, float z, float offsetY, float offsetX, float offsetZ, float data, int amount) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetY = offsetY;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
        this.data = data;
        this.amount = amount;
    }

    // -- Intern methods

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public int getAmount() {
        return amount;
    }

    public float getData() {
        return data;
    }
}
