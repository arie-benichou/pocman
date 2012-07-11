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

package pocman.matching;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;

public class Matches<T> implements Supplier<Map<T, T>>, Iterable<Entry<T, T>> {

    private final Map<T, T> matches;

    @Override
    public Map<T, T> get() {
        return this.matches;
    }

    private final Double cost;

    public Double getCost() {
        return this.cost;
    }

    public Matches(final Map<T, T> matches, final Double cost) {
        this.matches = ImmutableMap.copyOf(matches);
        this.cost = cost;
    }

    @Override
    public Iterator<Entry<T, T>> iterator() {
        return this.matches.entrySet().iterator();
    }

}