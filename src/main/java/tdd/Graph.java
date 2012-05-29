
package tdd;

import java.util.Map;

import com.google.common.base.Strings;

public final class Graph {

    private final int n;
    private final Map<Integer, Integer> vertices;
    private final Map<Integer, Integer> edges;
    private final double[][] values;
    private double[][] paths;

    public double[][] getPaths() {
        //if (this.paths == null) this.isConnected();
        return this.paths; // TODO
    }

    public static Graph from(final Map<Integer, Integer> vertices, final Map<Integer, Integer> edges, final double[][] values) {
        return new Graph(vertices, edges, values);
    }

    private Graph(final Map<Integer, Integer> vertices, final Map<Integer, Integer> edges, final double[][] values) {
        this.n = vertices.size();
        this.vertices = vertices;
        this.edges = edges;
        this.values = values;
        this.isConnected();
    }

    // A connected graph has a path between every pair of nodes
    public boolean isConnected() {

        boolean mightBeNotConnected = false;

        double max = 0;

        final double[][] paths = new double[this.n][this.n];
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (i == j) continue;
                final double value = this.values[i][j];
                if (value == 0) {
                    mightBeNotConnected = true;
                    paths[i][j] = Double.POSITIVE_INFINITY;
                }
                else {
                    paths[i][j] = value;
                    if (value > max)
                        max = value;
                }
            }
        }

        //System.out.println(mightBeNotConnected);

        if (mightBeNotConnected == true) {
            for (int k = 0; k < this.n; ++k) {
                for (int i = 0; i < this.n; ++i) {
                    for (int j = 0; j < this.n; ++j) {
                        if (i == j) continue;
                        final double newCost = paths[i][k] + paths[k][j];
                        if (newCost < paths[i][j]) paths[i][j] = newCost;
                    }
                }
            }
        }

        //this.debug(paths, max, Double.POSITIVE_INFINITY);

        this.paths = paths;

        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (i == j) continue;
                if (paths[i][j] == Double.POSITIVE_INFINITY) return false;
            }
        }
        return true;
    }

    private void debug(final double[][] paths, final double max, final double infinity) {
        //System.out.println();
        final int n = (int) Math.floor(Math.log10(Double.valueOf(max).intValue())) + 1;
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                String string;
                if (i == j) string = Strings.repeat(".", n);
                else if (paths[i][j] == infinity) string = Strings.repeat("X", n);
                else string = Strings.padStart(String.valueOf((int) paths[i][j]), n, '0');
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

}
