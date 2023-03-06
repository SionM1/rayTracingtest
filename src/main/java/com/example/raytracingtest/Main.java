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

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.io.*;
import java.lang.Math.*;
import java.util.List;

import javafx.geometry.HPos;

import static java.lang.Math.sqrt;

public class Main extends Application {
    double lighty = 10;
    ArrayList<Sphere> spheres;
    Sphere selectedSphere;

    Camera camera = new Camera(new Vector(0, 0, -200), 0, 0);
    double azimuth = camera.getAzimuth();
    double altitude = camera.getAltitude();

    //slider objects handler
    public static void setSliderTickLabels(Slider slider) {
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(0);

    }
    public void start(Stage stage) throws FileNotFoundException {
        stage.setTitle("Sphere!");
        int Width = 500;
        int Height = 500;

        // Create an instance of Sphere
        spheres = new ArrayList<>();
        spheres.add(new Sphere(new Vector(0, -55, -100), 25, Color.RED));
        spheres.add(new Sphere(new Vector(-55, 0, -100), 25, Color.BLUE));
        spheres.add(new Sphere(new Vector(55, 0, -100), 25, Color.GREEN));
        spheres.add(new Sphere(new Vector(0, 55, -100), 25, Color.ORANGE));

        //select the first sphere
        selectedSphere = spheres.get(1);

        // Create an image and an ImageView to display it
        WritableImage image = new WritableImage(Width, Height);
        ImageView view = new ImageView(image);


        //sliders for x, y, z , radius and alt az
        Slider x_slider = new Slider(-200, 200, 0);
        Slider y_slider = new Slider(-200, 200, 0);
        Slider z_slider = new Slider(-200, 0, -100);
        Slider azimuth_slider = new Slider(-180, 180, azimuth);
        Slider altitude_slider = new Slider(-90, 90, altitude);
        Slider radius_slider = new Slider(-200, 100, 0);



        //setting the tick labels on the sliders
        setSliderTickLabels(x_slider);
        setSliderTickLabels(y_slider);
        setSliderTickLabels(z_slider);
        setSliderTickLabels(radius_slider);
        setSliderTickLabels(altitude_slider);
        setSliderTickLabels(azimuth_slider);
        //rgb sliders
        Slider r_slider = new Slider(0, 1, selectedSphere.color.getRed());
        Slider g_slider = new Slider(0, 1, selectedSphere.color.getGreen());
        Slider b_slider = new Slider(0, 1, selectedSphere.color.getBlue());

        //setting the tick labels on the sliders
        setSliderTickLabels(r_slider);
        setSliderTickLabels(g_slider);
        setSliderTickLabels(b_slider);

        //add vbox into render
        VBox vbox = new VBox();
        vbox.setSpacing(3);
        //Toggle group for Radio button
        ToggleGroup group = new ToggleGroup();
        //create radio button
        for (int i = 0; i < spheres.size(); i++) {
            RadioButton rb = new RadioButton("Sphere " + (i + 1));
            rb.setToggleGroup(group);
            rb.setUserData(spheres.get(i));
            vbox.getChildren().add(rb);// add RadioButton to the VBox

        }
        // Add Label and Slider controls outside of the loop
        vbox.getChildren().addAll(
                new Label("Azimuth:"),
                azimuth_slider,
                new Label("Altitude:"),
                altitude_slider
        );

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                // Update the selected sphere
                selectedSphere = (Sphere) newValue.getUserData();
                // Update the XYZ and RGB sliders to show the selected sphere's properties
                x_slider.setValue(selectedSphere.center.x);
                y_slider.setValue(selectedSphere.center.y);
                z_slider.setValue(selectedSphere.center.z);
                r_slider.setValue(selectedSphere.color.getRed());
                g_slider.setValue(selectedSphere.color.getGreen());
                b_slider.setValue(selectedSphere.color.getBlue());
                radius_slider.setValue(selectedSphere.radius);
            }
        });

        view.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            //print out the X Y Z coordinates
            System.out.println(event.getX() + " " + event.getY());
            event.consume();
            Render(image, camera);
        });
        // Add ChangeListeners for each slider
        x_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
               if (selectedSphere != null)
                   selectedSphere.center.x = newValue.doubleValue();
                // Render the image again
                Render(image, camera);
                // Update the ImageView
                view.setImage(image);
            }

        });
        y_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
                if (selectedSphere != null) {
                    selectedSphere.center.y = newValue.doubleValue();
                    // Render the image again
                    Render(image, camera);
                    // Update the ImageView
                    view.setImage(image);

                }
            }

        });
        z_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
                if (selectedSphere != null) {
                    selectedSphere.center.z = newValue.doubleValue();
                    // Render the image again
                    Render(image, camera);
                    // Update the ImageView
                    view.setImage(image);
                }
            }
        });

        radius_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the position of the sphere
                if (selectedSphere != null) {
                    selectedSphere.radius = newValue.doubleValue();
                    // Render the image again
                    Render(image, camera);
                    // Update the ImageView
                    view.setImage(image);
                }
            }
        });

        r_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the color of the sphere
                if (selectedSphere != null) {
                    selectedSphere.color = Color.color(newValue.doubleValue(), selectedSphere.color.getGreen(), selectedSphere.color.getBlue());
                    // Render the image again
                    Render(image, camera);
                    // Update the ImageView
                    view.setImage(image);
                }
            }
        });

        g_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the color of the sphere
                if (selectedSphere != null) {
                    selectedSphere.color = Color.color(selectedSphere.color.getRed(), newValue.doubleValue(), selectedSphere.color.getBlue());
                    // Render the image again
                    Render(image, camera);
                    // Update the ImageView
                    view.setImage(image);
                }
            }
        });
        b_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Update the color of the sphere
                if (selectedSphere != null) {
                    selectedSphere.color = Color.color(selectedSphere.color.getRed(), selectedSphere.color.getGreen(), newValue.doubleValue());
                    // Render the image again
                    Render(image, camera);
                    // Update the ImageView
                    view.setImage(image);
                }
            }
        });

        // Add ChangeListeners for azimuth and altitude sliders
        azimuth_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                azimuth = newValue.doubleValue();
                camera.setAzimuth(azimuth);
                Render(image, camera);
                view.setImage(image);
            }
        });
        altitude_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                altitude = newValue.doubleValue();
                camera.setAltitude(altitude);
                Render(image, camera);
                view.setImage(image);
            }
        });


        //make a hbox to hold the RGB sliders
        HBox rgbSliderBox = new HBox();
        rgbSliderBox.setSpacing(10);
        rgbSliderBox.getChildren().addAll(
                new Label("R"), r_slider,
                new Label("G"), g_slider,
                new Label("B"), b_slider,
                new Label("Radius"), radius_slider
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
        root.add(vbox, 3, 0, 1, 3);


        // Create a Scene and show it
        Scene scene = new Scene(root, 840, 640);
        stage.setScene(scene);
        stage.show();

        // Render the image initially
        Render(image, camera);
    }

    public class Ray {
        public Vector origin;
        public Vector direction;

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
    public void Render(WritableImage image, Camera camera) {
        // Get image dimensions, and declare loop variables
        int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
        PixelWriter image_writer = image.getPixelWriter();

        Vector light = new Vector(250, 250, -100 * lighty);


        double cosAzimuth = Math.cos(camera.getAzimuth());
        double sinAzimuth = Math.sin(camera.getAzimuth());
        double cosAltitude = Math.cos(camera.getAltitude());
        double sinAltitude = Math.sin(camera.getAltitude());

        for (j = 0; j < h; j++) {
            for (i = 0; i < w; i++) {
                Vector direction = new Vector(
                        (i - w / 2) * cosAzimuth - (j - h / 2) * sinAzimuth,
                        (i - w / 2) * sinAzimuth + (j - h / 2) * cosAzimuth,
                        (j - h / 2) * cosAltitude + (i - w / 2) * sinAltitude
                ).sub(camera.getPosition());
                direction.normalise();

                Main.Ray ray = new Main.Ray(camera.getPosition(), direction);

                Sphere.Intersection closest = null;
                double closestDistance = Double.POSITIVE_INFINITY;

                for (Sphere sphere : spheres) {
                    Sphere.Intersection current = sphere.intersect(ray);
                    if (current != null) {
                        double distance = current.getPoint().sub(camera.getPosition()).magnitude();
                        if (distance < closestDistance) {
                            closest = current;
                            closestDistance = distance;
                        }
                    }
                }

                if (closest != null) {
                    Vector point = closest.getPoint();
                    Vector normal = closest.getNormal();
                    Vector lightDir = light.sub(point);
                    lightDir.normalise();
                    double lightDist = light.sub(point).magnitude();
                    Main.Ray shadowRay = new Main.Ray(point, lightDir);
                    boolean inShadow = false;

                    for (Sphere sphere : spheres) {
                        Sphere.Intersection shadowIntersect = sphere.intersect(shadowRay);
                        if (shadowIntersect != null) {
                            double shadowDistance = shadowIntersect.getPoint().sub(point).magnitude();
                            if (shadowDistance < lightDist) {
                                inShadow = true;
                                break;
                            }
                        }
                    }

                    double brightness = Math.max(0, normal.dot(lightDir)) * (inShadow ? 0.5 : 1);
                    Color col = closest.getColor().deriveColor(0, 1, brightness, 1);
                    image_writer.setColor(i, j, col);
                } else {
                    image_writer.setColor(i, j, Color.GRAY);
                }
            }
     }
    }}

