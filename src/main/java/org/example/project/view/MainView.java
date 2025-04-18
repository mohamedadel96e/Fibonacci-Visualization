
package org.example.project.view;

import org.example.project.controller.FibonacciController;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {
    private final VBox root;
    private final TextField inputField;
    private final TextArea resultArea;
    private final LineChart<Number, Number> chart;

    public MainView() {
        inputField = new TextField();
        inputField.setPromptText("Enter value for n");
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);

        Button recursiveBtn = new Button("Recursive");
        Button memoBtn = new Button("Memoization");
        Button tabBtn = new Button("Tabulation");

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("n");
        yAxis.setLabel("F(n)");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Fibonacci Growth");
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        chart.setMinHeight(200);

        FibonacciController controller = new FibonacciController(inputField, resultArea, chart);

        recursiveBtn.setOnAction(controller::handleRecursive);
        memoBtn.setOnAction(controller::handleMemoized);
        tabBtn.setOnAction(controller::handleTabulated);

        HBox buttons = new HBox(15, recursiveBtn, memoBtn, tabBtn);
        buttons.setPadding(new Insets(10));
        buttons.setStyle("-fx-alignment: center");

        root = new VBox(15,
            new Label("Fibonacci Calculator (DP Methods)"),
            inputField,
            buttons,
            resultArea,
            chart
        );
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4f8");
    }

    public VBox getView() {
        return root;
    }
}
