
package old;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * Roy-Warshall-Floyd algorithm
 */
public final class ShortestPaths {

    private static Path[][] copy(final Path[][] paths) {
        final int length = paths.length;
        final Path[][] pathCopy = new Path[length][length];
        for (int i = 0; i < length; ++i)
            for (int j = 0; j < length; ++j) {
                pathCopy[i][j] = paths[i][j];
            }
        return pathCopy;
    }

    private final Path[][] shortestPaths;

    public Path[][] getShortestPaths() {
        return this.shortestPaths;
    }

    private double maximalCost = Double.NEGATIVE_INFINITY;

    public double getMaximalCost() {
        return this.maximalCost;
    }

    public ShortestPaths(final Path[][] paths) {
        this.shortestPaths = copy(paths);
        final int length = this.shortestPaths.length;
        for (int k = 0; k < length; ++k) {
            for (int i = 0; i < length; ++i) {
                for (int j = 0; j < length; ++j) {
                    final double currentCost = this.shortestPaths[i][j].getCost();
                    final double newCost = this.shortestPaths[i][k].getCost() + this.shortestPaths[k][j].getCost();
                    if (newCost < currentCost) {
                        this.shortestPaths[i][j] = this.shortestPaths[i][k].add(this.shortestPaths[k][j]);
                        if (newCost > this.maximalCost) this.maximalCost = newCost;
                    }
                    else if (currentCost != Double.POSITIVE_INFINITY && currentCost > this.maximalCost) this.maximalCost = currentCost;
                }
            }
        }
    }

    @Override
    public String toString() {
        final int n = (int) Math.floor(Math.log10(Double.valueOf(this.maximalCost).intValue())) + 1;
        final StringBuilder sb = new StringBuilder();
        sb.append("\n----------------8<----------------\n");
        sb.append("\n");
        for (final Path[] row : this.shortestPaths) {
            for (final Path path : row) {
                sb.append(" ");
                String value;
                if (path.getCost() == Double.POSITIVE_INFINITY) value = Strings.repeat(".", n);
                else value = String.valueOf(Double.valueOf(path.getCost()).intValue());
                sb.append(Strings.padStart(value, n, '0'));
            }
            sb.append("\n");
        }
        sb.append("\n----------------8<----------------\n");
        for (final Path[] row : this.shortestPaths) {
            for (final Path path : row) {
                if (path.getCost() == 0 || path.getCost() == Double.POSITIVE_INFINITY) continue;
                sb.append("\nThe shortest path from "
                        + path.getEdges().get(0).getFirstMazeNode()
                        + " to "
                        + path.getLastEdge().getLastMazeNode()
                        + " is: "
                        + path);
                sb.append("\n");
            }
            sb.append("\n----------------8<----------------\n");
        }
        return sb.toString();
    }

    public static void main(final String[] args) {

        final Edge edge1 = new Edge(1, Lists.newArrayList(2), 3);
        final Edge edge2 = new Edge(3, Lists.newArrayList(4, 5), 6);
        final Edge edge3 = new Edge(6, Lists.newArrayList(7, 8, 9), 10);
        final Edge edge4 = new Edge(10, Lists.newArrayList(11, 12, 13, 14), 1);

        /*
         * nodes:
         * 0:1
         * 1:3
         * 2:6
         * 3:10
         */

        final Path O = Path.from(0);
        final Path N = Path.from(Double.POSITIVE_INFINITY);

        final Path a = Path.from(edge1);
        final Path z = Path.from(edge1.getSymetric());

        final Path b = Path.from(edge2);
        final Path y = Path.from(edge2.getSymetric());

        final Path c = Path.from(edge3);
        final Path x = Path.from(edge3.getSymetric());

        final Path d = Path.from(edge4);
        final Path w = Path.from(edge4.getSymetric());

        final Path[][] paths = {
                //       1  3  6  10
                /*  1*/{ O, a, N, w },
                /*  3*/{ z, O, b, N },
                /*  6*/{ N, y, O, c },
                /* 10*/{ d, N, x, O }
        };

        final Stopwatch stopwatch = new Stopwatch().start();
        final ShortestPaths shortestPaths = new ShortestPaths(paths);
        System.out.println(shortestPaths);
        System.out.println(stopwatch.elapsedTime(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS.toString());
    }
}