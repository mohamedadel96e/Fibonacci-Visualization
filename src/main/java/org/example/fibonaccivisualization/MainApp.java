package org.example.fibonaccivisualization;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.fibonaccivisualization.view.MainView;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getView(), 600, 550);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("Gon.jpeg")));

//        mainView.initializeResponsiveBehavior();
        primaryStage.setTitle("Fibonacci Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
