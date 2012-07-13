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

package graph;

import graph.Path;
import graph.WeightedEdge;

import org.junit.Test;

public class PathTestExceptions { // TODO à compléter

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullReferenceOfWeightedEdgeOfT() {
        final Path<String> path = Path.from("A", "B", 1.0);
        final WeightedEdge<String> edge = null;
        path.add(edge);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation1() {
        final Path<String> path = Path.from("A", "B", 1.0);
        final WeightedEdge<String> edge = WeightedEdge.from("C", "D", 1.0);
        path.add(edge);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullReferenceOfPathOfT() {
        final Path<String> path1 = Path.from("A", "B", 1.0);
        final Path<String> path2 = null;
        path1.add(path2);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation2() {
        final Path<String> path1 = Path.from("A", "B", 2.0);
        final Path<String> path2 = Path.from("C", "D", 4.0);
        path1.add(path2);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation3() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "E", 3.0);
        path1.add(path2);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation4() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "E", 3.0).add(Path.from("E", "F", 4.0));
        path1.add(path2);
    }

}