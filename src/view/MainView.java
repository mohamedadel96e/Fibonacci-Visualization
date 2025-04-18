package view;

import controller.FibonacciController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {
    private final BorderPane root;
    private final TextField inputField;
    private final TextArea resultArea;
    private final LineChart<Number, Number> chart;
    private final TreePane treePane;
    private final TabPane tabPane;

    public MainView() {
        // Input Section
        inputField = new TextField();
        inputField.setPromptText("Enter Fibonacci number (n)");
        inputField.setMaxWidth(200);

        Label titleLabel = new Label("Fibonacci Calculator");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox headerBox = new HBox(20, titleLabel, inputField);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15));
        headerBox.setStyle("-fx-background-color: #3498db; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Buttons
        Button recursiveBtn = createStyledButton("Recursive Tree", "#e74c3c");
        Button memoBtn = createStyledButton("Memoization", "#2ecc71");
        Button tabBtn = createStyledButton("Tabulation", "#9b59b6");

        HBox buttons = new HBox(15, recursiveBtn, memoBtn, tabBtn);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(15));

        // Result Area
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPrefHeight(100);
        resultArea.setStyle("-fx-font-family: monospace; -fx-font-size: 14px;");

        // Chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("n");
        yAxis.setLabel("F(n)");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Fibonacci Sequence Growth");
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setLegendVisible(false);
        chart.setStyle("-fx-font-size: 14px;");

        // Tree Visualization
        treePane = new TreePane();

        // Create zoom controls
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
        ScrollPane scrollPane = new ScrollPane(treePane);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox treeContainer = new VBox(zoomControls, scrollPane);
        treeContainer.setPadding(new Insets(5));

        // Add to tab pane
        Tab treeTab = new Tab("Recursive Tree", treeContainer);


        // TabPane for different visualizations
        Tab chartTab = new Tab("Growth Chart", chart);
        tabPane = new TabPane();
        tabPane.getTabs().addAll(
                new Tab("Growth Chart", chart),
                treeTab
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox visualizationContainer = new VBox(tabPane);
        visualizationContainer.setPadding(new Insets(10));
        visualizationContainer.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Status Bar
        Label statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        HBox statusBar = new HBox(statusLabel);
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #ecf0f1;");

        // Main Layout
        VBox contentBox = new VBox(10, headerBox, buttons, resultArea, visualizationContainer);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: #f5f7fa;");

        root = new BorderPane();
        root.setCenter(contentBox);
        root.setBottom(statusBar);

        // Controller
        FibonacciController controller = new FibonacciController(inputField, resultArea, chart, treePane, statusLabel);
        recursiveBtn.setOnAction(controller::handleRecursiveTree);
        memoBtn.setOnAction(controller::handleMemoized);
        tabBtn.setOnAction(controller::handleTabulated);



    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", -20%); -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;"));
        return btn;
    }


    private Button createZoomButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 30px; " +
                "-fx-min-height: 30px; -fx-background-radius: 15px;");
        return btn;
    }


    public BorderPane getView() {
        return root;
    }
}