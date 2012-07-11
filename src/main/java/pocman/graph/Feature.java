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

    public Class<?> getFeatureClass() {
        return this.featureClass;
    }

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

}