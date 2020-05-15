package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.utils.Label;
import org.insa.graphs.algorithm.utils.LabelStar;
import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    @Override
    protected Label makeLabel(Node node) {
        LabelStar label = new LabelStar(node);

        if(this.data.getMode() == AbstractInputData.Mode.TIME) {
            if (getInputData().getMaximumSpeed() != GraphStatistics.NO_MAXIMUM_SPEED) {
                label.setEstimatedTime(this.getInputData().getDestination(), this.data.getMaximumSpeed());
            } else {
                label.setEstimatedTime(this.getInputData().getDestination(), 130);
            }
        } else {
            label.setEstimatedLength(this.getInputData().getDestination());
        }

        return label;
    }

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

}
