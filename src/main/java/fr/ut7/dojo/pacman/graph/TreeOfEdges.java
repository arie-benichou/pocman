
package fr.ut7.dojo.pacman.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.ut7.dojo.pacman.model.Board;
import fr.ut7.dojo.pacman.model.Constants;

public class TreeOfEdges {

    //private final Map<Integer, PathNode> map;
    private final Map<Integer, List<GraphEdge>> edgesById;
    private final Board board;
    private final HashMap<Integer, PathNode> mapCopy;
    private int id;

    public TreeOfEdges(final Map<Integer, PathNode> map, final Map<Integer, List<GraphEdge>> edgesById, final Board board) {
        //this.map = map;
        this.mapCopy = Maps.newHashMap(map);
        this.edgesById = edgesById;
        this.board = board;
    }

    private GraphEdge findEdge(final int parent, final PathNode child) {
        final List<GraphEdge> edges = this.edgesById.get(parent);
        for (final GraphEdge edge : edges)
            if (edge.getLastNode().getId() == child.getId()) return edge;
        return null;
    }

    public HashMap<Integer, PathNode> postorder(final int id) {
        this.id = id;

        //System.out.println(this.mapCopy);

        System.out.println(this.mapCopy.size());
        this.postorder(this.mapCopy.get(id));

        System.out.println();
        System.out.println(this.mapCopy.size());

        System.out.println(this.mapCopy);
        return this.mapCopy;
    }

    public void postorder(final PathNode node)
    {
        final List<PathNode> children = Lists.transform(node.getChildren(), new Function<Integer, PathNode>() {

            public PathNode apply(final Integer input) {
                return TreeOfEdges.this.mapCopy.get(input);
            }

        });

        for (final PathNode child : children) {
            this.postorder(child);
        }

        //if (node.hasChild()) return;
        //if (this.mapCopy.get(node.getId()).hasChild()) return;

        final GraphEdge edge = this.findEdge(node.getParent(), node);

        final int c = this.countPills(edge);

        // TODO compter le nombre de pilules contenues dans une edge lors de la création du graphe
        // 
        if (c == 0) {

            /*
            System.out.println();
            System.out.println(node);
            final char[] array = this.board.toCharArray();
            array[node.getId()] = Constants.PACMAN;
            System.out.println(new BoardView().render(Board.from(array)));
            */

            final PathNode parent = this.mapCopy.get(node.getParent());
            final List<Integer> childrenOfParent = Lists.newArrayList(parent.getChildren());
            //System.out.println(childrenOfParent);
            childrenOfParent.remove((Integer) node.getId());
            //System.out.println(childrenOfParent);
            final PathNode newParent = new PathNode(parent.getParent(), parent.getId(), childrenOfParent, parent.getNumberOfDescendants() - 1);
            if (!this.mapCopy.get(node.getId()).hasChild()) {
                //if (!node.hasChild()) {
                this.mapCopy.remove(node.getId());
                this.mapCopy.put(newParent.getId(), newParent);

                int parentNodeId = newParent.getParent();
                while (parentNodeId != this.id) {
                    final PathNode parentNode = this.mapCopy.get(parentNodeId);
                    final PathNode newParentNode =
                                                   new PathNode(parentNode.getParent(), parentNode.getId(), parentNode.getChildren(),
                                                           parentNode.getNumberOfDescendants() - 1);
                    this.mapCopy.put(newParentNode.getId(), newParentNode);

                    /*
                    System.out.println("########################");
                    System.out.println(node);
                    final List<GraphEdge> edges = this.edgesById.get(node.getId());
                    for (final GraphEdge graphEdge : edges) {
                        System.out.println(graphEdge);
                    }
                    System.out.println("########################");
                    */

                    parentNodeId = newParentNode.getParent();
                }

            }
            /*
            else {
                //final List<GraphEdge> edges = this.edgesById.get(node);
                System.out.println("c == 0 but node has child");
                System.out.println(edge);
            }
            */
        }

    }

    private int countPills(final GraphEdge edge) {
        int c = 1;
        if (edge != null) {
            if (this.board.getCell(edge.getLastNode().getId()) != Constants.PILL) {
                --c;
                final List<GraphNode> betweenNodes = edge.getBetweenNodes();
                for (final GraphNode graphNode : betweenNodes) {
                    if (this.board.getCell(graphNode.getId()) == Constants.PILL) {
                        ++c;
                    }
                }
            }
        }
        return c;
    }

}