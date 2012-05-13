
package fr.ut7.dojo.pacman.graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

// TODO ? en faire une classe interne à Graph
public final class PathBuilder {

    private final static Function<TransientNode, Integer> F1 = new Function<TransientNode, Integer>() {

        public Integer apply(final TransientNode input) {
            return input.getId();
        }

    };

    // TODO retourner une map immutable
    public Map<Integer, PathNode> build(final TransientNode mutableTree) {
        final Map<Integer, PathNode> map = Maps.newHashMap();
        this.postorder(mutableTree, map);
        return map;
    }

    private void postorder(final TransientNode node, final Map<Integer, PathNode> map)
    {
        Collections.sort(node.getChildren());
        for (final TransientNode child : node.getChildren()) {
            this.postorder(child, map);
        }
        map.put(node.getId(), this.newPathNode(node, Lists.transform(node.getChildren(), F1)));
    }

    private PathNode newPathNode(final TransientNode t, final List<Integer> children) {
        return new PathNode(
                t.getParent() == null ? t.getId() : t.getParent().getId(),
                t.getId(),
                children,
                t.getNumberOfDescendants());
    }

}