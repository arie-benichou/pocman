
package tdd;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;

import fr.ut7.dojo.pacman.graph.Vertex;
import fr.ut7.dojo.pacman.model.Board;

public class GameGraph {

    private final Board board;

    public Board getBoard() {
        return this.board;
    }

    private final Map<Integer, Vertex> vertices;

    private final Map<Integer, List<Edge>> edgesByVertexId;

    public List<Edge> getEdgesByVertexId(final Integer vertexId) {
        return this.edgesByVertexId.get(vertexId);
    }

    private final Map<Integer, Integer> vertexIdByVertexIndex;

    public Integer getVertexIdByVertexIndex(final Integer vertexIndex) {
        return this.vertexIdByVertexIndex.get(vertexIndex);
    }

    private final Map<Integer, Integer> vertexIndexByVertexId;

    public Integer getVertexIndexByVertexId(final Integer vertexId) {
        return this.vertexIndexByVertexId.get(vertexId);
    }

    private final int numberOfVertices;

    public int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    private final ShortestPaths shortestPaths;

    public Path getShortestPath(final Integer vertexIndex1, final Integer vertexIndex2) {
        return this.shortestPaths.getShortestPaths()[vertexIndex1][vertexIndex2];
    }

    private Path[][] buildPathMatrix() {
        final int n = this.getNumberOfVertices();
        final Path[][] paths = new Path[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                Path path;
                if (i == j) path = Path.from(0);
                else {
                    path = Path.from(Double.POSITIVE_INFINITY);
                    // TODO ! retrouver l'edge à partir du hashcode
                    for (final Edge edge : this.getEdgesByVertexId(this.getVertexIdByVertexIndex(i))) {
                        final Integer lastNode = this.getVertexIdByVertexIndex(j);
                        if (edge.getLastNode() == lastNode) {
                            path = Path.from(edge);
                            break;
                        }
                    }
                }
                paths[i][j] = path;
            }
        }
        return paths;
    }

    public GameGraph(final Board board, final Map<Integer, Vertex> vertices, final Map<Integer, List<Edge>> edgesByVertexId) {
        this.board = board;
        this.vertices = vertices;
        this.edgesByVertexId = edgesByVertexId;
        final Builder<Integer, Integer> vertexIdByVertexIndexBuilder = new ImmutableSortedMap.Builder<Integer, Integer>(Ordering.natural());
        final Builder<Integer, Integer> vertexIndexByVertexIdBuilder = new ImmutableSortedMap.Builder<Integer, Integer>(Ordering.natural());
        int index = 0;
        for (final Integer vertexId : this.edgesByVertexId.keySet()) {
            vertexIndexByVertexIdBuilder.put(vertexId, index);
            vertexIdByVertexIndexBuilder.put(index, vertexId);
            ++index;
        }
        this.vertexIdByVertexIndex = vertexIdByVertexIndexBuilder.build();
        this.vertexIndexByVertexId = vertexIndexByVertexIdBuilder.build();
        this.numberOfVertices = this.vertexIdByVertexIndex.size();
        this.shortestPaths = new ShortestPaths(this.buildPathMatrix());
    }

}