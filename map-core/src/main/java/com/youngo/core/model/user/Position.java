package com.youngo.core.model.user;

/**
 * Created by FC on 2016/3/9.
 * 用户的位置信息，经度，纬度
 */
public class Position
{
    private double latitude;//经度
    private double longitude;//维度

    /**
     * @return 获取经度信息
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude 经度
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return 纬度
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude 纬度
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
