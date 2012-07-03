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

package pocman.matching.naive;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

/**
 * Minimal or maximal cost from independant set with maximal cardinality.
 * 
 * @author Arié Bénichou
 * @todo Edmonds's matching / Ford-Fulkerson algorithm implementations
 * @todo revoir le max
 */
public final class BranchAndBoundMatching {

    private final static int[] EMPTY_ARRAY = new int[0];
    private final static LowerBound MAX_LOWER_BOUND = new LowerBound(EMPTY_ARRAY, 0);
    private final static LowerBound MIN_LOWER_BOUND = new LowerBound(EMPTY_ARRAY, Integer.MAX_VALUE);
    private final static MatchesCollectorInterface NULL_PAIRS_COLLECTOR_INSTANCE = new NullPairsCollector();

    /**
     * Structure for a lower bound.
     */
    private final static class LowerBound {

        private final int[] pairs;
        private final double cost;

        public LowerBound(final int[] pairs, final double cost) {
            this.pairs = new int[pairs.length];
            System.arraycopy(pairs, 0, this.pairs, 0, pairs.length);
            this.cost = cost;
        }
    }

    /**
     * Enumeration of extremum: minimum, maximum.
     */
    public enum Extremum {

        MIN(1, MIN_LOWER_BOUND),
        MAX(-1, MAX_LOWER_BOUND);

        private final int signum;
        private final LowerBound lowerBound;

        private Extremum(final int signum, final LowerBound lowerBound) {
            this.signum = signum;
            this.lowerBound = lowerBound;
        }

        public int getSignum() {
            return this.signum;
        }

        private LowerBound getLowerBound() {
            return this.lowerBound;
        }

    }

    /**
     * Couple of a row index and a column index in the given matrix.
     */
    public final class Position {

        private final int rowIndex, columnIndex;
        private final int hashCode;

        public Position(final int rowIndex, final int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            //this.hashCode = MatchingSolver.this.dimension + (rowIndex + 1) * (columnIndex + 1) * (rowIndex + columnIndex);
            final int hashCode1 = rowIndex * BranchAndBoundMatching.this.dimension + columnIndex;
            final int hashCode2 = columnIndex * BranchAndBoundMatching.this.dimension + rowIndex;
            this.hashCode = hashCode1 * hashCode2;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null) return false;
            if (object == this) return true;
            if (!(object instanceof Position)) return false;
            final Position that = (Position) object;
            return that.hashCode() == this.hashCode();
        }

        public int getRowIndex() {
            return this.rowIndex;
        }

        public int getColumnIndex() {
            return this.columnIndex;
        }

        @Override
        public String toString() {
            return "(" + this.getRowIndex() + ", " + this.getColumnIndex() + ")";
        }
    }

    public final class Match {

        private final List<Position> positions;
        private final double cost;

        private Match(final int[] pairs, final double cost) {
            this.cost = Math.abs(cost);
            final int n = pairs.length * 2;
            final Builder<Position> positionsBuilder = new ImmutableList.Builder<Position>();
            for (final int i : pairs)
                positionsBuilder.add(new Position(i / n, i % n));
            this.positions = positionsBuilder.build();
        }

        public Match(final Set<Position> positions, final int[][] matrix) {
            this.positions = ImmutableList.copyOf(positions);
            double cost = 0;
            for (final Position position : this.positions)
                cost += matrix[position.getRowIndex()][position.getColumnIndex()];
            this.cost = cost;
        }

        public Match(final Set<Position> positions, final double[][] matrix) {
            this.positions = ImmutableList.copyOf(positions);
            double cost = 0;
            for (final Position position : this.positions)
                cost += matrix[position.getRowIndex()][position.getColumnIndex()];
            this.cost = cost;
        }

        private Match(final LowerBound lowerBound) {
            this(lowerBound.pairs, lowerBound.cost);
        }

        public List<Position> getPositions() {
            return this.positions;
        }

        public double getCost() {
            return this.cost;
        }

        public <T> List<T> apply(final Function<Position, T> function) {
            return Lists.transform(this.getPositions(), function);
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("positions", this.getPositions()).add("cost", this.getCost()).toString();
        }

    }

    private static interface MatchesCollectorInterface {

        MatchesCollectorInterface add(int[] pairs);

        MatchesCollectorInterface clear();
    }

    private final static class NullPairsCollector implements MatchesCollectorInterface {

        @Override
        public MatchesCollectorInterface add(final int[] pairs) {
            return this;
        }

        @Override
        public MatchesCollectorInterface clear() {
            return this;
        }

    }

    private final static class MatchesCollector implements MatchesCollectorInterface {

        private final LinkedList<int[]> pairs = Lists.newLinkedList();

        @Override
        public MatchesCollectorInterface add(final int[] pairs) {
            final int[] pairsCopy = new int[pairs.length];
            System.arraycopy(pairs, 0, pairsCopy, 0, pairs.length);
            this.pairs.add(pairsCopy);
            return this;
        }

        @Override
        public MatchesCollectorInterface clear() {
            this.pairs.clear();
            return this;
        }

    }

    private final class Matches implements Iterator<Match>, Iterable<Match> {

        private final MatchesCollector matchesCollector;
        private final double cost;

        public Matches(final MatchesCollector matchesCollector, final double cost) {
            this.matchesCollector = matchesCollector;
            this.cost = Math.abs(cost);
        }

        @Override
        public Iterator<Match> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return !this.matchesCollector.pairs.isEmpty();
        }

        @Override
        public Match next() {
            return new Match(this.matchesCollector.pairs.pop(), this.cost);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * The given even, squared and symetric matrix of positive costs.
     */
    private final double[][] matrix;

    /**
     * The dimension of this squared matrix.
     */
    private final int dimension;

    /**
     * Constructor for a matching problem instance.
     * 
     * @param matrix
     *            the given (read-only) matrix of positive costs.
     */
    public BranchAndBoundMatching(final double[][] matrix) { // TODO
        Preconditions.checkNotNull(matrix);
        final int n = matrix.length;
        Preconditions.checkState(n > 0 && n % 2 == 0);
        for (final double[] row : matrix) {
            Preconditions.checkState(row.length == n);
            for (final double cost : row)
                Preconditions.checkState(cost >= 0);
        }
        this.matrix = matrix;
        this.dimension = n;
    }

    private void swap(final int[] data, final int i) {
        final int tmp = data[1];
        data[1] = data[i];
        data[i] = tmp;
    }

    /**
     * Returns the lower bound for the cost of a perfect match.
     * 
     * @param depth
     *            the current depth
     * @param data
     *            the current (mutable) array of combination
     * @param pairs
     *            the path to the current combination
     * @param costSoFar
     *            the cost so far
     * @param signum
     *            1 if matching is minimizing, -1 if matching is maximising
     * @param lowerBound
     *            the current (mutable) lower bound
     * @param matchesCollector
     *            the matches collector
     * 
     * @return the lower bound for the cost of a perfect match
     */
    private LowerBound match(final int depth, final int[] data, final int[] pairs, final double costSoFar, final int signum, LowerBound lowerBound,
            final MatchesCollectorInterface matchesCollector) {
        final int n = this.dimension - depth * 2;
        if (n > 2) {
            for (int i = 1; i < n; ++i) {
                this.swap(data, i);
                final int rowIndex = data[0], columnIndex = data[1];
                pairs[depth] = this.dimension * rowIndex + columnIndex;
                final double cost = signum * this.matrix[rowIndex][columnIndex] + costSoFar;
                if (cost > signum * lowerBound.cost) {
                    break;
                }
                // TODO possible de ne pas avoir besoin de copier le tableau ?
                final LowerBound result = this.match(depth + 1, Arrays.copyOfRange(data, 2, n), pairs, cost, signum, lowerBound, matchesCollector);
                if (result.cost < lowerBound.cost) {
                    lowerBound = result;
                }
            }
        }
        else {
            final int rowIndex = data[0], columnIndex = data[1];
            pairs[depth] = this.dimension * rowIndex + columnIndex;
            final double cost = signum * this.matrix[rowIndex][columnIndex] + costSoFar;
            if (cost < lowerBound.cost) {
                lowerBound = new LowerBound(pairs, cost);
                matchesCollector.clear().add(pairs);
            }
            else if (Double.compare(cost, lowerBound.cost) == 0) matchesCollector.add(pairs);
        }
        return lowerBound;
    }

    /**
     * Returns the first perfect match with the lowest bound.
     * 
     * @param extremum
     *            MIN or MAX
     * @return the first perfect match with the lowest bound
     */
    private Match match(final Extremum extremum, final MatchesCollectorInterface matchesCollector) {
        Preconditions.checkNotNull(extremum);
        final int depth = 0;
        final int[] data = new int[this.dimension];
        for (int i = 0; i < this.dimension; ++i)
            data[i] = i;
        final int[] pairs = new int[this.dimension / 2];
        final double cost = 0;
        final int signum = extremum.getSignum();
        final LowerBound lowerBound = extremum.getLowerBound();
        return new Match(this.match(depth, data, pairs, cost, signum, lowerBound, matchesCollector));
    }

    public Iterable<Match> matchAll(final Extremum extremum) {
        final MatchesCollector matchesCollector = new MatchesCollector();
        final Match bestResult = this.match(extremum, matchesCollector);
        return new Matches(matchesCollector, bestResult.getCost());
    }

    public Iterable<Match> matchAll() {
        return this.matchAll(Extremum.MIN);
    }

    public Match match(final Extremum extremum) {
        return this.match(extremum, NULL_PAIRS_COLLECTOR_INSTANCE);
    }

    public Match match() {
        return this.match(Extremum.MIN);
    }

    /*
    public Match edmondMatch() {
    //        final int[][] matrix = new int[this.dimension][this.dimension];
    //        for (int i = 0; i < this.dimension; ++i)
    //            for (int j = 0; j < this.dimension; ++j) {
    //                matrix[i][j] = (int) this.matrix[i][j];
    //            }
        final Matching weightedMatch = new Matching(this.matrix);
        //final SmallerWeightedMatch weightedMatch = new SmallerWeightedMatch(matrix);
        //final WeightedMatch weightedMatch = new WeightedMatch(matrix);
        //final int[] mate = weightedMatch.weightedMatch(SmallerWeightedMatch.MINIMIZE);
        final Set<Position> positions = Sets.newHashSet();
        //System.out.println();
        for (int i = 0; i < this.dimension; ++i) {
            final Position position = new Position(this.i, mate[this.i + 1] - 1);
            this.positions.add(position);
        }
        //System.out.println();
        //System.out.println(positions);
        return new Match(positions, this.matrix);
    }
    */

    /**
     * Tiny tests / benchmarks.
     */
    public static void _main(final String[] args) {

        /*
        final double[][] matrix =
        {
                { 0, 1, 2, 3 },
                { 1, 0, 10, 20 },
                { 2, 10, 0, 10 },
                { 3, 20, 10, 0 },
        };
        */

        final double[][] matrix =
        {
                //     A  B  C  D
                /*A*/{ 0, 1, 5, 10 },
                /*B*/{ 1, 0, 1, 1 },
                /*C*/{ 5, 1, 0, 1 },
                /*D*/{ 10, 1, 1, 0 }
        };
        // TODO ? normaliser la matrice entre 0 et 1 ?
        /*
        final double[][] matrix =
        {
                { 0, 1, 1, 1, 1, 1 },
                { 1, 0, 1, 1, 1, 1 },
                { 1, 1, 0, 1, 1, 1 },
                { 1, 1, 1, 0, 1, 1 },
                { 1, 1, 1, 1, 0, 1 },
                { 1, 1, 1, 1, 1, 0 },
        };
        */

        /*
        final double[][] matrix =
        {
                { 0, 10, 1, 1, 1, 1, 1, 1 },
                { 10, 0, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 0, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 0, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 0, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 0, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 0 },
        };
        */

        final BranchAndBoundMatching matching = new BranchAndBoundMatching(matrix);
        final List<String> labels = Lists.newArrayList("A", "B", "C", "D", "E", "F");

        final Function<Position, String> mapping = new Function<Position, String>() {

            @Override
            public String apply(final Position position) {
                return labels.get(position.getRowIndex()) + labels.get(position.getColumnIndex());
            }

        };

        final Stopwatch stopwatch = new Stopwatch();

        stopwatch.start();
        //final Match firstMatch = matching.match(Extremum.MIN);
        //System.out.println(firstMatch);
        //System.out.println(firstMatch.apply(mapping));
        final Iterable<Match> matches = matching.matchAll();
        stopwatch.stop();
        for (final Match match : matches)
            System.out.println(match.apply(mapping));
        System.out.println(stopwatch.elapsedTime(TimeUnit.NANOSECONDS) + " " + TimeUnit.NANOSECONDS.toString());

    }
}