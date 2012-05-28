
package cpp;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class CPPSolution {

    private final List<Path> path;

    public List<Path> getPath() {
        return this.path;
    }

    private final double cost;

    public double getCost() {
        return this.cost;
    }

    public CPPSolution(final List<Path> path, final double cost) {
        this.path = ImmutableList.copyOf(path);
        this.cost = cost;
    }

    public CPPSolution(final CPPSolution bestSolution, final double cost) {
        final Builder<Path> builder = new ImmutableList.Builder<Path>();
        final List<Path> bestPath = bestSolution.getPath();
        for (int k = 1; k < bestPath.size() - 1; ++k)
            builder.add(bestPath.get(k));
        this.path = builder.build();
        this.cost = cost;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (final Path path : this.path) {
            stringBuilder.append(path);
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Cost = " + this.getCost());
        return stringBuilder.toString();
    }

}