package com.example.raytracingtest;
import javafx.scene.paint.Color;

public class Sphere {
    public Vector center;
    public double radius;
    public Color color;

    public Sphere(Vector center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }


    public Intersection intersect(Main.Ray ray) {
        Vector oc = ray.getOrigin().sub(center);
        double a = ray.getDirection().dot(ray.getDirection());
        double b = 2.0 * oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            return null;
        } else {
            double t1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            double t2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);
            double t = Math.min(t1, t2);
            if (t < 0) {
                return null;
            } else {
                Vector hitPoint = ray.getOrigin().add(ray.getDirection().mul(t));
                Vector normal = hitPoint.sub(center);
                normal.normalise();
                return new Intersection(hitPoint, normal, color);
            }
        }
    }


    public static class Intersection {
        public Vector point;
        public Vector normal;
        public Color color;

        public Intersection(Vector point, Vector normal, Color color) {
            this.point = point;
            this.normal = normal;
            this.color = color;
        }

        public Vector getPoint() {
            return point;
        }

        public Vector getNormal() {
            return normal;
        }

        public Color getColor() {
            return color;
        }
    }
}