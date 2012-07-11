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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import graph.Path;
import graph.WeightedEdge;

import org.junit.Test;

public class PathTest { // TODO à compléter

    @Test
    public void testIsNull() {
        //final Path<String> path1 = Path.from();
        //assertTrue(path1.isNull());

        //final Path<String> path2 = Path.from(2.0);
        //assertTrue(path2.isNull());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertFalse(path3.isNull());
    }

    @Test
    public void testGetNumberOfEdges() {
        //final Path<String> path1 = Path.from();
        //assertEquals(0, path1.getNumberOfEdges());

        //final Path<String> path2 = Path.from(2.0);
        //assertEquals(0, path2.getNumberOfEdges());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertEquals(1, path3.getNumberOfEdges());
    }

    @Test
    public void testGetEndPoint1() {
        //final Path<String> path1 = Path.from();
        //assertNull(path1.getEndPoint1());

        //final Path<String> path2 = Path.from(2.0);
        //assertNull(path2.getEndPoint1());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertTrue(path3.getEndPoint1().equals("A"));
    }

    @Test
    public void testGetEndPoint2() {
        //final Path<String> path1 = Path.from();
        //assertNull(path1.getEndPoint2());

        //final Path<String> path2 = Path.from(2.0);
        //assertNull(path2.getEndPoint2());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertTrue(path3.getEndPoint2().equals("B"));
    }

    @Test
    public void testGetWeight() {
        //final Path<String> path1 = Path.from();
        //assertEquals(0, path1.getWeight(), 0.1);

        //final Path<String> path2 = Path.from(2.0);
        //assertEquals(2.0, path2.getWeight(), 0.1);

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertEquals(1.0, path3.getWeight(), 0.1);
    }

    /*
    @Test
    public void testCompareTo() {
        //final Path<String> path1 = Path.from(1.0);
        //final Path<String> path2 = Path.from(2.0);
        assertEquals(0, path1.compareTo(path1));
        assertEquals(-1, path1.compareTo(path2));
        assertEquals(1, path2.compareTo(path1));
        assertEquals(0, path2.compareTo(path2));
    }
    */

    @Test
    public void testGetEdges() {
        //final Path<String> path1 = Path.from();
        //assertTrue(path1.getEdges().isEmpty());

        //final Path<String> path2 = Path.from(2.0);
        //assertTrue(path2.getEdges().isEmpty());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertEquals(1, path3.getEdges().size());

        final WeightedEdge<String> edge = path3.getEdges().get(0);
        assertTrue(edge.getEndPoint1().equals("A"));
        assertTrue(edge.getEndPoint2().equals("B"));
        assertEquals(1.0, edge.getWeight(), 0.1);
    }

    @Test
    public void testGetLastEdge() {
        //final Path<String> path1 = Path.from();
        //assertNull(path1.getLastEdge());

        //final Path<String> path2 = Path.from(2.0);
        //assertNull(path2.getLastEdge());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertTrue(path3.getLastEdge().equals(path3.getEdges().get(0)));
    }

    @Test
    public void testReverse() {
        //final Path<String> path1 = Path.from();
        //assertEquals(path1, path1.reverse());

        //final Path<String> path2 = Path.from(2.0);
        //assertEquals(path2, path2.reverse());

        final Path<String> path3 = Path.from("A", "B", 1.0);
        assertNotSame(path3, path3.reverse());
        assertTrue(path3.reverse().getEndPoint1().equals("B"));
        assertTrue(path3.reverse().getEndPoint2().equals("A"));
    }

    /*
    @Test
    public void testAddWeightedEdgeOfT_On_NullPathOfT() {
        final Path<String> path = Path.from("A", "A", 9.0);
        final WeightedEdge<String> edge = WeightedEdge.from("A", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertFalse(augmentedPath.isNull());
        assertEquals(2, augmentedPath.getEdges().size()); // TODO ?
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }
    */

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge1() {
        final Path<String> path = Path.from("A", "B", 1.0);
        final WeightedEdge<String> edge = WeightedEdge.from("B", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(2.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge2() {
        final Path<String> path = Path.from("A", "B", 1.0);
        final WeightedEdge<String> edge = WeightedEdge.from("C", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(2.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge3() {
        final Path<String> path = Path.from("B", "A", 1.0);
        final WeightedEdge<String> edge = WeightedEdge.from("B", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(2.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingOneSingleEdge4() {
        final Path<String> path = Path.from("B", "A", 1.0);
        final WeightedEdge<String> edge = WeightedEdge.from("C", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(2.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges1() {
        final Path<String> path = Path.from("A", "B", 1.0).add(WeightedEdge.from("B", "C", 1.0));
        final WeightedEdge<String> edge = WeightedEdge.from("C", "D", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges2() {
        final Path<String> path = Path.from("A", "B", 1.0).add(WeightedEdge.from("B", "C", 1.0));
        final WeightedEdge<String> edge = WeightedEdge.from("D", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges3() {
        final Path<String> path = Path.from("B", "A", 1.0).add(WeightedEdge.from("B", "C", 1.0));
        final WeightedEdge<String> edge = WeightedEdge.from("C", "D", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddWeightedEdgeOfT_On_PathOfTHavingManyEdges4() {
        final Path<String> path = Path.from("B", "A", 1.0).add(WeightedEdge.from("B", "C", 1.0));
        final WeightedEdge<String> edge = WeightedEdge.from("D", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }

    /*
    @Test
    public void testAddNullPathOfT_On_NullPathOfT() {
        final Path<String> path1 = Path.from("A", "A", 1.0);
        final Path<String> path2 = Path.from("A", "A", 2.0);
        final Path<String> augmentedPath = path1.add(path2);
        //assertTrue(augmentedPath.isNull());
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }
    */

    /*
    @Test
    public void testAddPathOfT_On_NullPathOfT() {
        final Path<String> path1 = Path.from("A", "A", 1.0);
        final Path<String> path2 = Path.from("A", "B", 2.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }
    */

    /*
    @Test
    public void testAddNullPathOfT_On_PathOfT() {
        final Path<String> path1 = Path.from("A", "B", 2.0);
        final Path<String> path2 = Path.from("B", "B", 1.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertEquals(3.0, augmentedPath.getWeight(), 0.1);
    }
    */

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge1() {
        final Path<String> path1 = Path.from("A", "B", 2.0);
        final Path<String> path2 = Path.from("B", "C", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge2() {
        final Path<String> path1 = Path.from("A", "B", 2.0);
        final Path<String> path2 = Path.from("C", "B", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge3() {
        final Path<String> path1 = Path.from("B", "A", 2.0);
        final Path<String> path2 = Path.from("C", "B", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingOneSingleEdge4() {
        final Path<String> path1 = Path.from("B", "A", 2.0);
        final Path<String> path2 = Path.from("B", "C", 4.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(2, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges1() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges2() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "C", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges3() {
        final Path<String> path1 = Path.from("B", "A", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "C", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingOneSingleEdge_On_PathOfTHavingManyEdges4() {
        final Path<String> path1 = Path.from("B", "A", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0);
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(3, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertEquals(6.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges1() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0).add(Path.from("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges2() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "C", 3.0).add(Path.from("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges3() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "C", 3.0).add(Path.from("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges4() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0).add(Path.from("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges5() {
        final Path<String> path1 = Path.from("B", "A", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0).add(Path.from("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges6() {
        final Path<String> path1 = Path.from("B", "A", 1.0).add(Path.from("C", "B", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0).add(Path.from("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges7() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("C", "B", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0).add(Path.from("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges8() {
        final Path<String> path1 = Path.from("B", "A", 1.0).add(Path.from("B", "C", 2.0));
        final Path<String> path2 = Path.from("D", "C", 3.0).add(Path.from("D", "E", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges9() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("C", "B", 2.0));
        final Path<String> path2 = Path.from("C", "D", 3.0).add(Path.from("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges10() {
        final Path<String> path1 = Path.from("B", "A", 1.0).add(Path.from("C", "B", 2.0));
        final Path<String> path2 = Path.from("D", "C", 3.0).add(Path.from("E", "D", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges11() {
        final Path<String> path1 = Path.from("D", "C", 3.0).add(Path.from("E", "D", 4.0));
        final Path<String> path2 = Path.from("B", "A", 1.0).add(Path.from("C", "B", 2.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("E"));
        assertTrue(augmentedPath.getEndPoint2().equals("A"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges12() {
        final Path<String> path1 = Path.from("A", "B", 3.0).add(Path.from("B", "C", 4.0));
        final Path<String> path2 = Path.from("A", "D", 1.0).add(Path.from("D", "E", 2.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("C"));
        assertTrue(augmentedPath.getEndPoint2().equals("E"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges13() {
        final Path<String> path1 = Path.from("D", "A", 1.0).add(Path.from("E", "D", 2.0));
        final Path<String> path2 = Path.from("A", "B", 3.0).add(Path.from("B", "C", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("E"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges14() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "A", 2.0));
        final Path<String> path2 = Path.from("A", "C", 3.0).add(Path.from("C", "B", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    @Test
    public void testAddPathOfTHavingManyEdges_On_PathOfTHavingManyEdges15() {
        final Path<String> path1 = Path.from("A", "B", 1.0).add(Path.from("B", "A", 2.0));
        final Path<String> path2 = Path.from("B", "C", 3.0).add(Path.from("A", "B", 4.0));
        final Path<String> augmentedPath = path1.add(path2);
        assertEquals(4, augmentedPath.getEdges().size());
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertEquals(10.0, augmentedPath.getWeight(), 0.1);
    }

    /*
    @Test
    public void testHashCode() {
        fail("Not yet implemented");
    }

    @Test
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

    */

}