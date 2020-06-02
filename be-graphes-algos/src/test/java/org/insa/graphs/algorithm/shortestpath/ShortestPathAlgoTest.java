package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public abstract class ShortestPathAlgoTest {

    private static GraphFormatter belgique, htGaronne, carre;
    private static ArrayList<AlgoTester> tests = new ArrayList<>();
    private static ArcInspector[] inspectors;
    protected static Method method = Method.ORACLE; //Default Value, can be changed in subclasses

    /**
     * There is two different testing methods
     */
    protected enum Method {ORACLE, NOORACLE};

    /**
     * Class used to load the graph
     */
    private static class GraphFormatter {
        private Graph graph;
        private final String mapName;
        private static final String mapPath = "C:\\Users\\gabin\\Documents\\INSA\\3MIC\\Java\\be-graphes\\Maps\\";

        public Graph getGraph() {
            return this.graph;
        }

        public String getMapName() {
            return mapName;
        }

        public GraphFormatter(String name) {
            this.graph = null;
            this.mapName = name.substring(0,1).toUpperCase() + name.substring(1);
            String fileName = name+".mapgr";

            DataInputStream stream;
            GraphReader reader;
            try {
                stream = new DataInputStream(new BufferedInputStream(
                        new FileInputStream(GraphFormatter.mapPath + fileName)));
                reader = new BinaryGraphReader(stream);
                this.graph = reader.read();
            }
            catch (IOException e) {
                System.out.println("Cannot find the following file : "+ fileName + " at " + GraphFormatter.mapPath);
                e.printStackTrace();
            }
        }

    }

    /**
     * Use to run the tested algorithm
     * @param data object that regroup the information to run a test (graph, origin, destination)
     * @return the solution of the algo
     */
    protected abstract ShortestPathSolution computeSolution(ShortestPathData data);

    /**
     * Algo tester regroup information to run a test with or without oracle
     */
    private static class AlgoTester {
        private ShortestPathSolution standard;
        private ShortestPathSolution solution;
        private final ShortestPathData data;

        public boolean isFeasible() {
            return this.standard.getPath() != null;
        }

        public AlgoTester(ShortestPathData data) {
            if (method == Method.ORACLE) {
                this.standard = new BellmanFordAlgorithm(data).run();
                this.data = data;
            } else {
                this.standard = null;
                this.data = data;
            }
        }

        public void constructSolutions(ShortestPathSolution solution) {
            this.solution = solution;
            if (method == Method.NOORACLE) {
                Path pathFromNodes = null;
                Path solutionPath = this.solution.getPath();
                if (solutionPath != null) {
                    if (this.data.getMode() == AbstractInputData.Mode.LENGTH) {
                        pathFromNodes = Path.createShortestPathFromNodes(this.data.getGraph(), solutionPath.getNodes());
                    } else {
                        pathFromNodes = Path.createFastestPathFromNodes(this.data.getGraph(), solutionPath.getNodes());
                    }
                }
                this.standard = new ShortestPathSolution(this.data, this.solution.getStatus(), pathFromNodes);
            }
        }

    }

    /**
     * Add a specifict test from origin to destination in the given graph
     * @param graphF
     * @param origin
     * @param destination
     */
    private static void addTest(GraphFormatter graphF, int origin, int destination) {
        Graph graph = graphF.getGraph();
        for (ArcInspector inspector : inspectors) {
            ShortestPathData data = new ShortestPathData(graph, graph.get(origin), graph.get(destination), inspector);
            tests.add(new AlgoTester(data));
        }
    }

    /**
     * Add a test for the given graph, and choose the origin and destination randomly
     * @param graphF
     */
    private static void addTest(GraphFormatter graphF) {
        int origin = ThreadLocalRandom.current().nextInt(0, graphF.graph.size());
        int destination = ThreadLocalRandom.current().nextInt(0, graphF.graph.size());
        addTest(graphF, origin, destination);
    }

    /**
     * List of scenario we want to test
     */
    private static void computeTests() {
        //Unfeasible
        addTest(carre, 0, 0);
        addTest(belgique, 407916, 183547);
        //Feasible
        addTest(carre, 0, 12);
        addTest(htGaronne, 67779, 87958);
        addTest(htGaronne, 110707, 79697);
        addTest(belgique, 761908,454325);
        addTest(belgique,442335, 591002);

        //Random tests
        addTest(carre);
        addTest(htGaronne);
        addTest(belgique);
    }

    @BeforeClass
    public static void initAll(){
        carre = new GraphFormatter("carre");
        belgique = new GraphFormatter("belgium");
        htGaronne = new GraphFormatter("haute-garonne");
        inspectors = new ArcInspector[] {
                ArcInspectorFactory.getAllFilters().get(0),
                ArcInspectorFactory.getAllFilters().get(2)
        };

        computeTests();

    }

    @Before
    public void init(){
        for (AlgoTester test : tests) {
            test.constructSolutions(this.computeSolution(test.data));
        }
    }

    @Test
    public void testEndNodes() {
        for (AlgoTester test : tests) {
            assertEquals(test.standard.getInputData().getOrigin().getId(), test.solution.getInputData().getOrigin().getId());
            assertEquals(test.standard.getInputData().getDestination().getId(), test.solution.getInputData().getDestination().getId());
        }
    }

    @Test
    public void testPathEmptiness() {
        for (AlgoTester test : tests) {
            if (test.isFeasible()) {
                assertEquals(test.standard.getPath().isEmpty(), test.solution.getPath().isEmpty());
            } else {
                assertNull(test.solution.getPath());
            }
        }
    }

    @Test
    public void testPathValidity() {
        for (AlgoTester test : tests) {
            if (test.isFeasible()) {
                assertEquals(test.standard.getPath().isValid(), test.solution.getPath().isValid());
            } else {
                assertNull(test.solution.getPath());
            }
        }
    }

    @Test
    public void testPathLength() {
        for (AlgoTester test : tests) {
            if (test.isFeasible()) {
                assertEquals(test.standard.getPath().getLength(), test.solution.getPath().getLength(), 1e-3);
            } else {
                assertNull(test.solution.getPath());
            }
        }
    }

    @Test
    public void testPathMinimunTravelTime() {
        for (AlgoTester test : tests) {
            if (test.isFeasible()) {
                assertEquals(test.standard.getPath().getMinimumTravelTime(), test.solution.getPath().getMinimumTravelTime(), 1e-3);
            } else {
                assertNull(test.solution.getPath());
            }
        }
    }

    @Test
    public void testPathNumberOfArcs() {
        for (AlgoTester test : tests) {
            if (test.isFeasible()) {
                assertEquals(test.standard.getPath().getArcs().size(), test.solution.getPath().getArcs().size());
            } else {
                assertNull(test.solution.getPath());
            }
        }
    }

    @Test
    public void tmpsExec() {
        if (method == Method.ORACLE) {
            for (AlgoTester test : tests) {
                System.out.println("[Chemin] carte=" + test.standard.getInputData().getGraph().getMapName() + " points=" + test.standard.getInputData().getOrigin().getId() + "/" + test.standard.getInputData().getDestination().getId());
                System.out.println("Temps Belleman : " + test.standard.getSolvingTime().toString());
                System.out.println("Temps solution : " + test.solution.getSolvingTime().toString());
                System.out.println("");
            }
        }
    }
}
