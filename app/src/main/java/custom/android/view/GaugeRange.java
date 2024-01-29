package custom.android.view;

import android.graphics.Color;

public class GaugeRange {
    private int MAX_VALUE;
    private int MIN_VALUE;
    private int color;

    public int getMaxValue() {
        return MAX_VALUE;
    }

    public void setMaxValue(int MAX_VALUE) {
        this.MAX_VALUE = MAX_VALUE;
    }

    public int getMinValue() {
        return MIN_VALUE;
    }

    public void setMinValue(int MIN_VALUE) {
        this.MIN_VALUE = MIN_VALUE;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
