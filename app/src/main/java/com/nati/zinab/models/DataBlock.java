package com.nati.zinab.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataBlock {

    private String summary;
    private String icon;
    @SerializedName("data")
    private List<DataPoint> data;

    public DataBlock() {
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public List<DataPoint> getData() {
        return data;
    }
}
