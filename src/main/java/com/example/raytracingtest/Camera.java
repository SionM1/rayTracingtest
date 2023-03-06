package com.example.raytracingtest;
public class Camera {

    private Vector position;
    double azimuth;
    double altitude;

    public Camera(Vector position, double azimuth, double altitude) {
        this.position = position;
        this.azimuth = azimuth;
        this.altitude = altitude;
    }

    public Vector getPosition() {
        return position;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
