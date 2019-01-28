package pers.like.framework.main.util;

import android.graphics.Bitmap;

/**
 * @author like
 */
public class Crop {

    private int aspectX = 1;
    private int aspectY = 1;
    private boolean crop = true;
    private boolean circleCrop = false;
    private String outputFormat = Bitmap.CompressFormat.JPEG.toString();
    private boolean scale = true;
    private boolean scaleUpIfNeeded = true;
    private int outputX = 360;
    private int outputY = 360;

    public Crop() {
    }

    public int aspectX() {
        return aspectX;
    }

    public Crop aspectX(int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    public int aspectY() {
        return aspectY;
    }

    public Crop aspectY(int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    public boolean crop() {
        return crop;
    }

    public Crop crop(boolean crop) {
        this.crop = crop;
        return this;
    }

    public boolean circleCrop() {
        return circleCrop;
    }

    public Crop circleCrop(boolean circleCrop) {
        this.circleCrop = circleCrop;
        return this;
    }

    public String outputFormat() {
        return outputFormat;
    }

    public Crop outputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public boolean scale() {
        return scale;
    }

    public Crop scale(boolean scale) {
        this.scale = scale;
        return this;
    }

    public boolean scaleUpIfNeeded() {
        return scaleUpIfNeeded;
    }

    public Crop scaleUpIfNeeded(boolean scaleUpIfNeeded) {
        this.scaleUpIfNeeded = scaleUpIfNeeded;
        return this;
    }

    public int outputX() {
        return outputX;
    }

    public Crop outputX(int outputX) {
        this.outputX = outputX;
        return this;
    }

    public int outputY() {
        return outputY;
    }

    public Crop outputY(int outputY) {
        this.outputY = outputY;
        return this;
    }
}
