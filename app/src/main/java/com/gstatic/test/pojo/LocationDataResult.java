package com.gstatic.test.pojo;



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationDataResult {

    @SerializedName("status")
    public String status;
    @SerializedName("code")
    public Integer code;
    @SerializedName("data")
    public List<LocationData> locationData;

}
