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

package fr.designpattern.pocman.graph;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.designpattern.pocman.graph.Path;
import fr.designpattern.pocman.graph.WeightedEdge;
import fr.designpattern.pocman.graph.Path.Factory;

public class PathTest { // TODO à compléter

    private Factory<String> factory;

    @Before
    public void setUp() throws Exception {
        this.factory = new Path.Factory<String>();
    }

    @After
    public void tearDown() throws Exception {
        this.factory = null;
    }

    @Test
    public void testIsNull() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.isNull());

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.isNull());

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(!path3.isNull());
    }

    @Test
    public void testGetNumberOfEdges() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.getNumberOfEdges() == 0);

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.getNumberOfEdges() == 0);

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.getNumberOfEdges() == 1);
    }

    @Test
    public void testGetEndPoint1() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.getEndPoint1() == null);

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.getEndPoint1() == null);

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.getEndPoint1().equals("A"));
    }

    @Test
    public void testGetEndPoint2() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.getEndPoint2() == null);

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.getEndPoint2() == null);

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.getEndPoint2().equals("B"));
    }

    @Test
    public void testGetWeight() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.getWeight() == 0);

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.getWeight() == 2.0);

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.getWeight() == 1.0);
    }

    @Test
    public void testCompareTo() {
        final Path<String> path1 = this.factory.newPath(1.0);
        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path1.compareTo(path1) == 0);
        assertTrue(path1.compareTo(path2) == -1);
        assertTrue(path2.compareTo(path1) == 1);
        assertTrue(path2.compareTo(path2) == 0);
    }

    @Test
    public void testGetEdges() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.getEdges().isEmpty());

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.getEdges().isEmpty());

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.getEdges().size() == 1);

        final WeightedEdge<String> edge = path3.getEdges().get(0);
        assertTrue(edge.getEndPoint1().equals("A"));
        assertTrue(edge.getEndPoint2().equals("B"));
        //assertTrue(edge.getWeight().equals(1.0)); // TODO utiliser Double
        assertTrue(edge.getWeight() == 1.0);
    }

    @Test
    public void testGetLastEdge() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.getLastEdge() == null);

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.getLastEdge() == null);

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.getLastEdge().equals(path3.getEdges().get(0)));
    }

    @Test
    public void testReverse() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.reverse() == path1);

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.reverse() == path2);

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(path3.reverse() != path3);
        assertTrue(path3.reverse().getEndPoint1().equals("B"));
        assertTrue(path3.reverse().getEndPoint2().equals("A"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullReferenceOfWeightedEdgeOfT() {
        final Path<String> path = this.factory.newPath();
        final WeightedEdge<String> edge = null;
        path.add(edge);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_NullPathOfT() {
        final Path<String> path = this.factory.newPath(9.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("A", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(!augmentedPath.isNull());
        assertTrue(augmentedPath.getEdges().size() == 1);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge1() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge2() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge3() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge4() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation1() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "D", 1.0);
        path.add(edge);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges1() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "D", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges2() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("D", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges3() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "D", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges4() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("D", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullReferenceOfPathOfT() {
        final Path<String> path1 = this.factory.newPath();
        final Path<String> path2 = null;
        path1.add(path2);
    }

    @Test
    public void testAddNullPathOfT_On_NullPathOfT() {
        final Path<String> path1 = this.factory.newPath(1.0);
        final Path<String> path2 = this.factory.newPath(2.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.isNull());
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddPathOfT_On_NullPathOfT() {
        final Path<String> path1 = this.factory.newPath(1.0);
        final Path<String> path2 = this.factory.newPath("A", "B", 2.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 1);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddNullPathOfT_On_PathOfT() {
        final Path<String> path1 = this.factory.newPath("A", "B", 2.0);
        final Path<String> path2 = this.factory.newPath(1.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 1);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge1() {
        final Path<String> path1 = this.factory.newPath("A", "B", 2.0);
        final Path<String> path2 = this.factory.newPath("B", "C", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge2() {
        final Path<String> path1 = this.factory.newPath("A", "B", 2.0);
        final Path<String> path2 = this.factory.newPath("C", "B", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge3() {
        final Path<String> path1 = this.factory.newPath("B", "A", 2.0);
        final Path<String> path2 = this.factory.newPath("C", "B", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge4() {
        final Path<String> path1 = this.factory.newPath("B", "A", 2.0);
        final Path<String> path2 = this.factory.newPath("B", "C", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation2() {
        final Path<String> path1 = this.factory.newPath("A", "B", 2.0);
        final Path<String> path2 = this.factory.newPath("C", "D", 4.0);
        path1.add(path2);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges1() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges2() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "C", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges3() {
        final Path<String> path1 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "C", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges4() {
        final Path<String> path1 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 6.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation3() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "E", 3.0);
        path1.add(path2);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges1() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0).add(this.factory.newPath("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges2() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "C", 3.0).add(this.factory.newPath("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges3() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "C", 3.0).add(this.factory.newPath("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges4() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0).add(this.factory.newPath("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathOfTAugmentation4() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "E", 3.0).add(this.factory.newPath("E", "F", 4.0));
        path1.add(path2);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges5() {
        final Path<String> path1 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0).add(this.factory.newPath("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges6() {
        final Path<String> path1 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("C", "B", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0).add(this.factory.newPath("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges7() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("C", "B", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0).add(this.factory.newPath("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges8() {
        final Path<String> path1 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("B", "C", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "C", 3.0).add(this.factory.newPath("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges9() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("C", "B", 2.0));
        final Path<String> path2 = this.factory.newPath("C", "D", 3.0).add(this.factory.newPath("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges10() {
        final Path<String> path1 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("C", "B", 2.0));
        final Path<String> path2 = this.factory.newPath("D", "C", 3.0).add(this.factory.newPath("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges11() {
        final Path<String> path1 = this.factory.newPath("D", "C", 3.0).add(this.factory.newPath("E", "D", 4.0));
        final Path<String> path2 = this.factory.newPath("B", "A", 1.0).add(this.factory.newPath("C", "B", 2.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("E"));
        assertTrue(augmentedPath.getEndPoint2().equals("A"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges12() {
        final Path<String> path1 = this.factory.newPath("A", "B", 3.0).add(this.factory.newPath("B", "C", 4.0));
        final Path<String> path2 = this.factory.newPath("A", "D", 1.0).add(this.factory.newPath("D", "E", 2.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("C"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges13() {
        final Path<String> path1 = this.factory.newPath("D", "A", 1.0).add(this.factory.newPath("E", "D", 2.0));
        final Path<String> path2 = this.factory.newPath("A", "B", 3.0).add(this.factory.newPath("B", "C", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("E"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges14() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "A", 2.0));
        final Path<String> path2 = this.factory.newPath("A", "C", 3.0).add(this.factory.newPath("C", "B", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges15() {
        final Path<String> path1 = this.factory.newPath("A", "B", 1.0).add(this.factory.newPath("B", "A", 2.0));
        final Path<String> path2 = this.factory.newPath("B", "C", 3.0).add(this.factory.newPath("A", "B", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertTrue(augmentedPath.getEdges().size() == 4);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 10.0);
    }

    //@Test
    public void testHashCode() {
        fail("Not yet implemented");
    }

    //@Test
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

    //@Test
    public void testToString() {
        fail("Not yet implemented");
    }

}