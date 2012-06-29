
package pocman.view;

import pocman.maze.Maze;
import pocman.maze.MazeNode;

public class MazeView {

    private final MazeAsBoardView mazeAsBoardView;
    private final MazeAsGraphView mazeAsGraphView;

    public MazeView() {
        this.mazeAsBoardView = new MazeAsBoardView();
        this.mazeAsGraphView = new MazeAsGraphView();
    }

    public String render(final Maze maze) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsBoardView.render(maze));
        sb.append("\n");
        sb.append(this.mazeAsGraphView.render(maze));
        return sb.toString();
    }

    public String renderAsBoard(final Maze maze, final int nodeId) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsBoardView.render(maze, nodeId));
        return sb.toString();
    }

    public String renderAsGraph(final Maze maze, final MazeNode MazeNode) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsGraphView.render(maze, MazeNode));
        return sb.toString();
    }

}