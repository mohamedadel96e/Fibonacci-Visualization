package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getView(), 800, 650);
        scene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
        //primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("Gon.jpeg")));

        primaryStage.setTitle("Fibonacci Calculator & Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
