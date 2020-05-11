package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label>{
	
	private Node currentNode;
	private boolean marked;
	private double cost;
	private Arc father;
	
	public Label(Node node, boolean mark, double cost, Arc father) {
		this.currentNode = node;
		this.marked = mark;
		this.cost = cost;
		this.father = father;
	}
	
	public Label(Node node) {
		this.currentNode = node;
		this.marked = false;
		this.cost = Double.POSITIVE_INFINITY;
		this.father = null;
	}
	
	public Node getNode() {
		return this.currentNode;
	}
	
	public double getCost() {
		return this.cost;
	}
	
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
		return Double.compare(this.getCost(), l.getCost());
	}
	
	public String toString() {
		return Double.toString(this.getCost());
	}
}
