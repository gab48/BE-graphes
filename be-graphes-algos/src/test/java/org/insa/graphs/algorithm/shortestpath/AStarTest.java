package org.insa.graphs.algorithm.shortestpath;

public class AStarTest extends ShortestPathAlgoTest{
    public AStarTest() {

        super.method = Method.NOORACLE;
    }
    @Override
    protected ShortestPathSolution computeSolution(ShortestPathData data) {
        return new AStarAlgorithm(data).run();
    }
}
