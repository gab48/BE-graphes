package org.insa.graphs.algorithm.shortestpath;

public class DijkstraTest extends ShortestPathAlgoTest {
    public DijkstraTest() {
        super.method = Method.ORACLE;
    }
    @Override
    protected ShortestPathSolution computeSolution(ShortestPathData data) {
        return new DijkstraAlgorithm(data).run();
    }
}
