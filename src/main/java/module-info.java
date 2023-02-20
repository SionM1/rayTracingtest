module com.example.raytracingtest {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.raytracingtest to javafx.fxml;
    exports com.example.raytracingtest;
}