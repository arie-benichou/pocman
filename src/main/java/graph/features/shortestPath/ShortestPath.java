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

package graph.features.shortestPath;

import graph.UndirectedGraph;

import com.google.common.base.Strings;

// TODO scinder en ShortestPathFeature et EulerianTrail
// TODO inclure cpp features dans le package routing
final class ShortestPath<T> implements ShortestPathInterface<T> {

    private final UndirectedGraph<T> graph;

    private volatile PathInterface<T>[][] data = null;

    public static <T> ShortestPath<T> from(final UndirectedGraph<T> graph) {
        return new ShortestPath<T>(graph);
    }

    private ShortestPath(final UndirectedGraph<T> graph) {
        this.graph = graph;
    }

    public UndirectedGraph<T> getGraph() {
        return this.graph;
    }

    @SuppressWarnings("unchecked")
    private PathInterface<T>[][] initializeData() {
        final int n = this.getGraph().getOrder();
        final PathInterface<T>[][] data = new PathInterface[n][n];
        for (int i = 0; i < n; ++i) {
            final T endPoint1 = this.getGraph().get(i);
            for (int j = 0; j < n; ++j) {
                final T endPoint2 = this.getGraph().get(j);
                if (i != j && this.getGraph().hasEdge(endPoint1, endPoint2))
                    data[i][j] = Path.from(this.getGraph().getEdge(endPoint1, endPoint2));
            }
        }
        //this.debug(data);
        return data;
    }

    /**
     * Roy-Warshall-Floyd algorithm
     * 
     * @return
     */
    private PathInterface<T>[][] computeData(final PathInterface<T>[][] data) {
        final int n = this.getGraph().getOrder();
        for (int k = 0; k < n; ++k) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (i == j) continue;
                    final PathInterface<T> path = data[i][j];
                    final double currentCost = path == null ? Double.POSITIVE_INFINITY : path.getWeight(); // TODO ? compute upper-bound
                    final PathInterface<T> path1 = data[i][k];
                    if (path1 == null) continue;
                    final PathInterface<T> path2 = data[k][j];
                    if (path2 == null) continue;
                    if (path1.getWeight() + path2.getWeight() < currentCost) data[i][j] = path1.add(path2);
                }
            }
        }
        //this.debug(data);
        return data;
    }

    private PathInterface<T>[][] getData() {
        PathInterface<T>[][] value = this.data;
        if (value == null) {
            synchronized (this) {
                if ((value = this.data) == null) this.data = value = this.computeData(this.initializeData());
            }
        }
        return value;
    }

    @Override
    public double[][] getShortestPathWeights() {
        final int n = this.getGraph().getOrder();
        final double[][] copy = new double[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (i != j) copy[i][j] = this.getData()[i][j].getWeight();
        return copy;
    }

    @Override
    public PathInterface<T> getShortestPath(final T endPoint1, final T endPoint2) {
        return this.getData()[this.getGraph().getOrdinal(endPoint1)][this.getGraph().getOrdinal(endPoint2)];
    }

    public void debug(final PathInterface<T>[][] array) {
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

}