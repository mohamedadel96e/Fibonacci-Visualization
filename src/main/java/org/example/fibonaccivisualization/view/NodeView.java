package org.example.fibonaccivisualization.view;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NodeView extends StackPane {
    private static final double RADIUS = 25;

    public NodeView(String text) {
        // Extract the value from the text (format is "F(n)")
        String valueStr = text.substring(2, text.length() - 1);
        int value = Integer.parseInt(valueStr);

        Circle circle = new Circle(RADIUS);

        // Color based on Fibonacci value
        Color fillColor;
        if (value <= 1) {
            // Base cases - special color
            fillColor = Color.ORANGE;
        } else {
            // Generate color based on value
            double hue = (value * 30) % 360;  // Cycle through colors
            fillColor = Color.hsb(hue, 0.7, 0.9);
        }

        Color strokeColor = fillColor.darker();
        circle.setFill(fillColor);
        circle.setStroke(strokeColor);
        circle.setStrokeWidth(2);

        // Create label showing both F(n) notation and the value
        VBox labelBox = new VBox(2);
        labelBox.setAlignment(javafx.geometry.Pos.CENTER);

        Label mainLabel = new Label(text);
        mainLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        mainLabel.setTextFill(Color.WHITE);

        labelBox.getChildren().add(mainLabel);

        getChildren().addAll(circle, labelBox);

        // Tooltip with more information
        Tooltip tooltip = new Tooltip("Calculating Fibonacci(" + valueStr + ")");
        Tooltip.install(this, tooltip);
    }

    // Update the node to show the Fibonacci result
    public void updateWithResult(long result) {
        VBox labelBox = (VBox) getChildren().get(1);

        // Check if the result label already exists
        if (labelBox.getChildren().size() == 1) {
            Label resultLabel = new Label("= " + result);
            resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            resultLabel.setTextFill(Color.WHITE);
            labelBox.getChildren().add(resultLabel);
        }
    }
}
