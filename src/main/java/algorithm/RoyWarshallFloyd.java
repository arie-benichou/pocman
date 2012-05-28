
package algorithm;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

public final class RoyWarshallFloyd {

    private final static int X = Integer.MAX_VALUE;

    private RoyWarshallFloyd() {}

    private static int[][] copy(final int[][] path, final int max, final int infinity) {
        final int length = path.length;
        final int[][] pathCopy = new int[length][length];
        for (int i = 0; i < length; ++i)
            for (int j = 0; j < length; ++j)
                pathCopy[i][j] = path[i][j] >= max ? infinity : path[i][j];
        return pathCopy;
    }

    private static int[][] copy(final int[][] path) {
        return copy(path, X, X / 2);
    }

    public static int[][] from(final int[][] path) {
        final int[][] pathCopy = copy(path);
        final int length = pathCopy.length;
        for (int k = 0; k < length; ++k) {
            //debug(pathCopy);
            for (int i = 0; i < length; ++i) {
                for (int j = 0; j < length; ++j) {
                    /*
                    System.out.println("i = " + i);
                    System.out.println("j = " + j);
                    System.out.println("k = " + k);
                    System.out.println("ik : " + "[" + i + "]" + "[" + k + "]");
                    System.out.println("kj : " + "[" + k + "]" + "[" + j + "]");
                    System.out.println("ik = " + pathCopy[i][k]);
                    System.out.println("kj = " + pathCopy[k][j]);
                    */
                    pathCopy[i][j] = Math.min(pathCopy[i][j], pathCopy[i][k] + pathCopy[k][j]);
                    //System.out.println();
                }
                //System.out.println();
            }
        }
        return pathCopy;
    }

    public static void debug(final int[][] path) {
        final int[][] pathCopy = copy(path, X / 2, -1);
        final int n = (int) Math.floor(Math.log10(Ints.max(Ints.concat(pathCopy)))) + 1;
        System.out.println();
        for (final int[] i : pathCopy) {
            for (final int j : i)
                System.out.print(" " + Strings.padStart(String.valueOf(j == -1 ? Strings.repeat("?", n) : j), n, '0'));
            System.out.println();
        }
    }

    public static void main(final String[] args) {

        final int[][] path = {
                { 0, 3, X, X, 7, X, X },
                { 3, 0, 2, X, 4, 3, X },
                { X, 2, 0, 2, X, X, X },
                { X, X, 2, 0, X, X, 2 },
                { 7, 4, X, X, 0, 1, X },
                { X, 3, X, X, 1, 0, 2 },
                { X, X, X, 2, X, 2, 0 }
        };

        debug(path);

        final int[][] result = from(path);
        debug(result);

        debug(path);

        {
            final Stopwatch stopwatch = new Stopwatch().start();
            for (int i = 0; i < 100000; i++)
                from(path);
            System.out.println();
            System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS));
        }

    }
}