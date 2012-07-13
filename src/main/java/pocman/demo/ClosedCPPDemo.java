/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package pocman.demo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import matching.MatchingAlgorithm;
import pocman.game.Maze;
import pocman.game.MazeNode;
import pocman.game.Move;
import pocman.game.Tile;
import pocman.view.MazeAsBoardView;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import cpp.ClosedCPP;
import cpp.ClosedCPPSolution;
import cpp.EulerianTrail;
import cpp.OpenCPP;
import cpp.OpenCPPSolution;

public class ClosedCPPDemo {

    public final static MatchingAlgorithm MATCHING_ALGORITHM_1 = new matching.edmonds1.Matching();
    public final static MatchingAlgorithm MATCHING_ALGORITHM_2 = new matching.edmonds2.Matching();
    public final static MatchingAlgorithm MATCHING_ALGORITHM_3 = new matching.naive.Matching();

    private final MatchingAlgorithm matchingAlgorithm;

    public ClosedCPPDemo(final MatchingAlgorithm matchingAlgorithm) {
        this.matchingAlgorithm = matchingAlgorithm;
    }

    public OpenCPPSolution<MazeNode> solve(final ClosedCPPSolution<MazeNode> closedCPPSolution, final MazeNode mazeNode) {
        //return OpenCPP.from(maze, this.matchingAlgorithm).solveFrom(maze.getNode(pocManPosition));
        final OpenCPP<MazeNode> openCPP = OpenCPP.from(closedCPPSolution);
        final OpenCPPSolution<MazeNode> openCPPSolution = openCPP.solveFrom(mazeNode);
        return openCPPSolution;
    }

    public ClosedCPPSolution<MazeNode> solve(final Maze maze) {
        final ClosedCPP<MazeNode> closedCPP = ClosedCPP.from(maze, this.matchingAlgorithm);
        final ClosedCPPSolution<MazeNode> closedCPPSolution = closedCPP.solve();
        //final double upperBoundCost = closedCPPSolution.getUpperBoundCost();
        return closedCPPSolution;
    }

    private Move findMove(final MazeNode endPoint1, final MazeNode endPoint2) {
        final int kDelta = endPoint2.getId() - endPoint1.getId();
        if (Math.abs(kDelta) % Move.GO_UP.getDelta() == 0) return kDelta < 0 ? Move.GO_UP : Move.GO_DOWN;
        return kDelta < 0 ? Move.GO_LEFT : Move.GO_RIGHT;
    }

    private void debug(final Maze maze, final List<MazeNode> trail, final long laps) throws InterruptedException {

        final char[] board = maze.getBoard().toCharArray();
        final MazeAsBoardView view = new MazeAsBoardView();

        MazeNode parentNode = trail.get(0);
        board[parentNode.getId()] = Tile.POCMAN.toCharacter();

        System.out.println(Move.GO_NOWHERE);
        System.out.println(view.render(board));

        final int n = trail.size();
        for (int i = 1; i < n; ++i) {
            final MazeNode childNode = trail.get(i);
            final Move move = this.findMove(parentNode, childNode);
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

    public static void main(final String[] args) throws InterruptedException {

        final Stopwatch stopwatch = new Stopwatch();

        final ClosedCPPDemo that = new ClosedCPPDemo(MATCHING_ALGORITHM_1);

        for (final String level : Mazes.LEVELS) {
            final int pocManPosition = level.indexOf(Tile.POCMAN.toCharacter());
            Preconditions.checkState(pocManPosition > -1, "POCMAN POSITION NOT FOUND !");
            final char[] data = level.toCharArray();
            data[pocManPosition] = Tile.COIN.toCharacter();

            final Maze maze = Maze.from(data);

            stopwatch.start();
            final ClosedCPPSolution<MazeNode> closedCPPSolution = that.solve(maze);
            stopwatch.stop();

            final List<MazeNode> closedTrail = EulerianTrail.from(
                    closedCPPSolution.getGraph(),
                    closedCPPSolution.getTraversalByEdge(),
                    maze.getNode(pocManPosition));

            that.debug(maze, closedTrail, 160);
            System.out.println(closedCPPSolution);
            Thread.sleep(1000);

            /*
            stopwatch.start();
            final OpenCPPSolution<MazeNode> openCPPSolution = that.solve(closedCPPSolution, maze.getNode(pocManPosition));
            stopwatch.stop();

            final List<MazeNode> trail = EulerianTrail.from(
                    openCPPSolution.getGraph(),
                    openCPPSolution.getTraversalByEdge(),
                    openCPPSolution.getEndPoint());

            that.debug(maze, trail, 160);
            */

            //break;
        }

        /*
        stopwatch.start();
        final OpenCPPSolution<MazeNode> openCPPSolution = that.solve(closedCPPSolution, maze.getNode(pocManPosition));
        stopwatch.stop();

        final List<MazeNode> trail = EulerianTrail.from(
                openCPPSolution.getGraph(),
                openCPPSolution.getTraversalByEdge(),
                openCPPSolution.getEndPoint());

        that.debug(maze, trail, 320);
        System.out.println(openCPPSolution);
        Thread.sleep(1000);
        */

        //}

        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());
    }
}