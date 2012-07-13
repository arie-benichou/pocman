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

package matching;

import graph.UndirectedGraph;

// TODO ! make it a graph feature
public interface MatchingAlgorithmInterface {

    <T> Matches<T> from(final UndirectedGraph<T> graph);

    //TODO !! à faire
    //<T> Matches<T> from(final UndirectedGraph<T> graph, map edgeInstance);

    // TODO !! à virer
    <T> void setOriginalGraph(final UndirectedGraph<T> graph);

}