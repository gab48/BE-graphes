package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private double estimatedCost;

    public LabelStar(Node node, boolean mark, double cost, Arc father) {
        super(node, mark, cost, father);
    }

    public LabelStar(Node node) {
        super(node);
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    private double getDistanceBetweenNodes(Node destNode) {
        return this.node.getPoint().distanceTo(destNode.getPoint());
    }

    public void setEstimatedTime(Node destNode, int speed) {
        this.estimatedCost = getDistanceBetweenNodes(destNode) / speed;
    }

    public void setEstimatedLength(Node destNode) {
        this.estimatedCost = getDistanceBetweenNodes(destNode);
    }

    public double getTotalCost() {
        return this.cost + this.estimatedCost;
    }

    @Override
    public String toString() {
        return super.toString() + "//" + Double.toString(this.estimatedCost);
    }
}
