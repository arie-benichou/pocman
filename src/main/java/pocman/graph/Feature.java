
package pocman.graph;

import java.lang.reflect.Method;

import pocman.graph.features.Connectivity;
import pocman.graph.features.Degree;
import pocman.graph.features.Routing;

import com.google.common.base.Preconditions;

public enum Feature {

    NONE(null),
    CONNECTIVITY(Connectivity.class),
    ROUTING(Routing.class),
    DEGREE(Degree.class);

    private final Class<?> featureClass;

    private Feature(final Class<?> featureClass) {
        this.featureClass = featureClass;
    }

    public <T> Object on(final UndirectedGraph<T> graph) {
        Object feature = null;
        Exception featureException = null;
        try {
            final Method method = this.getFeatureClass().getMethod("from", UndirectedGraph.class);
            feature = method.invoke(null, graph);
        }
        catch (final Exception exception) {
            featureException = exception;
        }
        Preconditions.checkState(feature != null, featureException);
        return feature;
    }

    public Class<?> getFeatureClass() {
        return this.featureClass;
    }

}