package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>{
	
	protected Node node;
	protected boolean marked;
	protected double cost;
	protected Arc father;
	
	public Label(Node node, boolean mark, double cost, Arc father) {
		this.node = node;
		this.marked = mark;
		this.cost = cost;
		this.father = father;
	}
	
	public Label(Node node) {
		this.node = node;
		this.marked = false;
		this.cost = Double.POSITIVE_INFINITY;
		this.father = null;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public double getCost() {
		return this.cost;
	}

	public double getTotalCost() { return this.cost; }
	
	public void setCost(double c) {
		this.cost = c;
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	public void setMark() {
		this.marked = true;
	}
	
	public Arc getFather() {
		return this.father;
	}
	
	public void setFather(Arc f) {
		this.father = f;
	}

	@Override
	public int compareTo(Label l) {
		int comp = Double.compare(this.getTotalCost(), l.getTotalCost());
		if (comp == 0) {
			comp = Double.compare(this.getTotalCost() - this.getCost(), l.getTotalCost() - l.getCost());
		}
		return comp;
	}
	
	public String toString() {
		return Double.toString(this.getCost());
	}
}
