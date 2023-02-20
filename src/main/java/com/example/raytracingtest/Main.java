package com.example.raytracingtest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.*;
import java.lang.Math.*;
import javafx.geometry.HPos;

import static java.lang.Math.sqrt;

public class Main extends Application {
    double lighty = 7.0;
    Sphere sphere;

    public void start(Stage stage) throws FileNotFoundException {
        stage.setTitle("Sphere!");
        int Width = 500;
        int Height = 500;

        // Create an instance of Sphere
        sphere = new Sphere(new Vector(0, 0, -100), 50, Color.RED);

        // Create an image and an ImageView to display it
        WritableImage image = new WritableImage(Width, Height);
        ImageView view = new ImageView(image);

        //sliders for x, y, and z positions
        Slider x_slider = new Slider(-200, 200, 0);
        Slider y_slider = new Slider(-200, 200, 0);
        Slider z_slider = new Slider(-200, 0, -100);
        //rgb sliders
        Slider r_slider = new Slider(0, 1, sphere.color.getRed());
        Slider g_slider = new Slider(0, 1, sphere.color.getGreen());
        Slider b_slider = new Slider(0, 1, sphere.color.getBlue());

        // Add ChangeListeners for each slider
        x_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
                sphere.center.x = newValue.doubleValue();
                // Render the image again
                Render(image);
                // Update the ImageView
                view.setImage(image);
            }
        });
        y_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
                sphere.center.y = newValue.doubleValue();
                // Render the image again
                Render(image);
                // Update the ImageView
                view.setImage(image);
            }
        });
        z_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
                sphere.center.z = newValue.doubleValue();
                // Render the image again
                Render(image);
                // Update the ImageView
                view.setImage(image);
            }
        });
        r_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the color of the sphere
                sphere.color = Color.color(newValue.doubleValue(), sphere.color.getGreen(), sphere.color.getBlue());
                // Render the image again
                Render(image);
                // Update the ImageView
                view.setImage(image);
            }
        });

        g_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the color of the sphere
                sphere.color = Color.color(sphere.color.getRed(), newValue.doubleValue(), sphere.color.getBlue());
                // Render the image again
                Render(image);
                // Update the ImageView
                view.setImage(image);
            }
        });
        b_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the color of the sphere
                sphere.color = Color.color(sphere.color.getRed(), sphere.color.getGreen(), newValue.doubleValue());
                // Render the image again
                Render(image);
                // Update the ImageView
                view.setImage(image);
            }
        });
        //make a hbox to hold the RGB sliders
        HBox rgbSliderBox = new HBox();
        rgbSliderBox.setSpacing(10);
        rgbSliderBox.getChildren().addAll(
              new Label("R"), r_slider,
              new Label("G"), g_slider,
              new Label("B"), b_slider
        );

        // Create HBox to hold x, y, and z sliders
        HBox sliderBox = new HBox();
        sliderBox.setSpacing(10);
        sliderBox.getChildren().addAll(
                new Label("X"), x_slider,
                new Label("Y"), y_slider,
                new Label("Z"), z_slider
        );

        // Create a GridPane to hold the ImageView and sliders
        GridPane root = new GridPane();
        root.setVgap(16);
        root.setHgap(4);

        GridPane.setRowIndex(view, 0);
        GridPane.setRowIndex(sliderBox, 1);
        root.getChildren().addAll(view, sliderBox);
        //add gridpane for the RGB sliders
        GridPane.setRowIndex(rgbSliderBox, 2);
        GridPane.setColumnIndex(rgbSliderBox, 0);
        root.getChildren().add(rgbSliderBox);

        // Create a Scene and show it
        Scene scene = new Scene(root, 640, 640);
        stage.setScene(scene);
        stage.show();

        // Render the image initially
        Render(image);
    }

    public class Ray {
        private Vector origin;
        private Vector direction;

        public Ray(Vector origin, Vector direction) {
            this.origin = origin;
            this.direction = direction;
        }

        public Vector getOrigin() {
            return origin;
        }

        public Vector getDirection() {
            return direction;
        }
    }

    public void Render(WritableImage image) {
        // Get image dimensions, and declare loop variables
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
        PixelWriter image_writer = image.getPixelWriter();

        Vector camera = new Vector(0, 0, -200);
        Vector light = new Vector(250, 250, -100 * lighty);


        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                Vector direction = new Vector(i - w / 2, j - h / 2, 0).sub(camera);
                direction.normalise();
                Ray ray = new Ray(camera, direction);
                Sphere.Intersection closest = null;
                double closestDistance = Double.POSITIVE_INFINITY;
                Sphere.Intersection current;
                current = sphere.intersect(ray);
                if (current != null) {
                    double distance = current.getPoint().sub(camera).magnitude();
                    if (distance < closestDistance) {
                        closest = current;
                        closestDistance = distance;
                    }
                }
                if (closest != null) {
                    Vector point = closest.getPoint();
                    Vector normal = closest.getNormal();
                    Vector lightDir = light.sub(point);
                    lightDir.normalise();
                    double lightDist = light.sub(point).magnitude();
                    Ray shadowRay = new Ray(point, lightDir);
                    boolean inShadow = false;
                    Sphere.Intersection shadowIntersect = sphere.intersect(shadowRay);
                    if (shadowIntersect != null) {
                        double shadowDistance = shadowIntersect.getPoint().sub(point).magnitude();
                        if (shadowDistance < lightDist) {
                            inShadow = true;
                        }
                    }
                    double brightness = Math.max(0, normal.dot(lightDir)) * (inShadow ? 0.5 : 1);
                    Color col = sphere.getColor().deriveColor(0, 1, brightness, 1);
                    image_writer.setColor(i, j, col);
                } else {
                    image_writer.setColor(i, j, Color.BLACK);
                }
            }
        }
    }

}