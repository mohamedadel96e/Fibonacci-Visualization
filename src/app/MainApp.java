package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getView(), 600, 550);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
//        mainView.initializeResponsiveBehavior();
        primaryStage.setTitle("Fibonacci Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
