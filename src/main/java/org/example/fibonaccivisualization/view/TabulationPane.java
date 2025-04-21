package view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;

public class TabulationPane extends VBox {
    private static final int CELL_SIZE = 60;

    public TabulationPane() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #ecf0f1;");
    }

    public void visualizeTabulation(List<Long> dpTable) {
        getChildren().clear();

        // Title
        Label titleLabel = new Label("Tabulation Approach Visualization");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.valueOf("#2c3e50"));
        getChildren().add(titleLabel);

        // Explanation
        Label explanationLabel = new Label("Bottom-up approach: Starting with base cases, building up to the target value");
        explanationLabel.setFont(Font.font("Arial", 14));
        explanationLabel.setTextFill(Color.valueOf("#2c3e50"));
        getChildren().add(explanationLabel);

        // Visualization of the dp table
        FlowPane tableContainer = new FlowPane(10, 10);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPrefWrapLength(800); // Adjust based on your layout

        ParallelTransition allTransitions = new ParallelTransition();

        for (int i = 0; i < dpTable.size(); i++) {
            StackPane cell = createDpCell(i, dpTable.get(i));

            // Animation for appearing
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), cell);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setDelay(Duration.millis(i * 100));

            allTransitions.getChildren().add(fadeIn);
            tableContainer.getChildren().add(cell);
        }

        getChildren().add(tableContainer);

        // Add dependency arrows for larger tables
        if (dpTable.size() > 5) {
            HBox dependencyContainer = new HBox(10);
            dependencyContainer.setAlignment(Pos.CENTER);
            dependencyContainer.setPadding(new Insets(20, 0, 0, 0));

            Label depLabel = new Label("For each position i, F(i) = F(i-1) + F(i-2)");
            depLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            depLabel.setTextFill(Color.valueOf("#2c3e50"));

            dependencyContainer.getChildren().add(depLabel);
            getChildren().add(dependencyContainer);
        }

        // Complexity information
        VBox complexityInfo = new VBox(5);
        complexityInfo.setAlignment(Pos.CENTER);
        complexityInfo.setPadding(new Insets(10));

        Label timeComplexityLabel = new Label("Time Complexity: O(n)");
        timeComplexityLabel.setFont(Font.font("Arial", 14));
        timeComplexityLabel.setTextFill(Color.valueOf("#2c3e50"));

        Label spaceComplexityLabel = new Label("Space Complexity: O(n)");
        spaceComplexityLabel.setFont(Font.font("Arial", 14));
        spaceComplexityLabel.setTextFill(Color.valueOf("#2c3e50"));

        complexityInfo.getChildren().addAll(timeComplexityLabel, spaceComplexityLabel);
        getChildren().add(complexityInfo);

        // Play all animations
        allTransitions.play();
    }

    private StackPane createDpCell(int index, Long value) {
        Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);

        // Color based on index
        Color fillColor;
        if (index <= 1) {
            // Base cases
            fillColor = Color.ORANGE;
        } else {
            // Calculate color based on position (gradient effect)
            double hue = (index * 10) % 360;
            fillColor = Color.hsb(hue, 0.6, 0.9);
        }

        rect.setFill(fillColor);
        rect.setStroke(Color.valueOf("#34495e"));
        rect.setStrokeWidth(1);
        rect.setArcHeight(10);
        rect.setArcWidth(10);

        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER);

        Label indexLabel = new Label("F(" + index + ")");
        indexLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        indexLabel.setTextFill(Color.WHITE);

        Label valueLabel = new Label(value.toString());
        valueLabel.setFont(Font.font("Arial", 12));
        valueLabel.setTextFill(Color.WHITE);

        content.getChildren().addAll(indexLabel, valueLabel);

        StackPane cell = new StackPane(rect, content);

        // Formula for non-base cases
        if (index > 1) {
            String formula = "F(" + (index-1) + ") + F(" + (index-2) + ") = " +
                    value;
            Tooltip tooltip = new Tooltip(formula);
            Tooltip.install(cell, tooltip);
        } else {
            Tooltip tooltip = new Tooltip("Base case: F(" + index + ") = " + value);
            Tooltip.install(cell, tooltip);
        }

        return cell;
    }
}

