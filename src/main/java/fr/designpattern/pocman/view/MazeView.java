
package fr.designpattern.pocman.view;

import fr.designpattern.pocman.game.Maze;
import fr.designpattern.pocman.graph.Vertex;

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

    public String renderAsBoard(final Maze maze, final Vertex vertex) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsBoardView.render(maze, vertex));
        return sb.toString();
    }

    public String renderAsGraph(final Maze maze, final Vertex vertex) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mazeAsGraphView.render(maze, vertex));
        return sb.toString();
    }

}