package com.nati.zinab.models;

public class DataPoint {

    private long time;
    private String summary;
    private String icon;
    private long sunriseTime;
    private long sunsetTime;
    private String moonPhase;
    private String nearestStormDistance;
    private String nearestStormBearing;
    private double precipIntensity;
    private String precipIntensityMax;
    private String precipIntensityMaxTime;
    private double precipProbability;
    private String precipType;
    private String precipAccumulation;
    private double temperature;
    private double temperatureMin;
    private double temperatureMinTime;
    private double temperatureMax;
    private double temperatureMaxTime;
    private double apparentTemperature;
    private double apparentTemperatureMin;
    private double apparentTemperatureMinTime;
    private double apparentTemperatureMax;
    private double apparentTemperatureMaxTime;
    private double dewPoint;
    private double windSpeed;
    private int windBearing;
    private double cloudClover;
    private double humidity;
    private double pressure;
    private String visibility;
    private double ozone;

    public DataPoint() {
    }

    public long getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public long getSunriseTime() {
        return sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public String getMoonPhase() {
        return moonPhase;
    }

    public String getNearestStormDistance() {
        return nearestStormDistance;
    }

    public String getNearestStormBearing() {
        return nearestStormBearing;
    }

    public double getPrecipIntensity() {
        return precipIntensity;
    }

    public String getPrecipIntensityMax() {
        return precipIntensityMax;
    }

    public String getPrecipIntensityMaxTime() {
        return precipIntensityMaxTime;
    }

    public double getPrecipProbability() {
        return precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public String getPrecipAccumulation() {
        return precipAccumulation;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public double getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public double getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public double getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public double getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public double getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public double getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindBearing() {
        return windBearing;
    }

    public double getCloudClover() {
        return cloudClover;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public double getOzone() {
        return ozone;
    }
}
