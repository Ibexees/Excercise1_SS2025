module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.rmi;
    requires com.google.gson;
    requires ormlite.jdbc;
    requires java.sql;
    requires com.h2database;
    opens at.ac.fhcampuswien.fhmdb.logic to com.google.gson;
    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.dataLayer.database;
    exports at.ac.fhcampuswien.fhmdb.dataLayer.api;
    exports at.ac.fhcampuswien.fhmdb.ui;
    exports at.ac.fhcampuswien.fhmdb.logic;
    //opens at.ac.fhcampuswien.fhmdb.dataLayer to ormlite.jdbc;
    opens at.ac.fhcampuswien.fhmdb.dataLayer.database to ormlite.jdbc;
    exports at.ac.fhcampuswien.fhmdb.logic.models;
    opens at.ac.fhcampuswien.fhmdb.logic.models to com.google.gson;

}