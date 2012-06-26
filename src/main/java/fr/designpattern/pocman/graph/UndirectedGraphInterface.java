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

package fr.designpattern.pocman.graph;

import java.util.List;
import java.util.Set;

public interface UndirectedGraphInterface<T> extends Iterable<T> { // TODO à revoir

    /*
    boolean addVertex(final T vertex);

    void addEdge(final T one, final T two);

    void removeEdge(final T one, final T two);
    */

    boolean edgeExists(final T one, final T two);

    Set<T> getConnectedVerticeSet(final T vertex);

    List<WeightedEdge<T>> getEdges(final T vertex);

    boolean isEmpty();

}