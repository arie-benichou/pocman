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

package graph;

import java.util.Set;

public interface UndirectedGraphInterface<T> extends Iterable<T> {

    int getOrder();

    boolean hasEndPoint(final T endPoint);

    Integer getOrdinal(final T endPoint);

    T get(final int ordinal);

    boolean hasEdge(final T endPoint1, final T endPoint2);

    Set<T> getConnectedEndPoints(final T endPoint);

    Set<WeightedEdge<T>> getEdgesFrom(final T endPoint);

    Set<WeightedEdge<T>> getSetOfEdges();

    WeightedEdge<T> getEdge(final T endPoint1, final T endPoint2);

}