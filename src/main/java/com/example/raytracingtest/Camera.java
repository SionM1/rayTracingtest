package com.example.raytracingtest;
public class Camera {

    private Vector position;
    private double azimuth;
    private double altitude;

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

    public void moveForward(double distance) {
        Vector direction = getDirection();
        position = position.add(direction.mul(distance));
    }

    public void moveBackward(double distance) {
        Vector direction = getDirection();
        position = position.sub(direction.mul(distance));
    }

    public void moveRight(double distance) {
        Vector direction = getDirection().cross(new Vector(0, 1, 0)).normalise();
        position = position.add(direction.mul(distance));
    }

    public void moveLeft(double distance) {
        Vector direction = getDirection().cross(new Vector(0, 1, 0)).normalise();
        position = position.sub(direction.mul(distance));
    }

    public void moveUp(double distance) {
        position = position.add(new Vector(0, distance, 0));
    }

    public void moveDown(double distance) {
        position = position.sub(new Vector(0, distance, 0));
    }

    public void rotateAzimuth(double angle) {
        azimuth += angle;
    }

    public void rotateAltitude(double angle) {
        altitude += angle;
        if (altitude > 90) {
            altitude = 90;
        } else if (altitude < -90) {
            altitude = -90;
        }
    }
}
