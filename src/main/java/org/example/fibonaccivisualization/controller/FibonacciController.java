package org.example.fibonaccivisualization.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.example.fibonaccivisualization.model.FibNode;
import org.example.fibonaccivisualization.model.FibonacciCalculator;
import org.example.fibonaccivisualization.view.MemoizationPane;
import org.example.fibonaccivisualization.view.TabulationPane;
import org.example.fibonaccivisualization.view.TreePane;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FibonacciController {
    private final TextField inputField;
    private final TextArea resultArea;
    private final LineChart<Number, Number> chart;
    private final TreePane treePane;
    private final MemoizationPane memoPane;
    private final TabulationPane tabPane;
    private final TabPane visualizationTabPane;
    private final FibonacciCalculator calculator;

    public FibonacciController(TextField inputField, TextArea resultArea, LineChart<Number, Number> chart,
                               TreePane treePane, MemoizationPane memoPane, TabulationPane tabPane,
                               TabPane visualizationTabPane) {
        this.inputField = inputField;
        this.resultArea = resultArea;
        this.chart = chart;
        this.treePane = treePane;
        this.memoPane = memoPane;
        this.tabPane = tabPane;
        this.visualizationTabPane = visualizationTabPane;
        this.calculator = new FibonacciCalculator();
    }

    public void handleRecursiveAction(ActionEvent event) {
        int n = validateInput();
        if (n < 0) return;

        // Clear previous results
        resultArea.clear();
        resultArea.appendText("Calculating Fibonacci(" + n + ") using recursive approach...\n\n");

        // Use a CompletableFuture to handle the computation in a separate thread
        CompletableFuture.supplyAsync(() -> {
            // For visualization, we don't want to create too large trees
            if (n > 12) {
                resultArea.appendText("Note: Tree visualization is limited to n <= 12 due to exponential growth.\n");
                resultArea.appendText("Calculating result without tree visualization...\n\n");
                long start = System.currentTimeMillis();
                long result = calculator.recursive(n);
                long timeTaken = System.currentTimeMillis() - start;
                return new Object[]{result, timeTaken, null};
            } else {
                long start = System.currentTimeMillis();
                FibNode root = calculator.buildRecursiveTree(n);
                long timeTaken = System.currentTimeMillis() - start;
                return new Object[]{root.result, timeTaken, root};
            }
        }).thenAcceptAsync(result -> {
            long fibResult = (long) result[0];
            long timeTaken = (long) result[1];
            FibNode root = (FibNode) result[2];

            resultArea.appendText("Result: F(" + n + ") = " + fibResult + "\n");
            resultArea.appendText("Time taken: " + timeTaken + " ms\n");
            resultArea.appendText("Number of recursive calls: " + calculator.getRecursionCount() + "\n");
            resultArea.appendText("Time complexity: O(2^n)\n");
            resultArea.appendText("Space complexity: O(n) for the call stack\n");

            // Update chart with this single point
            updateChart("Recursive", n, fibResult);

            // Show the tree visualization if we generated it
            if (root != null) {
                Platform.runLater(() -> {
                    treePane.drawTree(root);
                    visualizationTabPane.getSelectionModel().select(0); // Select the tree tab
                });
            }
        }, Platform::runLater);
    }

    public void handleMemoAction(ActionEvent event) {
        int n = validateInput();
        if (n < 0) return;

        // Clear previous results
        resultArea.clear();
        resultArea.appendText("Calculating Fibonacci(" + n + ") using memoization...\n\n");

        // Use a CompletableFuture to handle the computation in a separate thread
        CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            long result = calculator.memoized(n);
            long timeTaken = System.currentTimeMillis() - start;

            // Get the memo table for visualization
            Map<Integer, Long> memoTable = calculator.getMemoizedWithVisualization(n);
            List<Integer> callOrder = calculator.getMemoizationCallOrder();
            Map<Integer, Integer> callCounts = calculator.getCallCounts();

            return new Object[]{result, timeTaken, memoTable, callOrder, callCounts};
        }).thenAcceptAsync(result -> {
            long fibResult = (long) result[0];
            long timeTaken = (long) result[1];
            Map<Integer, Long> memoTable = (Map<Integer, Long>) result[2];
            List<Integer> callOrder = (List<Integer>) result[3];
            Map<Integer, Integer> callCounts = (Map<Integer, Integer>) result[4];

            resultArea.appendText("Result: F(" + n + ") = " + fibResult + "\n");
            resultArea.appendText("Time taken: " + timeTaken + " ms\n");
            resultArea.appendText("Total function calls: " + calculator.getTotalCalls() + "\n");
            resultArea.appendText("Cache hits: " + calculator.getCacheHits() + "\n");
            resultArea.appendText("Time complexity: O(n)\n");
            resultArea.appendText("Space complexity: O(n) for the memoization table\n");

            // Update chart with this single point
            updateChart("Memoization", n, fibResult);

            // Show the memoization visualization
            Platform.runLater(() -> {
                memoPane.visualizeMemoization(memoTable, callOrder, callCounts);
                visualizationTabPane.getSelectionModel().select(1); // Select the memoization tab
            });
        }, Platform::runLater);
    }

    public void handleTabulationAction(ActionEvent event) {
        int n = validateInput();
        if (n < 0) return;

        // Clear previous results
        resultArea.clear();
        resultArea.appendText("Calculating Fibonacci(" + n + ") using tabulation (bottom-up)...\n\n");

        // Use a CompletableFuture to handle the computation in a separate thread
        CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            long result = calculator.tabulated(n);
            long timeTaken = System.currentTimeMillis() - start;

            // Get the DP table for visualization
            List<Long> dpTable = calculator.getTabulatedWithSteps(n);

            return new Object[]{result, timeTaken, dpTable};
        }).thenAcceptAsync(result -> {
            long fibResult = (long) result[0];
            long timeTaken = (long) result[1];
            List<Long> dpTable = (List<Long>) result[2];

            resultArea.appendText("Result: F(" + n + ") = " + fibResult + "\n");
            resultArea.appendText("Time taken: " + timeTaken + " ms\n");
            resultArea.appendText("Time complexity: O(n)\n");
            resultArea.appendText("Space complexity: O(n) for the dp table\n");

            // Update chart with this single point
            updateChart("Tabulation", n, fibResult);

            // Show the tabulation visualization
            Platform.runLater(() -> {
                tabPane.visualizeTabulation(dpTable);
                visualizationTabPane.getSelectionModel().select(2); // Select the tabulation tab
            });
        }, Platform::runLater);
    }

    public void handleCompareAction(ActionEvent event) {
        int n = validateInput();
        if (n < 0) return;

        // Clear previous results
        resultArea.clear();
        resultArea.appendText("Comparing all three approaches for Fibonacci(" + n + ")...\n\n");

        // Use a CompletableFuture to handle the computation in a separate thread
        CompletableFuture.supplyAsync(() -> {
            // For recursive, we'll limit to smaller n due to exponential growth
            long recursiveResult;
            long recursiveTime;
            int recursiveCalls;

            if (n <= 40) {
                long start = System.currentTimeMillis();
                recursiveResult = calculator.recursive(n);
                recursiveTime = System.currentTimeMillis() - start;
                recursiveCalls = calculator.getRecursionCount();
            } else {
                recursiveResult = -1; // Too large for recursive
                recursiveTime = -1;
                recursiveCalls = -1;
            }

            // Memoization
            calculator.resetCounters();
            long memoStart = System.currentTimeMillis();
            long memoResult = calculator.memoized(n);
            long memoTime = System.currentTimeMillis() - memoStart;
            int totalCalls = calculator.getTotalCalls();
            int cacheHits = calculator.getCacheHits();

            // Tabulation
            long tabStart = System.currentTimeMillis();
            long tabResult = calculator.tabulated(n);
            long tabTime = System.currentTimeMillis() - tabStart;

            return new Object[]{
                    recursiveResult, recursiveTime, recursiveCalls,
                    memoResult, memoTime, totalCalls, cacheHits,
                    tabResult, tabTime
            };
        }).thenAcceptAsync(result -> {
            long recursiveResult = (long) result[0];
            long recursiveTime = (long) result[1];
            int recursiveCalls = (int) result[2];
            long memoResult = (long) result[3];
            long memoTime = (long) result[4];
            int totalCalls = (int) result[5];
            int cacheHits = (int) result[6];
            long tabResult = (long) result[7];
            long tabTime = (long) result[8];

            // Display results in a comparison table
            resultArea.appendText("╔════════════════════╦════════════════╦════════════════╦════════════════╗\n");
            resultArea.appendText("║ Metric             ║ Recursive      ║ Memoization    ║ Tabulation     ║\n");
            resultArea.appendText("╠════════════════════╬════════════════╬════════════════╬════════════════╣\n");

            // Result row
            resultArea.appendText("║ Result F(" + n + ")      ║ ");
            if (recursiveResult == -1) {
                resultArea.appendText("Too large      ║ ");
            } else {
                resultArea.appendText(padRight(String.valueOf(recursiveResult), 14) + " ║ ");
            }
            resultArea.appendText(padRight(String.valueOf(memoResult), 14) + " ║ ");
            resultArea.appendText(padRight(String.valueOf(tabResult), 14) + " ║\n");

            // Time row
            resultArea.appendText("║ Time (ms)          ║ ");
            if (recursiveTime == -1) {
                resultArea.appendText("Too slow       ║ ");
            } else {
                resultArea.appendText(padRight(String.valueOf(recursiveTime), 14) + " ║ ");
            }
            resultArea.appendText(padRight(String.valueOf(memoTime), 14) + " ║ ");
            resultArea.appendText(padRight(String.valueOf(tabTime), 14) + " ║\n");

            // Function calls row
            resultArea.appendText("║ Function calls     ║ ");
            if (recursiveCalls == -1) {
                resultArea.appendText("N/A            ║ ");
            } else {
                resultArea.appendText(padRight(String.valueOf(recursiveCalls), 14) + " ║ ");
            }
            resultArea.appendText(padRight(String.valueOf(totalCalls), 14) + " ║ ");
            resultArea.appendText(padRight("N/A", 14) + " ║\n");

            // Cache hits row
            resultArea.appendText("║ Cache hits         ║ ");
            resultArea.appendText(padRight("N/A", 14) + " ║ ");
            resultArea.appendText(padRight(String.valueOf(cacheHits), 14) + " ║ ");
            resultArea.appendText(padRight("N/A", 14) + " ║\n");

            // Time complexity row
            resultArea.appendText("║ Time complexity    ║ ");
            resultArea.appendText(padRight("O(2^n)", 14) + " ║ ");
            resultArea.appendText(padRight("O(n)", 14) + " ║ ");
            resultArea.appendText(padRight("O(n)", 14) + " ║\n");

            // Space complexity row
            resultArea.appendText("║ Space complexity   ║ ");
            resultArea.appendText(padRight("O(n)", 14) + " ║ ");
            resultArea.appendText(padRight("O(n)", 14) + " ║ ");
            resultArea.appendText(padRight("O(n)", 14) + " ║\n");

            resultArea.appendText("╚════════════════════╩════════════════╩════════════════╩════════════════╝\n\n");

            resultArea.appendText("Conclusion: ");
            if (n <= 10) {
                resultArea.appendText("For small values of n, all three approaches are viable.\n");
            } else if (n <= 40) {
                resultArea.appendText("For medium values of n, memoization and tabulation are significantly more efficient.\n");
            } else {
                resultArea.appendText("For large values of n, only memoization and tabulation are feasible due to the exponential growth of recursive calls.\n");
            }

            // Update chart with comparative series
            updateChartWithAllValues(n);

            // Show the chart tab
            Platform.runLater(() -> {
                visualizationTabPane.getSelectionModel().select(3); // Select the chart tab
            });
        }, Platform::runLater);
    }

    private int validateInput() {
        String input = inputField.getText().trim();

        try {
            int n = Integer.parseInt(input);
            if (n < 0) {
                showError("Input must be a non-negative integer.");
                return -1;
            }
            if (n > 92) {
                showError("Values above 92 will exceed the range of long in Java.\nPlease enter a smaller value.");
                return -1;
            }
            return n;
        } catch (NumberFormatException e) {
            showError("Invalid input. Please enter a valid integer.");
            return -1;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateChart(String seriesName, int n, long result) {
        // Clear existing series with the same name
        chart.getData().removeIf(series -> series.getName().equals(seriesName));

        // Create new series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        // Add the data point
        series.getData().add(new XYChart.Data<>(n, result));

        // Add the series to the chart
        chart.getData().add(series);
    }

    private void updateChartWithAllValues(int maxN) {
        // Clear previous data
        chart.getData().clear();

        // Create series for each algorithm
        XYChart.Series<Number, Number> recursiveSeries = new XYChart.Series<>();
        recursiveSeries.setName("Recursive");

        XYChart.Series<Number, Number> memoSeries = new XYChart.Series<>();
        memoSeries.setName("Memoization");

        XYChart.Series<Number, Number> tabSeries = new XYChart.Series<>();
        tabSeries.setName("Tabulation");

        // Calculate and add data points
        calculator.resetCounters();
        for (int i = 0; i <= Math.min(maxN, 20); i++) {  // Only show up to 20 points for clarity
            // Recursive might be too slow for larger values
            if (i <= 15) {
                recursiveSeries.getData().add(new XYChart.Data<>(i, calculator.recursive(i)));
            }

            calculator.resetCounters();
            memoSeries.getData().add(new XYChart.Data<>(i, calculator.memoized(i)));

            tabSeries.getData().add(new XYChart.Data<>(i, calculator.tabulated(i)));
        }

        // Add the series to the chart
        chart.getData().addAll(recursiveSeries, memoSeries, tabSeries);
    }

    private String padRight(String s, int width) {
        return String.format("%-" + width + "s", s);
    }
}
