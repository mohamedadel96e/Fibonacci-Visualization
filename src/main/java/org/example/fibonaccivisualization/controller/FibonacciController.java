package org.example.fibonaccivisualization.controller;

import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.example.fibonaccivisualization.model.FibonacciCalculator;
import org.example.fibonaccivisualization.model.FibNode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.example.fibonaccivisualization.view.TreePane;

public class FibonacciController {
    private final FibonacciCalculator calculator;
    private final TextField inputField;
    private final TextArea resultArea;
    private final LineChart<Number, Number> chart;
    private final TreePane treePane;
    private final Label statusLabel;

    public FibonacciController(TextField inputField, TextArea resultArea,
                               LineChart<Number, Number> chart, TreePane treePane,
                               Label statusLabel) {
        this.calculator = new FibonacciCalculator();
        this.inputField = inputField;
        this.resultArea = resultArea;
        this.chart = chart;
        this.treePane = treePane;
        this.statusLabel = statusLabel;
    }

    public void handleRecursiveTree(ActionEvent event) {
        try {
            int n = parseInput();
            statusLabel.setText("Building recursive tree for n = " + n + "...");

            new Thread(() -> {
                FibNode root = calculator.buildRecursiveTree(n);
                javafx.application.Platform.runLater(() -> {
                    treePane.drawTree(root);
                    statusLabel.setText("Recursive tree built for n = " + n);
                });
            }).start();

        } catch (NumberFormatException e) {
            resultArea.setText("Error: Please enter a valid positive integer (n ≤ 35 for recursive).");
            statusLabel.setText("Invalid input");
        }
    }

    public void handleMemoized(ActionEvent event) {
        runAndDisplay(() -> calculator.memoized(parseInput()), "Memoization");
    }

    public void handleTabulated(ActionEvent event) {
        runAndDisplay(() -> calculator.tabulated(parseInput()), "Tabulation");
    }

    private int parseInput() throws NumberFormatException {
        int n = Integer.parseInt(inputField.getText());
        if (n < 0) throw new NumberFormatException();
        return n;
    }

    private void runAndDisplay(FibFunction func, String methodName) {
        try {
            int n = parseInput();
            statusLabel.setText("Calculating using " + methodName + " for n = " + n + "...");

            new Thread(() -> {
                long start = System.nanoTime();
                long result = func.run();
                long end = System.nanoTime();
                double timeMs = (end - start) / 1_000_000.0;

                javafx.application.Platform.runLater(() -> {
                    resultArea.appendText(methodName + " result for F(" + n + "): " + result +
                            "\nTime: " + String.format("%.3f", timeMs) + " ms\n\n");

                    updateChart(n);
                    statusLabel.setText(methodName + " calculation completed for n = " + n);
                });
            }).start();

        } catch (NumberFormatException e) {
            resultArea.setText("Error: Please enter a valid positive integer.");
            statusLabel.setText("Invalid input");
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
            statusLabel.setText("Calculation error");
        }
    }

    private void updateChart(int n) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Fibonacci Sequence");

        for (int i = 0; i <= n; i++) {
            series.getData().add(new XYChart.Data<>(i, calculator.tabulated(i)));
        }

        chart.getData().clear();
        chart.getData().add(series);
    }

    @FunctionalInterface
    interface FibFunction {
        long run();
    }
}