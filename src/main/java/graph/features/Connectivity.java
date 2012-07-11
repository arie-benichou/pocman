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

package graph.features;

import graph.Feature;
import graph.UndirectedGraph;

import java.util.Map;

import pocman.demo.Mazes;
import pocman.game.Maze;
import pocman.game.MazeNode;

import com.google.common.collect.ImmutableMap;

public final class Connectivity<T> {

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    public static <T> Connectivity<T> from(final UndirectedGraph<T> graph) {
        return new Connectivity<T>(graph);
    }

    private Connectivity(final UndirectedGraph<T> graph) {
        this.graph = graph;
    }

    private volatile Map<T, Integer> ordinalByEndPoint = null;

    // TODO refactoring
    public Map<T, Integer> getOrdinalByEndPoint() {
        Map<T, Integer> value = this.ordinalByEndPoint;
        if (value == null) {
            synchronized (this) {
                if ((value = this.ordinalByEndPoint) == null) {
                    final ImmutableMap.Builder<T, Integer> builder = new ImmutableMap.Builder<T, Integer>();
                    int i = -1;
                    for (final T endPoint : this.getGraph())
                        builder.put(endPoint, ++i);
                    this.ordinalByEndPoint = value = builder.build();
                }
            }
        }
        return value;
    }

    private volatile Boolean data = null;

    private Boolean getData() {
        Boolean value = this.data;
        if (value == null) {
            synchronized (this) {
                if ((value = this.data) == null) {
                    final int n = this.getGraph().getOrder();
                    final boolean[][] matrix = new boolean[n][n];
                    int potentiallyNotConnected = 0;
                    for (final T endPoint1 : this.getGraph()) {
                        final int i = this.getOrdinalByEndPoint().get(endPoint1);
                        for (final T endPoint2 : this.getGraph()) {
                            final int j = this.getOrdinalByEndPoint().get(endPoint2);
                            if (i == j) matrix[i][j] = true;
                            if (this.getGraph().hasEdge(endPoint1, endPoint2)) matrix[i][j] = true;
                            else {
                                ++potentiallyNotConnected;
                                matrix[i][j] = false;
                            }
                        }
                    }
                    //this.debug(matrix);
                    int notConnected = potentiallyNotConnected;
                    if (notConnected != 0) {
                        for (int k = 0; k < n; ++k) {
                            for (int i = 0; i < n; ++i) {
                                for (int j = 0; j < n; ++j) {
                                    if (matrix[i][j]) continue;
                                    if (!matrix[i][k] || !matrix[k][j]) continue;
                                    matrix[i][j] = true;
                                    --notConnected;
                                    if (notConnected == 0) break;
                                }
                            }
                        }
                    }
                    //this.debug(matrix);
                    this.data = value = notConnected == 0;
                }
            }
        }
        return value;
    }

    public boolean isConnected() {
        return this.getData();
    }

    public void debug(final boolean[][] array) {
        final int n = this.getGraph().getOrder();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j)
                System.out.print((array[i][j] ? 1 : 0) + " ");
            System.out.println();
        }
        System.out.println();
    }

    public static void main(final String[] args) {
        final Maze maze = Maze.from(Mazes.LEVELS[0]);
        final UndirectedGraph<MazeNode> graph = maze.get();
        final Connectivity<MazeNode> feature = graph.getFeature(Feature.CONNECTIVITY);
        //feature.getData();
        System.out.println(feature.isConnected());
    }

}