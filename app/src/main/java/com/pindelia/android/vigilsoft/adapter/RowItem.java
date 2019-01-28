package com.pindelia.android.vigilsoft.adapter;

public class RowItem {

    private int imgobject;
    private String label;

    public RowItem(int imgobject, String label) {
        this.label = label;
        this.imgobject = imgobject;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getImgobject() {
        return imgobject;
    }

    public void setImgobject(int imgobject) {
        this.imgobject = imgobject;
    }
}
