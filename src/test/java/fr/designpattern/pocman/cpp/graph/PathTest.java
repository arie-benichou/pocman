
package fr.designpattern.pocman.cpp.graph;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.designpattern.pocman.cpp.graph.Path.Factory;

public class PathTest { // TODO à compléter

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    private Factory<String> factory;

    @Before
    public void setUp() throws Exception {
        this.factory = new Path.Factory<String>();
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testIsNull() {
        final Path<String> path1 = this.factory.newPath();
        assertTrue(path1.isNull());

        final Path<String> path2 = this.factory.newPath(2.0);
        assertTrue(path2.isNull());

        final Path<String> path3 = this.factory.newPath("A", "B", 1.0);
        assertTrue(!path3.isNull());

        /*
        assertTrue(path1.add(path2).isNull());
        assertTrue(!path1.add(path3).isNull());
        assertTrue(!path3.add(path1).isNull());
        */
        //assertTrue(path1.add(path2).getWeight() == 2.0); FIXME        
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

        /*
        final WeightedEdge<String> edge = path3.getEdges().get(0);
        assertTrue(edge.getEndPoint1().equals("A"));
        assertTrue(edge.getEndPoint2().equals("B"));
        assertTrue(edge.getWeight() == 1.0); // TODO utiliser Double
        */
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
    public void testAddWeightedEdgeOfTOnNullPath() {
        //final Path<String> path = this.factory.newPath(9.0); // FIXME
        final Path<String> path = this.factory.newPath();
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("A", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(!augmentedPath.isNull());
        assertTrue(augmentedPath.getEdges().size() == 1);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("B"));
        assertTrue(augmentedPath.getWeight() == 1.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingOneSingleEdge1() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingOneSingleEdge2() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingOneSingleEdge3() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingOneSingleEdge4() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "B", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 2);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("C"));
        assertTrue(augmentedPath.getWeight() == 2.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalPathAugmentation() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0);
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "D", 1.0);
        path.add(edge);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingManyEdges1() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "D", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingManyEdges2() {
        final Path<String> path = this.factory.newPath("A", "B", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("D", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingManyEdges3() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("C", "D", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    @Test
    public void testAddWeightedEdgeOfTOnPathHavingManyEdges4() {
        final Path<String> path = this.factory.newPath("B", "A", 1.0).add(new WeightedEdge.Factory<String>().newEdge("B", "C", 1.0));
        final WeightedEdge<String> edge = new WeightedEdge.Factory<String>().newEdge("D", "C", 1.0);
        final Path<String> augmentedPath = path.add(edge);
        assertTrue(augmentedPath.getEdges().size() == 3);
        assertTrue(augmentedPath.getEndPoint1().equals("A"));
        assertTrue(augmentedPath.getEndPoint2().equals("D"));
        assertTrue(augmentedPath.getWeight() == 3.0);
    }

    //@Test
    public void testAddPathOfT() {
        fail("Not yet implemented");
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