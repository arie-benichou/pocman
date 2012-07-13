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

package graph.features.routing;

import graph.Feature;
import graph.Path;
import graph.UndirectedGraph;
import pocman.demo.Mazes;
import pocman.game.Maze;
import pocman.game.MazeNode;

import com.google.common.base.Strings;

public final class Routing2<T> implements RoutingInterface<T> {

    private final UndirectedGraph<T> graph;

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    public static <T> Routing2<T> from(final UndirectedGraph<T> graph) {
        return new Routing2<T>(graph);
    }

    private Routing2(final UndirectedGraph<T> graph) {
        this.graph = graph;
    }

    private volatile Path<T>[][] data = null;

    private Path<T>[][] getData() {

        Path<T>[][] value = this.data;

        if (value == null) {

            synchronized (this) {

                if ((value = this.data) == null) {

                    final int n = this.getGraph().getOrder();

                    @SuppressWarnings("unchecked")
                    final Path<T>[][] data = new Path[n][n];

                    for (final T endPoint1 : this.getGraph()) {
                        final int i = this.getGraph().getOrdinal(endPoint1);
                        for (final T endPoint2 : this.getGraph()) {
                            final int j = this.getGraph().getOrdinal(endPoint2);
                            if (i != j && this.getGraph().hasEdge(endPoint1, endPoint2))
                                data[i][j] = Path.from(this.getGraph().getEdge(endPoint1, endPoint2));
                        }
                    }

                    //this.debug(data);

                    /**
                     * Roy-Warshall-Floyd algorithm
                     */
                    for (int k = 0; k < n; ++k) {
                        for (int i = 0; i < n; ++i) {
                            for (int j = 0; j < n; ++j) {
                                if (i == j) continue;
                                final Path<T> path = data[i][j];
                                final double currentCost = path == null ? Double.POSITIVE_INFINITY : path.getWeight(); // TODO ? compute upper-bound
                                final Path<T> path1 = data[i][k];
                                if (path1 == null) continue;
                                final Path<T> path2 = data[k][j];
                                if (path2 == null) continue;
                                if (path1.getWeight() + path2.getWeight() < currentCost) data[i][j] = path1.add(path2);
                            }
                        }
                    }

                    //this.debug(data);

                    this.data = value = data;
                }

            }

        }

        return value;
    }

    @Override
    public Path<T> getShortestPath(final T endPoint1, final T endPoint2) {
        return this.getData()[this.getGraph().getOrdinal(endPoint1)][this.getGraph().getOrdinal(endPoint2)];
    }

    public void debug(final Path<T>[][] array) {

        double max = 0;
        final int order = this.getGraph().getOrder();

        for (int i = 0; i < order; ++i)
            for (int j = 0; j < order; ++j)
                if (i != j && array[i][j] != null && array[i][j].getWeight() > max) max = array[i][j].getWeight();

        final int n = (int) Math.floor(Math.log10(Double.valueOf(max).intValue())) + 1;
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < order; ++j) {
                String string;
                if (i == j) string = Strings.repeat(".", n);
                else if (array[i][j] == null) string = Strings.repeat("X", n);
                else string = Strings.padStart(String.valueOf((int) array[i][j].getWeight()), n, '0');
                System.out.print(string + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(final String[] args) {

        final Maze maze = Maze.from(Mazes.LEVELS[0]);
        final UndirectedGraph<MazeNode> graph = maze.get();
        final Routing2<MazeNode> feature = graph.getFeature(Feature.ROUTING);
        feature.debug(feature.getData());
    }
}