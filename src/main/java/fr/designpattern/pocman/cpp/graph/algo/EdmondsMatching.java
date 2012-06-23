
package fr.designpattern.pocman.cpp.graph.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import fr.designpattern.pocman.cpp.graph.MutableUndirectedGraph;

/**
 * Ripped from Keith Schwarz (htiek@cs.stanford.edu)
 * http://www.keithschwarz.com/interesting/code/?dir=edmonds-matching
 */
public final class EdmondsMatching {

    public static <T> MutableUndirectedGraph<T> maximumMatching(final MutableUndirectedGraph<T> g) {
        if (g.isEmpty())
            return new MutableUndirectedGraph<T>();
        final MutableUndirectedGraph<T> result = new MutableUndirectedGraph<T>();
        for (final T node : g)
            result.addVertex(node);
        while (true) {
            final List<T> path = findAlternatingPath(g, result);
            if (path == null) return result;
            updateMatching(path, result);
        }
    }

    private static <T> void updateMatching(final List<T> path, final MutableUndirectedGraph<T> m) {
        for (int i = 0; i < path.size() - 1; ++i) {
            if (m.edgeExists(path.get(i), path.get(i + 1))) m.removeEdge(path.get(i), path.get(i + 1));
            else m.addEdge(path.get(i), path.get(i + 1));
        }
    }

    private static final class NodeInformation<T> {

        public final T parent;
        public final T treeRoot;
        public final boolean isOuter;

        public NodeInformation(final T parent, final T treeRoot, final boolean isOuter) {
            this.parent = parent;
            this.treeRoot = treeRoot;
            this.isOuter = isOuter;
        }
    }

    private static final class Edge<T> {

        public final T start;
        public final T end;

        public Edge(final T start, final T end) {
            this.start = start;
            this.end = end;
        }
    }

    private static final class Blossom<T> {

        public final T root;
        public final List<T> cycle;
        public final Set<T> nodes;

        public Blossom(final T root, final List<T> cycle, final Set<T> nodes) {
            this.root = root;
            this.cycle = cycle;
            this.nodes = nodes;
        }
    }

    private static <T> List<T> findAlternatingPath(final MutableUndirectedGraph<T> g,
            final MutableUndirectedGraph<T> m) {
        final Map<T, NodeInformation<T>> forest = new HashMap<T, NodeInformation<T>>();
        final Queue<Edge<T>> worklist = new LinkedList<Edge<T>>();
        for (final T node : g) {
            if (!m.getConnectedVerticeSet(node).isEmpty())
                continue;
            forest.put(node, new NodeInformation<T>(null, node, true));
            for (final T endpoint : g.getConnectedVerticeSet(node))
                worklist.add(new Edge<T>(node, endpoint));
        }
        while (!worklist.isEmpty()) {
            final Edge<T> curr = worklist.remove();
            if (m.edgeExists(curr.start, curr.end))
                continue;
            final NodeInformation<T> startInfo = forest.get(curr.start);
            final NodeInformation<T> endInfo = forest.get(curr.end);
            if (endInfo != null) {
                if (endInfo.isOuter && startInfo.treeRoot.equals(endInfo.treeRoot)) {
                    final Blossom<T> blossom = findBlossom(forest, curr);
                    final List<T> path = findAlternatingPath(contractGraph(g, blossom),
                            contractGraph(m, blossom));
                    if (path == null) return path;
                    return expandPath(path, g, forest, blossom);
                }
                else if (endInfo.isOuter && startInfo.treeRoot != endInfo.treeRoot) {
                    List<T> result = new ArrayList<T>();
                    for (T node = curr.start; node != null; node = forest.get(node).parent)
                        result.add(node);
                    result = reversePath(result);
                    for (T node = curr.end; node != null; node = forest.get(node).parent)
                        result.add(node);
                    return result;
                }
            }
            else {
                forest.put(curr.end, new NodeInformation<T>(curr.start,
                        startInfo.treeRoot,
                        false));
                final T endpoint = m.getConnectedVerticeSet(curr.end).iterator().next();
                forest.put(endpoint, new NodeInformation<T>(curr.end,
                        startInfo.treeRoot,
                        true));
                for (final T fringeNode : g.getConnectedVerticeSet(endpoint))
                    worklist.add(new Edge<T>(endpoint, fringeNode));
            }
        }
        return null;
    }

    private static <T> Blossom<T> findBlossom(final Map<T, NodeInformation<T>> forest,
            final Edge<T> edge) {
        final LinkedList<T> onePath = new LinkedList<T>(), twoPath = new LinkedList<T>();
        for (T node = edge.start; node != null; node = forest.get(node).parent)
            onePath.addFirst(node);
        for (T node = edge.end; node != null; node = forest.get(node).parent)
            twoPath.addFirst(node);
        int mismatch = 0;
        for (; mismatch < onePath.size() && mismatch < twoPath.size(); ++mismatch)
            if (onePath.get(mismatch) != twoPath.get(mismatch))
                break;
        final List<T> cycle = new ArrayList<T>();
        for (int i = mismatch - 1; i < onePath.size(); ++i)
            cycle.add(onePath.get(i));
        for (int i = twoPath.size() - 1; i >= mismatch - 1; --i)
            cycle.add(twoPath.get(i));

        return new Blossom<T>(onePath.get(mismatch - 1), cycle, new HashSet<T>(cycle));
    }

    private static <T> MutableUndirectedGraph<T> contractGraph(final MutableUndirectedGraph<T> g,
            final Blossom<T> blossom) {
        final MutableUndirectedGraph<T> result = new MutableUndirectedGraph<T>();
        for (final T node : g) {
            if (!blossom.nodes.contains(node))
                result.addVertex(node);
        }
        result.addVertex(blossom.root);
        for (final T node : g) {
            if (blossom.nodes.contains(node)) continue;
            for (T endpoint : g.getConnectedVerticeSet(node)) {
                if (blossom.nodes.contains(endpoint))
                    endpoint = blossom.root;
                result.addEdge(node, endpoint);
            }
        }

        return result;
    }

    private static <T> List<T> expandPath(List<T> path,
            final MutableUndirectedGraph<T> g,
            final Map<T, NodeInformation<T>> forest,
            final Blossom<T> blossom) {
        final int index = path.indexOf(blossom.root);

        if (index == -1) return path;

        if (index % 2 == 1)
            path = reversePath(path);
        final List<T> result = new ArrayList<T>();
        for (int i = 0; i < path.size(); ++i) {
            if (path.get(i) != blossom.root) {
                result.add(path.get(i));
            }
            else {
                result.add(blossom.root);
                final T outNode = findNodeLeavingCycle(g, blossom, path.get(i + 1));

                final int outIndex = blossom.cycle.indexOf(outNode);
                final int start = outIndex % 2 == 0 ? 1 : blossom.cycle.size() - 2;
                final int step = outIndex % 2 == 0 ? +1 : -1;
                //for (int k = start; k != step; k += step)
                for (int k = start; k != outIndex + step; k += step)
                    result.add(blossom.cycle.get(k));
            }
        }
        return result;
    }

    private static <T> List<T> reversePath(final List<T> path) {
        final List<T> result = new ArrayList<T>();

        for (int i = path.size() - 1; i >= 0; --i)
            result.add(path.get(i));

        return result;
    }

    private static <T> T findNodeLeavingCycle(final MutableUndirectedGraph<T> g,
            final Blossom<T> blossom,
            final T node) {
        for (final T cycleNode : blossom.nodes)
            if (g.edgeExists(cycleNode, node))
                return cycleNode;
        throw new AssertionError("Could not find an edge out of the blossom.");
    }

    public static void main(final String[] args) {
        /*
        main1();
        System.out.println();
        System.out.println();
        main2();
        */

        main3();
    }

    public static void main1() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addVertex("G");
        graph.addVertex("H");
        graph.addVertex("I");
        graph.addVertex("J");

        graph.addEdge("A", "B");

        graph.addEdge("B", "C");
        graph.addEdge("B", "G");

        graph.addEdge("C", "D");
        graph.addEdge("C", "H");

        graph.addEdge("D", "E");
        graph.addEdge("D", "I");

        graph.addEdge("F", "G");
        graph.addEdge("G", "H");

        graph.addEdge("I", "H");
        graph.addEdge("I", "J");

        for (final String string : graph) {
            System.out.print(string + ": ");
            System.out.println(graph.getConnectedVerticeSet(string));
        }

        System.out.println();

        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);

        for (final String string : maximumMatching) {
            System.out.print(string + ": ");
            System.out.println(maximumMatching.getConnectedVerticeSet(string));
        }
    }

    public static void main2() {

        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();

        graph.addVertex("B");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("G");

        graph.addEdge("D", "B");
        graph.addEdge("D", "E");
        graph.addEdge("D", "G");

        for (final String string : graph) {
            System.out.print(string + ": ");
            System.out.println(graph.getConnectedVerticeSet(string));
        }

        System.out.println();

        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);

        for (final String string : maximumMatching) {
            System.out.print(string + ": ");
            System.out.println(maximumMatching.getConnectedVerticeSet(string));
        }

    }

    public static void main3() {
        final MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<String>();

        graph.addVertex("B");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("G");

        graph.addEdge("B", "D");
        graph.addEdge("B", "E");
        graph.addEdge("B", "G");

        graph.addEdge("D", "B");
        graph.addEdge("D", "E");
        graph.addEdge("D", "G");

        graph.addEdge("E", "B");
        graph.addEdge("E", "D");
        graph.addEdge("E", "G");

        graph.addEdge("G", "B");
        graph.addEdge("G", "D");
        graph.addEdge("G", "E");

        for (final String string : graph) {
            System.out.print(string + ": ");
            System.out.println(graph.getConnectedVerticeSet(string));
        }

        System.out.println();

        final MutableUndirectedGraph<String> maximumMatching = EdmondsMatching.maximumMatching(graph);

        for (final String string : maximumMatching) {
            System.out.print(string + ": ");
            System.out.println(maximumMatching.getConnectedVerticeSet(string));
        }
    }

}