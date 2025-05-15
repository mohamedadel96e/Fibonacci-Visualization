package org.example.fibonaccivisualization.view;

import org.example.fibonaccivisualization.controller.FibonacciController;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MainView {
    private final BorderPane root;
    private final TextField inputField;
    private final TextArea resultArea;
    private final LineChart<Number, Number> chart;
    private final TreePane treePane;
    private final MemoizationPane memoPane;
    private final TabulationPane tabPane;
    private final TabPane visualizationTabPane;

    public MainView() {
        // Input Section
        inputField = new TextField();
        inputField.setPromptText("Enter Fibonacci number (n)");
        inputField.setMaxWidth(450);
        inputField.setMinWidth(200);
        inputField.setMinHeight(40);
        inputField.setId("inputField");

        Label titleLabel = new Label("Fibonacci Calculator & Algorithm Visualizer");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox headerBox = new HBox(20, titleLabel, inputField);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #3498db; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Buttons
        Button recursiveBtn = createStyledButton("Recursive Tree", "#e74c3c");
        Button memoBtn = createStyledButton("Memoization", "#2ecc71");
        Button tabBtn = createStyledButton("Tabulation", "#9b59b6");
        Button compareBtn = createStyledButton("Compare All", "#f39c12");

        HBox buttons = new HBox(15, recursiveBtn, memoBtn, tabBtn, compareBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15));

        // Result Area
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(350);
        resultArea.setStyle("-fx-font-family: 'Fira Code', monospace; -fx-font-size: 14px;");
        resultArea.setMinHeight(100);

        // Chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("n");
        yAxis.setLabel("F(n)");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Fibonacci Sequence Growth");
        chart.setAnimated(true);
        chart.setCreateSymbols(true);
        chart.setLegendVisible(true);
        chart.setStyle("-fx-font-size: 14px;");

        // Tree Visualization
        treePane = new TreePane();

        // Create zoom controls for tree pane
        Button zoomInBtn = createZoomButton("+");
        Button zoomOutBtn = createZoomButton("-");
        Button resetZoomBtn = createZoomButton("⟲");

        zoomInBtn.setOnAction(e -> treePane.zoomIn());
        zoomOutBtn.setOnAction(e -> treePane.zoomOut());
        resetZoomBtn.setOnAction(e -> treePane.resetZoom());

        HBox zoomControls = new HBox(5, zoomInBtn, zoomOutBtn, resetZoomBtn);
        zoomControls.setAlignment(Pos.CENTER_RIGHT);
        zoomControls.setPadding(new Insets(5));

        // Create scroll pane with tree and zoom controls
        ScrollPane treeScrollPane = new ScrollPane(treePane);
        treeScrollPane.setFitToWidth(false);
        treeScrollPane.setFitToHeight(false);
        treeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        treeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


// Create VBox for tree tab with zoom controls and scroll pane
        VBox treeContainer = new VBox(5, zoomControls, treeScrollPane);
        treeContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(treeScrollPane, Priority.ALWAYS);

        // Memoization visualization
        memoPane = new MemoizationPane();
        ScrollPane memoScrollPane = new ScrollPane(memoPane);
        memoScrollPane.setFitToWidth(true);
        memoScrollPane.setFitToHeight(true);

        // Tabulation visualization
        tabPane = new TabulationPane();
        ScrollPane tabScrollPane = new ScrollPane(tabPane);
        tabScrollPane.setFitToWidth(true);
        tabScrollPane.setFitToHeight(true);

        // Create tabs
        visualizationTabPane = new TabPane();
        Tab treeTab = new Tab("Recursive Tree", treeContainer);
        Tab memoTab = new Tab("Memoization", memoScrollPane);
        Tab tabulationTab = new Tab("Tabulation", tabScrollPane);
        Tab chartTab = new Tab("Growth Chart", chart);

        visualizationTabPane.getTabs().addAll(treeTab, memoTab, tabulationTab, chartTab);
        visualizationTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Split pane for results and visualization
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(resultArea, visualizationTabPane);
        splitPane.setDividerPositions(0.3);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // Create border pane
        root = new BorderPane();
        root.setTop(new VBox(headerBox, buttons));
        root.setCenter(splitPane);
        root.setStyle("-fx-background-color: #f9f9f9;");

        // Create controller and wire up events
        FibonacciController controller = new FibonacciController(
                inputField, resultArea, chart, treePane, memoPane, tabPane, visualizationTabPane);

        recursiveBtn.setOnAction(controller::handleRecursiveAction);
        memoBtn.setOnAction(controller::handleMemoAction);
        tabBtn.setOnAction(controller::handleTabulationAction);
        compareBtn.setOnAction(controller::handleCompareAction);
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setMinWidth(120);
        button.setMinHeight(35);
        return button;
    }

    private Button createZoomButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setMinWidth(30);
        button.setMinHeight(30);
        return button;
    }

    public BorderPane getView() {
        return root;
    }
}