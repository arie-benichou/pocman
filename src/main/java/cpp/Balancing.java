
package cpp;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class Balancing {

    private final List<Integer> negatives;

    public List<Integer> getNegatives() {
        return this.negatives;
    }

    private final List<Integer> positives;

    public List<Integer> getPositives() {
        return this.positives;
    }

    public Balancing(final List<Integer> deltas) {
        final Builder<Integer> negativesBuilder = new ImmutableList.Builder<Integer>();
        final Builder<Integer> positivesBuilder = new ImmutableList.Builder<Integer>();
        for (int nodeIndex = 0; nodeIndex < deltas.size(); ++nodeIndex) {
            final int delta = deltas.get(nodeIndex);
            if (delta < 0) negativesBuilder.add(nodeIndex);
            else if (delta > 0) positivesBuilder.add(nodeIndex);
        }
        this.negatives = negativesBuilder.build();
        this.positives = positivesBuilder.build();
    }

}