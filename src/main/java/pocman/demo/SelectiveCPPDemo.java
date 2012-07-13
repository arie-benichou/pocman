
package pocman.demo;

import graph.Feature;
import graph.UndirectedGraph;
import graph.WeightedEdge;
import graph.features.degree.DegreeInterface;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import pocman.game.Maze;
import pocman.game.MazeNode;
import pocman.game.Move;
import pocman.game.Tile;
import pocman.view.MazeAsBoardView;
import pocman.view.MazeView;

import com.google.common.base.Function;
import com.google.common.base.Stopwatch;

import cpp.EulerianTrail;
import cpp.OpenCPP;
import cpp.OpenCPPSolution;

// TODO MazeNode != GraphNode ( + between nodes)
// TODO MazePath
public class SelectiveCPPDemo {

    public static void main(final String[] args) throws InterruptedException {

        Maze maze;
        maze = Maze.from("" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃                         ┃" +
                "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
                "┃          ┃           ∙┃∙┃" +
                "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
                "┃          ┃ ┃            ┃" +
                "┃⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛ ┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
                "┃     ┃      ┃∙           ┃" +
                "┃⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃          ┃              ┃" +
                "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃                         ┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃                         ┃" +
                "┃ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
                "┃     ∙    ┃⬤∙            ┃" +
                "┃ ⬛⬛⬛⬛⬛⬛⬛⬛ ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛ ┃" +
                "┃                         ┃" +
                "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃");

        final MazeView view = new MazeView();
        final Maze originalMaze = maze;
        System.out.println(view.render(originalMaze));

        final Function<Maze, Maze> function1 = new Function<Maze, Maze>() {

            @Override
            public Maze apply(final Maze maze) {
                return apply1(maze);
            }
        };

        final Function<Maze, Maze> function2 = new Function<Maze, Maze>() {

            @Override
            public Maze apply(final Maze maze) {
                return apply2(SelectiveCPPDemo.apply(maze, function1));
            }

        };

        final Stopwatch stopwatch = new Stopwatch();

        stopwatch.start();
        maze = apply(maze, function2);
        stopwatch.stop();

        System.out.println(view.render(maze));

        final OpenCPP<MazeNode> openCPP = OpenCPP.from(maze);
        final OpenCPPSolution<MazeNode> solution = openCPP.solveFrom(maze.getNearestGraphNode(maze.find(Tile.COIN)));
        final List<MazeNode> trail = EulerianTrail.from(maze, solution.getTraversalByEdge(), solution.getEndPoint());

        debug(originalMaze, trail, 160);

        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());

    }

    private static Maze apply(final Maze maze, final Function<Maze, Maze> function) {
        Maze previousMaze;
        Maze reducedMaze = maze;
        do {
            previousMaze = reducedMaze;
            reducedMaze = function.apply(reducedMaze);
        }
        while (!previousMaze.getBoard().equals(reducedMaze.getBoard()));
        return reducedMaze;
    }

    private static Maze apply0(Maze maze) {
        Maze previousMaze;
        do {
            previousMaze = maze;
            maze = apply1(maze);
        }
        while (!previousMaze.getBoard().equals(maze.getBoard()));
        return maze;
    }

    private static Maze apply1(final Maze maze) {
        final UndirectedGraph<MazeNode> graph = maze.get();
        final DegreeInterface<MazeNode> degreeFeature = graph.getFeature(Feature.DEGREE);
        final Map<MazeNode, Integer> nodesWithDegree = degreeFeature.getNodesHavingDegree(1);
        final char[] board = maze.getBoard().toCharArray();
        for (final Entry<MazeNode, Integer> entry : nodesWithDegree.entrySet()) {
            final MazeNode endPoint = entry.getKey();
            final WeightedEdge<MazeNode> edge = maze.get().getEdgesFrom(endPoint).iterator().next();
            final int dist = Math.abs(edge.getEndPoint2().getId() - edge.getEndPoint1().getId());
            final Move move = endPoint.getOptions().iterator().next();
            final int from = endPoint.getId();
            int n = 0;
            boolean hasCollectable = false;
            int k = 0;
            while (Math.abs(n) != dist && !hasCollectable) {
                hasCollectable = maze.getBoard().getCell(from + n).isCollectable();
                n += move.getDelta();
                ++k;
            }
            if (!hasCollectable) {
                while (k > 0) {
                    --k;
                    board[from + k * move.getDelta()] = Tile.HORIZONTAL_WALL.toCharacter();
                }
            }
        }
        return Maze.from(board);
    }

    private static Maze apply2(final Maze maze) {

        final char[] board = maze.getBoard().toCharArray();

        final UndirectedGraph<MazeNode> graph = maze.get();
        final DegreeInterface<MazeNode> degreeFeature = graph.getFeature(Feature.DEGREE);

        // TODO ? sélectionner uniquement les corners
        final Map<MazeNode, Integer> nodesNotHavingDegreeOf1 = degreeFeature.getNodesNotHavingDegree(1);

        Maze bestReducedMaze = maze;

        for (final Entry<MazeNode, Integer> entry : nodesNotHavingDegreeOf1.entrySet()) {
            final char tileChar = board[entry.getKey().getId()];
            board[entry.getKey().getId()] = Tile.HORIZONTAL_WALL.toCharacter();
            final Maze nextMaze = Maze.from(board);
            final boolean hasIsland = nextMaze.hasIsland();
            board[entry.getKey().getId()] = tileChar;
            if (!hasIsland) {
                //System.out.println(entry);
                final Maze reducedMaze = apply0(nextMaze);
                //final MazeView view = new MazeView();
                if (reducedMaze.getNumberOfMazeNodes() < bestReducedMaze.getNumberOfMazeNodes()) {
                    //System.out.println(view.render(reducedMaze));
                    //System.out.println(reducedMaze.getNumberOfMazeNodes());
                    bestReducedMaze = reducedMaze;
                }
            }
        }

        return bestReducedMaze;
    }

    // TODO refactoring
    private static Move findMove(final MazeNode endPoint1, final MazeNode endPoint2) {
        final int kDelta = endPoint2.getId() - endPoint1.getId();
        if (Math.abs(kDelta) % Move.GO_UP.getDelta() == 0) return kDelta < 0 ? Move.GO_UP : Move.GO_DOWN;
        return kDelta < 0 ? Move.GO_LEFT : Move.GO_RIGHT;
    }

    // TODO refactoring
    private static void debug(final Maze maze, final List<MazeNode> trail, final long laps) throws InterruptedException {
        final char[] board = maze.getBoard().toCharArray();
        final MazeAsBoardView view = new MazeAsBoardView();
        MazeNode parentNode = trail.get(0);
        board[parentNode.getId()] = Tile.POCMAN.toCharacter();
        System.out.println(view.render(board));
        final int n = trail.size();
        for (int i = 1; i < n; ++i) {
            final int childNodeId = trail.get(i).getId();
            final MazeNode childNode = maze.getNode(childNodeId);
            final Move move = findMove(parentNode, childNode);
            MazeNode node = parentNode;
            while (!node.equals(childNode)) {
                node = maze.getNode(node.getId() + move.getDelta());
                board[node.getId() + move.getOpposite().getDelta()] = move.toString().charAt(0);
                board[node.getId()] = Tile.POCMAN.toCharacter();
                System.out.println(view.render(board));
                Thread.sleep(laps);
            }
            parentNode = childNode;
        }
    }

}