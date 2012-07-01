
package todo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

public class SmallerWeightedMatchDoubleTest2 {

    public static void main(final String[] args)
    {
        final double[][] costs =
        {
                //     A  B  C  D
                /*A*/{ 0, 1, 10.6, 10.5 },
                /*B*/{ 1, 0, 1, 1 },
                /*C*/{ 10.6, 1, 0, 1 },
                /*D*/{ 10.5, 1, 1, 0 }
        };

        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        final SmallerWeightedMatchDouble weightedMatch = new SmallerWeightedMatchDouble(costs);
        final int[] mate = weightedMatch.weightedMatch(SmallerWeightedMatchDouble.MINIMIZE);

        stopwatch.stop();

        final int[] matched = weightedMatch.getMatched(mate);
        final List<List<Integer>> partition = Lists.partition(Ints.asList(matched), 2);

        System.out.println(partition);

        System.out.println(stopwatch.elapsedTime(TimeUnit.MICROSECONDS) + " " + TimeUnit.MICROSECONDS.toString());

    }
}
