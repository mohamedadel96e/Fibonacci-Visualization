package org.example.fibonaccivisualization.model;

import org.example.fibonaccivisualization.view.NodeView;

public class FibNode {
    public int value;
    public FibNode left, right;
    public NodeView view;

    public FibNode(int value) {
        this.value = value;
    }
}
