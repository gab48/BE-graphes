package org.insa.graphs.algorithm.shortestpath;

public class AStarTest extends ShortestPathAlgoTest{
    public AStarTest() {

        super.method = Method.ORACLE;
    }
    @Override
    protected ShortestPathSolution computeSolution(ShortestPathData data) {
        return new AStarAlgorithm(data).run();
    }
}
