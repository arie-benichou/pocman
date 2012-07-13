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

package matching.naive;

import graph.UndirectedGraph;
import graph.features.routing.RoutingFeature;
import graph.features.routing.RoutingInterface;

import java.util.List;
import java.util.Map;

import matching.MatchingAlgorithmInterface;
import matching.naive.BranchAndBoundMatching.Match;
import matching.naive.BranchAndBoundMatching.Position;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class Matching implements MatchingAlgorithmInterface {

    @Override
    public <T> matching.Matches<T> from(final UndirectedGraph<T> residualGraph) {

        final RoutingInterface<T> routingInterface = residualGraph.fetch(RoutingFeature.class).up();

        final double[][] weights = routingInterface.getShortestPathWeights();

        final Match match = new BranchAndBoundMatching(weights).match();

        final Function<Position, Map<T, T>> mapping = new Function<Position, Map<T, T>>() {

            @Override
            public Map<T, T> apply(final Position position) {
                final ImmutableMap.Builder<T, T> builder = new ImmutableMap.Builder<T, T>();
                builder.put(residualGraph.get(position.getRowIndex()), residualGraph.get(position.getColumnIndex()));
                return builder.build();
            }

        };

        final List<Map<T, T>> map = match.apply(mapping);

        final Map<T, T> result = Maps.newHashMap(); // TODO
        for (final Map<T, T> entry : map)
            result.putAll(entry);

        return new matching.Matches<T>(result, match.getCost());

    }

    @Override
    public <T> void setOriginalGraph(final UndirectedGraph<T> graph) {}

}