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

package matching.edmonds2;

import graph.UndirectedGraph;
import graph.features.routing.RoutingFeature;
import graph.features.routing.RoutingInterface;

import java.util.List;
import java.util.Map;

import matching.MatchingAlgorithmInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

/**
 * Minimum Weight Perfect Matching Algorithm
 */
public final class Matching implements MatchingAlgorithmInterface {

    @Override
    public <T> matching.Matches<T> from(final UndirectedGraph<T> residualGraph) {

        final RoutingInterface<T> routingInterface = residualGraph.fetch(RoutingFeature.class).up();
        final double[][] weights = routingInterface.getShortestPathWeights();

        final WeightedMatchDouble weightedMatch = new WeightedMatchDouble(weights);
        final int[] mate = weightedMatch.weightedMatch(WeightedMatchDouble.MINIMIZE);
        final int[] matched = weightedMatch.getMatched(mate);

        double cost = 0.0;
        final List<List<Integer>> partition = Lists.partition(Ints.asList(matched), 2);
        final Map<T, T> matches = Maps.newHashMap();

        for (final List<Integer> position : partition) {
            final Integer i = position.get(0);
            final Integer j = position.get(1);
            matches.put(residualGraph.get(i), residualGraph.get(j));
            cost += weights[i][j];
        }

        return new matching.Matches<T>(matches, cost);
    }

    @Override
    public <T> void setOriginalGraph(final UndirectedGraph<T> graph) {
        // TODO Auto-generated method stub
    }

}