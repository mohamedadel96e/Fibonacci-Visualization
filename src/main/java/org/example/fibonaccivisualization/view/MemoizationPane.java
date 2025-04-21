package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;

public class MemoizationPane extends VBox {
    private static final int CELL_SIZE = 60;
    private static final int SEQUENCE_CELL_SIZE = 40;

    public MemoizationPane() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #ecf0f1;");
    }

    public void visualizeMemoization(Map<Integer, Long> memoTable, List<Integer> callOrder, Map<Integer, Integer> callCounts) {
        getChildren().clear();

        // Title
        Label titleLabel = new Label("Memoization Process Visualization");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.valueOf("#2c3e50"));
        getChildren().add(titleLabel);

        // Visualization of the memoization table
        HBox tableContainer = new HBox(5);
        tableContainer.setAlignment(Pos.CENTER);

        // Sort keys for ordered display
        memoTable.keySet().stream().sorted().forEach(key -> {
            VBox cell = createMemoCell(key, memoTable.get(key), callCounts.getOrDefault(key, 0));
            tableContainer.getChildren().add(cell);
        });

        getChildren().add(tableContainer);

        // Call sequence visualization
        Label sequenceLabel = new Label("Function Call Sequence:");
        sequenceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        sequenceLabel.setTextFill(Color.valueOf("#2c3e50"));
        getChildren().add(sequenceLabel);

        FlowPane sequenceFlow = new FlowPane(5, 5);
        sequenceFlow.setAlignment(Pos.CENTER);
        sequenceFlow.setPrefWrapLength(800); // Adjust based on your layout

        for (int i = 0; i < callOrder.size(); i++) {
            Integer value = callOrder.get(i);
            StackPane seqCell = createSequenceCell(value, i);
            sequenceFlow.getChildren().add(seqCell);
        }

        getChildren().add(sequenceFlow);

        // Legend
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER);

        legend.getChildren().addAll(
                createLegendItem("Base case", Color.ORANGE),
                createLegendItem("Calculated", Color.LIGHTGREEN),
                createLegendItem("Cache hit", Color.LIGHTBLUE)
        );

        getChildren().add(legend);
    }

    private VBox createMemoCell(int key, Long value, int callCount) {
        Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);

        // Color based on value
        Color fillColor;
        if (key <= 1) {
            // Base cases
            fillColor = Color.ORANGE;
        } else if (callCount > 1) {
            // Cache hits
            fillColor = Color.LIGHTBLUE;
        } else {
            // Calculated values
            fillColor = Color.LIGHTGREEN;
        }

        rect.setFill(fillColor);
        rect.setStroke(Color.valueOf("#34495e"));
        rect.setStrokeWidth(1);
        rect.setArcHeight(10);
        rect.setArcWidth(10);

        Label keyLabel = new Label("F(" + key + ")");
        keyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label valueLabel = new Label(value.toString());
        valueLabel.setFont(Font.font("Arial", 12));

        Label callCountLabel = new Label("Calls: " + callCount);
        callCountLabel.setFont(Font.font("Arial", 10));

        VBox cell = new VBox(3, keyLabel, valueLabel, callCountLabel);
        cell.setAlignment(Pos.CENTER);
        cell.setPadding(new Insets(5));

        StackPane cellPane = new StackPane(rect, cell);

        Tooltip tooltip = new Tooltip("F(" + key + ") = " + value + "\nCalled " + callCount + " times");
        Tooltip.install(cellPane, tooltip);

        VBox container = new VBox(5, cellPane);
        container.setAlignment(Pos.CENTER);

        return container;
    }

    private StackPane createSequenceCell(int value, int index) {
        Rectangle rect = new Rectangle(SEQUENCE_CELL_SIZE, SEQUENCE_CELL_SIZE);

        // Color based on value
        Color fillColor;
        if (value <= 1) {
            // Base cases
            fillColor = Color.ORANGE;
        } else {
            // Regular calls
            fillColor = Color.LIGHTGREEN;
        }

        rect.setFill(fillColor);
        rect.setStroke(Color.valueOf("#34495e"));
        rect.setStrokeWidth(1);
        rect.setArcHeight(5);
        rect.setArcWidth(5);

        Label valueLabel = new Label("F(" + value + ")");
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        VBox cellContent = new VBox(valueLabel);
        cellContent.setAlignment(Pos.CENTER);

        StackPane cell = new StackPane(rect, cellContent);

        Tooltip tooltip = new Tooltip("Call #" + (index + 1) + ": F(" + value + ")");
        Tooltip.install(cell, tooltip);

        return cell;
    }

    private HBox createLegendItem(String text, Color color) {
        Rectangle rect = new Rectangle(20, 20);
        rect.setFill(color);
        rect.setStroke(Color.valueOf("#34495e"));
        rect.setStrokeWidth(1);
        rect.setArcHeight(5);
        rect.setArcWidth(5);

        Label label = new Label(text);
        label.setFont(Font.font("Arial", 12));

        HBox item = new HBox(5, rect, label);
        item.setAlignment(Pos.CENTER_LEFT);

        return item;
    }
}

