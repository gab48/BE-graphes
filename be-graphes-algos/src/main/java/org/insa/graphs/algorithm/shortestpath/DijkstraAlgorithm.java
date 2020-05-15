package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.Label;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	protected Label makeLabel (Node node){
		return new Label(node);
	}

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null ;
        ArrayList<Label> labels = new ArrayList<Label>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        Label xLabel = null;
        Label yLabel = null;
        
        // Initialisation
        for(Node node : data.getGraph().getNodes()) {
        	labels.add(makeLabel(node));
        }
        labels.get(data.getOrigin().getId()).setCost(0);
        tas.insert(labels.get(data.getOrigin().getId()));
        this.notifyOriginProcessed(data.getOrigin());
        
        // Dijkstra
        boolean found = false;
        while(!tas.isEmpty() && !found) {
        	xLabel = tas.deleteMin();
        	xLabel.setMark();
        	this.notifyNodeMarked(xLabel.getNode());
        	found = xLabel.getNode().equals(data.getDestination());
        	for(Arc arc : xLabel.getNode().getSuccessors() ) {
        		if(data.isAllowed(arc)) {
	        		yLabel = labels.get(arc.getDestination().getId());
	        		if (!yLabel.isMarked()) {
	        			if (yLabel.getCost() > (xLabel.getCost() + data.getCost(arc))) {
	        				try {
	        					tas.remove(yLabel);
	        				} catch (Exception e) {
	        					this.notifyNodeReached(yLabel.getNode());
	        				}
	        				yLabel.setCost(xLabel.getCost() + data.getCost(arc));
	        				yLabel.setFather(arc);
	        				tas.insert(yLabel);
	        			}
	        		}
        		}
        	}
        }
        
        // Construct Path
        if (found) {
        	this.notifyDestinationReached(data.getDestination());
        	ArrayList<Arc> arcs = new ArrayList<Arc>();
        	Arc currentArc = labels.get(data.getDestination().getId()).getFather();
        	boolean completed = false;
        
        	while(!completed) {
        		arcs.add(0, currentArc);
        		completed = currentArc.getOrigin().equals(data.getOrigin());
        		if (!completed) {
        			currentArc = labels.get(currentArc.getOrigin().getId()).getFather();
        		}
        	}
        
        	Path shortest = new Path(data.getGraph(), arcs);
        	//Path checkShortest = Path.createShortestPathFromNodes(data.getGraph(), shortest.getNodes());
			//Path checkFastest = Path.createFastestPathFromNodes(data.getGraph(), shortest.getNodes());
			//System.out.println("Equal to shortest path from nodes ? " + shortest.equalTo(checkShortest));
			//System.out.println("Equal to fastest path from nodes ? " + shortest.equalTo(checkFastest));
        
        	solution = new ShortestPathSolution(data, Status.FEASIBLE, shortest);
        } else {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE, null);
        }
        	
        
        return solution;
    }

}
