package view;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NodeView extends StackPane {
    private static final double RADIUS = 25;

    public NodeView(String text) {
        Circle circle = new Circle(RADIUS);

        // Color based on Fibonacci value (simple hash for color variation)
        int colorSeed = text.hashCode();
        Color fillColor = Color.hsb(colorSeed % 360, 0.7, 0.9);
        Color strokeColor = fillColor.darker();

        circle.setFill(fillColor);
        circle.setStroke(strokeColor);
        circle.setStrokeWidth(2);

        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        label.setTextFill(Color.WHITE);

        getChildren().addAll(circle, label);

        // Tooltip with more information
        Tooltip tooltip = new Tooltip("Calculating Fibonacci(" + text.substring(2, text.length()-1) + ")");
        Tooltip.install(this, tooltip);
    }
}