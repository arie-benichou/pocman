
package fr.designpattern.pocman.cpp;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import fr.designpattern.pocman.game.Maze;
import fr.designpattern.pocman.graph.Node;
import fr.designpattern.pocman.graph.UndirectedGraph;
import fr.designpattern.pocman.graph.UndirectedGraph.Builder;
import fr.designpattern.pocman.graph.Vertex;
import fr.designpattern.pocman.graph.WeightedEdge;
import fr.designpattern.pocman.view.MazeView;

public final class OpenCPP<T> {

    private final ClosedCPP<T> closedCPP;
    private Maze maze;

    public ClosedCPP<T> getClosedCPP() {
        return this.closedCPP;
    }

    public static <T> OpenCPP<T> from(final ClosedCPP<T> closedCPP) {
        Preconditions.checkArgument(closedCPP != null);
        return new OpenCPP<T>(closedCPP);
    }

    private OpenCPP(final ClosedCPP<T> closedCPP) {
        this.closedCPP = closedCPP;
    }

    public UndirectedGraph<T> getGraph() {
        return this.getClosedCPP().getGraph();
    }

    public Double getLowerBoundCost() {
        return this.getClosedCPP().getLowerBoundCost();
    }

    public Solution<Node<T>> solveFrom(final T startingVertex) {

        Preconditions.checkArgument(startingVertex != null);
        Preconditions.checkState(this.getGraph().hasVertex(startingVertex));

        final int graphOrder = this.getGraph().getOrder();
        final List<T> oddVertices = Lists.newArrayList(this.getGraph().getOddVertices());
        final int oddVerticesOrder = oddVertices.size();

        System.out.println(oddVerticesOrder);

        ClosedCPP<Node<T>> bestSolverInstance = null;
        Double bestExtraCost = 0.0;

        ClosedCPP<Node<T>> cppSolver;
        int i = 0;

        do {

            final Builder<Node<T>> builder = new UndirectedGraph.Builder<Node<T>>(graphOrder + 1);
            for (final T vertex : this.getGraph()) {
                final List<WeightedEdge<T>> edges = this.getGraph().getEdges(vertex);
                for (final WeightedEdge<T> weightedEdge : edges) {
                    final WeightedEdge<Node<T>> edge = WeightedEdge.from(
                            new Node<T>(weightedEdge.getEndPoint1()),
                            new Node<T>(weightedEdge.getEndPoint2()),
                            weightedEdge.getWeight()
                            );
                    if (!builder.contains(edge)) builder.addEdge(edge);
                }
            }

            final WeightedEdge<Node<T>> virtualEdge1 = WeightedEdge.from(
                    new Node<T>(null),
                    new Node<T>(startingVertex),
                    this.getLowerBoundCost()
                    );
            if (!builder.contains(virtualEdge1))
                builder.addEdge(virtualEdge1);

            final WeightedEdge<Node<T>> virtualEdge2 = WeightedEdge.from(
                    new Node<T>(this.getGraph().isEulerian() ? startingVertex : oddVertices.get(i)),
                    new Node<T>(null),
                    this.getLowerBoundCost()
                    );
            if (!builder.contains(virtualEdge2))
                builder.addEdge(virtualEdge2);

            final UndirectedGraph<Node<T>> newGraph = builder.build(); // TODO optimisation possible

            cppSolver = ClosedCPP.from(newGraph);
            final Solution<Node<T>> solve = cppSolver.solve();

            final Double currentExtraCost = cppSolver.getExtraCost();
            if (bestSolverInstance == null || currentExtraCost < bestExtraCost) {
                bestSolverInstance = cppSolver;
                bestExtraCost = currentExtraCost;
            }
            System.out.println(i + " : " + solve.getLowerBoundCost() + " : " + solve.getUpperBoundCost());

            if (cppSolver.getUpperBoundCost().equals(650.01)) {

                System.out.println(new MazeView().render(this.maze, (Vertex) oddVertices.get(i)));

                final Solution<Node<T>> solution2 = cppSolver.solve();
                final List<Node<T>> openTrail = EulerianTrail.from(solution2.graph, solution2.getTraversalByEdge(), new Node<T>(startingVertex));

                for (final Node<T> node : Lists.reverse(openTrail)) {
                    final Vertex vertex = (Vertex) node.getData();
                    if (vertex == null) continue;
                    System.out.println(new MazeView().render(this.maze, vertex));
                    try {
                        Thread.sleep(300);
                    }
                    catch (final InterruptedException e) {}
                }
            }

        }
        while (++i < oddVerticesOrder);

        //final List<Integer> openTrail = bestSolverInstance.solveFrom(graphOrder);
        //final List<Integer> openTrail = bestSolverInstance.solveFrom(vertex);
        //final List<Integer> reverse = Lists.reverse(openTrail);

        /*
        final Map<WeightedEdge<Node<T>>, Integer> traversalByEdge = bestSolverInstance.getTraversalByEdge();
        final Map<WeightedEdge<T>, Integer> traversalByEdge2 = Maps.newHashMap(); // TODO
        for (final Entry<WeightedEdge<Node<T>>, Integer> entry : traversalByEdge.entrySet()) {
            final WeightedEdge<Node<T>> edge = entry.getKey();
            final T data1 = edge.getEndPoint1().getData();
            final T data2 = edge.getEndPoint2().getData();
            if (data1 == null || data2 == null) continue;
            traversalByEdge2.put(this.getGraph().getEdge(data1, data2), entry.getValue());
        }
        return new Solution<T>(traversalByEdge2, bestSolverInstance.getLowerBoundCost(), bestSolverInstance.getUpperBoundCost());
        */

        System.out.println(bestSolverInstance.solve());
        return bestSolverInstance.solve();

    }

    public void setMaze(final Maze maze) {
        this.maze = maze;
    }
}