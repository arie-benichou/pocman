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

package graph.features.cpp;

import graph.UndirectedGraph;
import graph.features.FeatureInterface;

public final class ClosedCPPFeature implements FeatureInterface {

    private final UndirectedGraph<?> graph;

    private ClosedCPPInterface<?> instance = null;

    public ClosedCPPFeature(final UndirectedGraph<?> graph) {
        this.graph = graph;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ClosedCPPInterface<T> up() {
        if (this.instance == null) this.instance = ClosedCPP.from(this.graph);
        return (ClosedCPP<T>) this.instance;
    }

    @Override
    public <T> ClosedCPPInterface<T> getInterface() {
        return this.up();
    }

}