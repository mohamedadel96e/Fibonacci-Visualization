
package org.example.project.controller;

import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.example.project.model.FibonacciCalculator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FibonacciController {
    private final FibonacciCalculator calculator;
    private final TextField inputField;
    private final TextArea resultArea;
    private final LineChart<Number, Number> chart;

    public FibonacciController(TextField inputField, TextArea resultArea, LineChart<Number, Number> chart) {
        this.calculator = new FibonacciCalculator();
        this.inputField = inputField;
        this.resultArea = resultArea;
        this.chart = chart;
    }

    public void handleRecursive(ActionEvent event) {
        runAndDisplay(() -> calculator.recursiveWithTrace(parseInput()));
    }

    public void handleMemoized(ActionEvent event) {
        runAndDisplay(() -> calculator.memoized(parseInput()));
    }

    public void handleTabulated(ActionEvent event) {
        runAndDisplay(() -> calculator.tabulated(parseInput()));
    }

    private int parseInput() {
        try {
            return Integer.parseInt(inputField.getText());
        } catch (NumberFormatException e) {
            resultArea.setText("Invalid input. Please enter a valid integer.");
            throw e;
        }
    }

    private void runAndDisplay(FibFunction func) {
        try {
            int n = parseInput();
            long start = System.nanoTime();
            long result = func.run();
            long end = System.nanoTime();
            resultArea.appendText("Result: " + result + "\nTime: " + (end - start) + " ns\n");

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i <= n; i++) {
                series.getData().add(new XYChart.Data<>(i, calculator.tabulated(i)));
            }
            chart.getData().clear();
            chart.getData().add(series);
        } catch (Exception ignored) {}
    }

    @FunctionalInterface
    interface FibFunction {
        long run();
    }
}
