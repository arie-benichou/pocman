
package tdd;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Ripped from Keith Schwarz (htiek@cs.stanford.edu)
 * http://www.keithschwarz.com/interesting/code/?dir=edmonds-matching
 */
public final class EdmondsMatching2 {

    private final static class NodeInformation<T> {

        private final T parent;
        private final T root;
        private final boolean isOuter;

        private NodeInformation(final T parent, final T root, final boolean isOuter) {
            this.parent = parent;
            this.root = root;
            this.isOuter = isOuter;
        }

        private NodeInformation(final T root, final boolean isOuter) {
            this(null, root, isOuter);
        }

    }

    private final static class Edge<T> {

        private final T start;
        private final T end;

        private Edge(final T start, final T end) {
            this.start = start;
            this.end = end;
        }
    }

    private final static class Blossom<T> {

        private final T root;
        private final List<T> cycle;
        private final Set<T> vertices;

        private Blossom(final T root, final List<T> cycle) {
            this.root = root;
            this.cycle = cycle;
            this.vertices = Sets.newHashSet(cycle);
        }
    }

    private static <T> int findMismatch(final List<T> onePath, final List<T> twoPath) {
        int mismatch = 0;
        for (; mismatch < onePath.size() && mismatch < twoPath.size(); ++mismatch)
            if (!onePath.get(mismatch).equals(twoPath.get(mismatch))) return mismatch;
        return mismatch;
    }

    private static <T> List<T> findCycle(final int mismatch, final List<T> onePath, final List<T> twoPath) {
        final List<T> cycle = Lists.newArrayList();
        for (int i = mismatch - 1; i < onePath.size(); ++i)
            cycle.add(onePath.get(i));
        for (int i = twoPath.size() - 1; i >= mismatch - 1; --i)
            cycle.add(twoPath.get(i));
        return cycle;
    }

    private static <T> Blossom<T> findBlossom(final Map<T, NodeInformation<T>> forest, final Edge<T> edge) {
        final LinkedList<T> onePath = Lists.newLinkedList();
        final LinkedList<T> twoPath = Lists.newLinkedList();
        for (T node = edge.start; node != null; node = forest.get(node).parent) {
            onePath.addFirst(node);
            twoPath.addFirst(node);
        }
        final int mismatch = findMismatch(onePath, twoPath);
        return new Blossom<T>(onePath.get(mismatch - 1), findCycle(mismatch, onePath, twoPath));
    }

    private static <T> UndirectedGraph<T> contractGraph(final UndirectedGraph<T> g, final Blossom<T> blossom) {
        final UndirectedGraph<T> result = new UndirectedGraph<T>();
        for (final T vertex : g)
            if (!blossom.vertices.contains(vertex)) result.addNode(vertex);
        result.addNode(blossom.root);
        for (final T vertex : g)
            if (!blossom.vertices.contains(vertex))
                for (final T endpoint : g.edgesFrom(vertex))
                    result.addEdge(vertex, blossom.vertices.contains(endpoint) ? blossom.root : endpoint);
        return result;
    }

    private static <T> T findNodeLeavingCycle(final UndirectedGraph<T> g, final Blossom<T> blossom, final T vertex) {
        for (final T cycleNode : blossom.vertices)
            if (g.edgeExists(cycleNode, vertex)) return cycleNode;
        throw new AssertionError("Could not find an edge out of the blossom.");
    }

    private static <T> List<T> expandPath(List<T> path, final UndirectedGraph<T> g, final Map<T, NodeInformation<T>> forest, final Blossom<T> blossom) {
        final int index = path.indexOf(blossom.root);
        if (index == -1) return path;
        if (index % 2 == 1) path = Lists.reverse(path);
        final List<T> result = Lists.newArrayList();
        for (int i = 0; i < path.size(); ++i) {
            if (path.get(i) != blossom.root) result.add(path.get(i));
            else {
                result.add(blossom.root);
                final T outNode = findNodeLeavingCycle(g, blossom, path.get(i + 1));
                final int outIndex = blossom.cycle.indexOf(outNode);
                final int start = outIndex % 2 == 0 ? 1 : blossom.cycle.size() - 2;
                final int step = outIndex % 2 == 0 ? +1 : -1;
                for (int k = start; k != step; k += step)
                    result.add(blossom.cycle.get(k));
            }
        }
        return result;
    }

    // TODO rename properly
    private static <T> void endInfoIsNull(final UndirectedGraph<T> originalGraph, final UndirectedGraph<T> resultingGraph,
            final Map<T, NodeInformation<T>> forest, final Queue<Edge<T>> worklist, final Edge<T> current, final NodeInformation<T> startInfo) {
        forest.put(current.end, new NodeInformation<T>(current.start, startInfo.root, false));
        final T endpoint = resultingGraph.edgesFrom(current.end).iterator().next();
        forest.put(endpoint, new NodeInformation<T>(current.end, startInfo.root, true));
        for (final T fringeNode : originalGraph.edgesFrom(endpoint))
            worklist.add(new Edge<T>(endpoint, fringeNode));
    }

    // TODO rename properly
    private static <T> List<T> endInfoIsOuterAndRootMatchs(final UndirectedGraph<T> originalGraph, final UndirectedGraph<T> resultingGraph,
            final Map<T, NodeInformation<T>> forest, final Edge<T> current) {
        final Blossom<T> blossom = findBlossom(forest, current);
        final List<T> path = findAlternatingPath(contractGraph(originalGraph, blossom), contractGraph(resultingGraph, blossom));
        return path == null ? path : expandPath(path, originalGraph, forest, blossom);
    }

    // TODO rename properly
    private static <T> List<T> endInfoIsOuterAndRootMismatchs(final Map<T, NodeInformation<T>> forest, final Edge<T> current) {
        List<T> result = Lists.newArrayList();
        for (T node = current.start; node != null; node = forest.get(node).parent)
            result.add(node);
        result = Lists.reverse(result);
        for (T node = current.end; node != null; node = forest.get(node).parent)
            result.add(node);
        return result;
    }

    private static <T> List<T> findAlternatingPath(final UndirectedGraph<T> originalGraph, final UndirectedGraph<T> resultingGraph) {
        final Map<T, NodeInformation<T>> forest = Maps.newHashMap();
        final Queue<Edge<T>> worklist = Lists.newLinkedList();
        for (final T node : originalGraph) {
            if (resultingGraph.edgesFrom(node).isEmpty()) {
                forest.put(node, new NodeInformation<T>(node, true));
                for (final T endpoint : originalGraph.edgesFrom(node))
                    worklist.add(new Edge<T>(node, endpoint));
            }
        }
        while (!worklist.isEmpty()) {
            final Edge<T> current = worklist.remove();
            if (!resultingGraph.edgeExists(current.start, current.end)) {
                final NodeInformation<T> startInfo = forest.get(current.start);
                final NodeInformation<T> endInfo = forest.get(current.end);
                if (endInfo == null) endInfoIsNull(originalGraph, resultingGraph, forest, worklist, current, startInfo);
                else if (endInfo.isOuter) {
                    if (startInfo.root.equals(endInfo.root)) return endInfoIsOuterAndRootMatchs(originalGraph, resultingGraph, forest, current);
                    return endInfoIsOuterAndRootMismatchs(forest, current);
                }
            }
        }
        return null;
    }

    private static <T> boolean updateMatching(final List<T> path, final UndirectedGraph<T> resultingGraph) {
        if (path == null) return false;
        final int n = path.size() - 1;
        for (int i = 0; i < n; ++i) {
            final T vertex1 = path.get(i);
            final T vertex2 = path.get(i + 1);
            if (resultingGraph.edgeExists(vertex1, vertex2)) resultingGraph.removeEdge(vertex1, vertex2);
            else resultingGraph.addEdge(vertex1, vertex2);
        }
        return true;
    }

    public static <T> UndirectedGraph<T> maximumMatching(final UndirectedGraph<T> originalGraph) {
        final UndirectedGraph<T> resultingGraph = new UndirectedGraph<T>();
        if (!originalGraph.isEmpty()) {
            for (final T node : originalGraph)
                resultingGraph.addNode(node);
            while (updateMatching(findAlternatingPath(originalGraph, resultingGraph), resultingGraph)) {}
        }
        return resultingGraph;
    }

}