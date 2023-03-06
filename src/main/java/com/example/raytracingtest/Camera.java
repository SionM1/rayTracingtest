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

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Vector getDirection() {
        double x = Math.sin(Math.toRadians(azimuth)) * Math.cos(Math.toRadians(altitude));
        double y = Math.sin(Math.toRadians(altitude));
        double z = Math.cos(Math.toRadians(azimuth)) * Math.cos(Math.toRadians(altitude));
        return new Vector(x, y, z).normalise();
    }


    public void rotateAzimuth(double angle) {
        azimuth += angle;
    }
    public void rotateAltitude(double angle) {
        altitude = angle;
        if (altitude > 90) {
            altitude = 90;
        } else if (altitude < -90) {
            altitude = -90;
        }
    }

}
