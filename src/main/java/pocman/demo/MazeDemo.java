
package pocman.demo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pocman.game.Move;
import pocman.graph.Path;
import pocman.graph.WeightedEdge;
import pocman.maze.Maze;
import pocman.maze.MazeNode;
import pocman.maze.Tile;
import pocman.view.MazeAsBoardView;

import com.google.common.collect.Lists;

public class MazeDemo {

    public static void main(final String[] args) throws InterruptedException {

        final Maze maze = Maze.from("" +
                "┃      ┃           ┃      ┃" +
                "┃ ⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛ ┃" +
                "┃                         ┃" +
                "┃⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛┃" +
                "┃    ┃       ┃       ┃    ┃" +
                "┃⬛⬛┃ ┃⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛┃ ┃⬛⬛┃" +
                "┃⬛⬛┃                   ┃⬛⬛┃" +
                "┃⬛⬛┃ ⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛┃ ┃⬛⬛┃" +
                "┃    ┃    ⬛⬛⬛⬛⬛⬛⬛    ┃    ┃" +
                "┃⬛⬛┃    ┃         ┃    ┃⬛⬛┃" +
                "┃⬛⬛┃ ⬛⬛⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛⬛⬛ ┃⬛⬛┃" +
                "┃⬛⬛┃         ┃         ┃⬛⬛┃" +
                "┃⬛⬛┃ ⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛ ┃⬛⬛┃" +
                "┃   ∙                     ┃" +
                "┃ ┃⬛⬛┃ ⬛⬛⬛⬛⬛ ┃ ⬛⬛⬛⬛⬛ ┃⬛⬛┃ ┃" +
                "┃ ┃⬛⬛┃ ┃     ┃     ┃ ┃⬛⬛┃ ┃" +
                "┃ ┃⬛⬛┃ ┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃ ┃⬛⬛┃ ┃" +
                "┃                        ∙┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃");

        final MazeNode firstCoinNode = maze.find(Tile.COIN);
        //System.out.println(firstCoinNode);

        final MazeNode nextCoinNode = maze.find(Tile.COIN, firstCoinNode);
        //System.out.println(nextCoinNode);

        final Map<MazeNode, Entry<Move, Integer>> graphNodeRange = maze.getGraphNodeRange(nextCoinNode);

        System.out.println(graphNodeRange);

        //System.exit(0);

        final MazeNode node1 = maze.getNearestGraphNode(firstCoinNode);
        //System.out.println(node1);

        //final MazeNode node2 = maze.getNearestGraphNode(nextCoinNode);
        //System.out.println(node2);

        Path<MazeNode> shortestPath = null;
        for (final Entry<MazeNode, Entry<Move, Integer>> entry : graphNodeRange.entrySet()) {
            shortestPath = maze.getShortestPath(node1, entry.getKey());
            System.out.println(shortestPath);

            final List<WeightedEdge<MazeNode>> edges = shortestPath.getEdges();
            final List<MazeNode> trail = Lists.newArrayList();
            trail.add(edges.get(0).getEndPoint1());
            final int n = edges.size() - 1;
            for (int i = 1; i <= n; ++i) {
                trail.add(edges.get(i).getEndPoint1());
                trail.add(edges.get(i).getEndPoint2());
            }
            trail.add(edges.get(n).getEndPoint2());
            //System.out.println(trail);

            debug(maze, trail, 160);

            final List<MazeNode> potential = Lists.newArrayList(trail);

            for (final MazeNode mazeNode : trail) {
                final Set<MazeNode> endPoints = maze.get().getEndPoints(mazeNode);
                //System.out.println(endPoints);
                potential.addAll(endPoints);
            }
            debug2(maze, potential);

        }

    }

    private static void debug2(final Maze maze, final List<MazeNode> nodes) {
        final char[] board = maze.getBoard().toCharArray();
        final MazeAsBoardView view = new MazeAsBoardView();
        for (final MazeNode node : nodes) {
            board[node.getId()] = Tile.POCMAN.toCharacter();
        }
        System.out.println(view.render(board));
    }

    private static Move findMove(final MazeNode endPoint1, final MazeNode endPoint2) {
        final int kDelta = endPoint2.getId() - endPoint1.getId();
        if (Math.abs(kDelta) % Move.GO_UP.getDelta() == 0) return kDelta < 0 ? Move.GO_UP : Move.GO_DOWN;
        return kDelta < 0 ? Move.GO_LEFT : Move.GO_RIGHT;
    }

    private static void debug(final Maze maze, final List<MazeNode> trail, final long laps) throws InterruptedException {
        final char[] board = maze.getBoard().toCharArray();
        final MazeAsBoardView view = new MazeAsBoardView();
        MazeNode parentNode = trail.get(0);
        board[parentNode.getId()] = Tile.POCMAN.toCharacter();
        System.out.println(view.render(board));
        final int n = trail.size();
        for (int i = 1; i < n; ++i) {
            final MazeNode childNode = trail.get(i);
            final Move move = findMove(parentNode, childNode);
            MazeNode node = parentNode;
            while (!node.equals(childNode)) {
                node = maze.getNode(node.getId() + move.getDelta());
                board[node.getId()] = Tile.POCMAN.toCharacter();
                //board[node.getId()] = (char) (96 + i);
                System.out.println(view.render(board));
                Thread.sleep(laps);
            }
            parentNode = childNode;
        }
    }

}