
package tdd;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class UndirectedGraphBuilder {

    private final int numberOfVertices;
    private final Map<Integer, Integer> vertices = Maps.newTreeMap();
    private final Map<Integer, Integer> edges = Maps.newTreeMap();
    private final Set<Integer> edgeExternalIdSet = Sets.newTreeSet();
    private int vertexCounter = 0;
    private int edgeCounter = 0;
    private final double[][] values;

    public UndirectedGraphBuilder(final int numberOfVertices) {

        Preconditions.checkArgument(numberOfVertices >= 2);

        this.numberOfVertices = numberOfVertices;
        this.values = new double[numberOfVertices][numberOfVertices];
    }

    public int getNumberOfVertices() {
        return this.numberOfVertices;
    }

    public boolean contains(final int edgeExternalId) {
        return this.edgeExternalIdSet.contains(edgeExternalId);
    }

    public UndirectedGraphBuilder addEdge(final int edgeExternalId, final int uExternalId, final int vExternalId, final double cost) {

        Preconditions.checkArgument(edgeExternalId > 0);
        Preconditions.checkArgument(uExternalId > 0);
        Preconditions.checkArgument(vExternalId > 0);
        Preconditions.checkArgument(uExternalId != vExternalId);
        Preconditions.checkArgument(cost >= 0);

        Integer uLocalId = this.vertices.get(uExternalId);
        if (uLocalId == null) {
            Preconditions.checkState(this.vertexCounter != this.numberOfVertices, "Maximal number of vertices reached.");
            uLocalId = ++this.vertexCounter;
            this.vertices.put(uExternalId, uLocalId);
        }

        Integer vLocalId = this.vertices.get(vExternalId);
        if (vLocalId == null) {
            Preconditions.checkState(this.vertexCounter != this.numberOfVertices);
            vLocalId = ++this.vertexCounter;
            this.vertices.put(vExternalId, vLocalId);
        }

        Preconditions.checkState(!this.contains(edgeExternalId));
        Preconditions.checkState(this.values[uLocalId - 1][vLocalId - 1] == 0);

        this.edgeExternalIdSet.add(edgeExternalId);
        this.values[uLocalId - 1][vLocalId - 1] = cost;
        this.values[vLocalId - 1][uLocalId - 1] = cost;
        this.edges.put(++this.edgeCounter, edgeExternalId);

        return this;
    }

    public Graph build() {
        Preconditions.checkState(this.numberOfVertices == this.vertexCounter);
        return Graph.from(this.vertices, this.edges, this.values);
    }

    public static void main(final String[] args) {
        final UndirectedGraphBuilder builder = new UndirectedGraphBuilder(5);
        builder.addEdge(1, 1, 2, 10);
        builder.addEdge(2, 2, 3, 10);
        builder.addEdge(3, 1, 3, 10);
        builder.addEdge(4, 2, 4, 10);
        builder.addEdge(5, 4, 5, 10);
        final Graph graph = builder.build();
        System.out.println(graph.isConnected());
    }
}