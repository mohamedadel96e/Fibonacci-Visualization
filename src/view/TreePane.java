package view;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.FibNode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class TreePane extends Pane {
    private static final double BASE_H_GAP = 80;
    private static final double V_GAP = 70;
    private static final double NODE_RADIUS = 20;
    private static final double MIN_H_GAP = 30;

    private double treeWidth = 800;
    private double treeHeight = 600;
    private double maxDepth = 0;
    private double currentScale = 1.0;
    private final Scale scaleTransform = new Scale();

    public TreePane() {
        setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1;");
        this.getTransforms().add(scaleTransform);
    }

    public void drawTree(FibNode root) {
        getChildren().clear();
        if (root == null) return;

        calculateTreeDimensions(root, 0);
        double initialHGap = BASE_H_GAP * Math.pow(1.8, maxDepth-1);
        drawNode(root, treeWidth/2, 50, initialHGap);

        setPrefSize(treeWidth, treeHeight);
        resetZoom();
    }

    public void zoomIn() {
        currentScale *= 1.2;
        applyZoom();
    }

    public void zoomOut() {
        currentScale /= 1.2;
        applyZoom();
    }

    public void resetZoom() {
        currentScale = 1.0;
        applyZoom();
    }

    private void applyZoom() {
        scaleTransform.setX(currentScale);
        scaleTransform.setY(currentScale);
    }

    private void calculateTreeDimensions(FibNode node, int currentDepth) {
        if (node == null) return;

        maxDepth = Math.max(maxDepth, currentDepth);
        calculateTreeDimensions(node.left, currentDepth + 1);
        calculateTreeDimensions(node.right, currentDepth + 1);

        treeWidth = Math.max(treeWidth, BASE_H_GAP * Math.pow(2, maxDepth + 1));
        treeHeight = (maxDepth + 2) * V_GAP + 100;
    }

    private void drawNode(FibNode node, double x, double y, double hGap) {
        if (node == null) return;

        NodeView view = new NodeView("F(" + node.value + ")");
        view.setLayoutX(x - NODE_RADIUS);
        view.setLayoutY(y - NODE_RADIUS);
        getChildren().add(view);
        node.view = view;

        double childHGap = Math.max(hGap / 1.8, MIN_H_GAP);
        double childY = y + V_GAP;

        if (node.left != null) {
            double childX = x - hGap;
            drawNode(node.left, childX, childY, childHGap);
            drawConnectingLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
        }

        if (node.right != null) {
            double childX = x + hGap;
            drawNode(node.right, childX, childY, childHGap);
            drawConnectingLine(x, y + NODE_RADIUS, childX, childY - NODE_RADIUS);
        }
    }

    private void drawConnectingLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.rgb(100, 100, 100));
        line.setStrokeWidth(1.2);
        line.getStrokeDashArray().addAll(4d, 4d);
        getChildren().add(line);
    }
}