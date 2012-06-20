
package fr.designpattern.pocman.cpp.graph;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.designpattern.pocman.cpp.graph.WeightedEdge.Factory;

public class WeightedEdgeTest {

    private Factory<String> factory;

    @Before
    public void setup() {
        this.factory = new WeightedEdge.Factory<String>();
    }

    @After
    public void tearDown() {
        this.factory = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeFromOneEndpointToSameEndpoint() {
        this.factory.newEdge("A", "A", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeWithEndpoint1BeingNull() {
        this.factory.newEdge(null, "B", 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeWithEndpoint2BeingNull() {
        this.factory.newEdge("A", null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewEdgeWithNegativeWeight() {
        this.factory.newEdge("A", "B", -1.0);
    }

    @Test
    public void testGetEndPoint1() {
        final WeightedEdge<String> edge = this.factory.newEdge("A", "B", 1.0);
        assertTrue(edge.getEndPoint1().equals("A"));
    }

    @Test
    public void testGetEndPoint2() {
        final WeightedEdge<String> edge = this.factory.newEdge("A", "B", 1.0);
        assertTrue(edge.getEndPoint2().equals("B"));
    }

    @Test
    public void testGetWeight() {
        final WeightedEdge<String> edge = this.factory.newEdge("A", "B", 1.0);
        assertTrue(edge.getWeight() == 1.0);
    }

    @Test
    public void testGetSymetric() {
        final WeightedEdge<String> edge = this.factory.newEdge("A", "B", 1.0);
        final WeightedEdge<String> symetricEdge = edge.getSymetric();
        assertTrue(symetricEdge.getEndPoint1().equals("B"));
        assertTrue(symetricEdge.getEndPoint2().equals("A"));
        assertTrue(symetricEdge.getWeight() == 1.0);
    }

    @Test
    public void testCompareTo() {
        final WeightedEdge<String> edge1 = this.factory.newEdge("A", "C", 2.0);
        final WeightedEdge<String> edge2 = this.factory.newEdge("A", "D", 4.0);
        assertTrue(edge1.compareTo(edge1) == 0);
        assertTrue(edge1.compareTo(edge2) == -1);
        assertTrue(edge2.compareTo(edge2) == 0);
        assertTrue(edge2.compareTo(edge1) == 1);
    }

    @Test
    public void testEqualsObject() {
        final WeightedEdge<String> edge1 = this.factory.newEdge("A", "C", 2.0);
        final WeightedEdge<String> edge2 = this.factory.newEdge("A", "D", 2.0);
        final WeightedEdge<String> edge3 = edge1.getSymetric();

        assertTrue(edge1.equals(null) == false);
        assertTrue(edge1.equals(edge1) == true);
        assertTrue(edge1.equals(new Object()) == false);

        assertTrue(edge1.equals(edge2) == false);
        assertTrue(edge2.equals(edge1) == false);

        assertTrue(edge1.equals(edge3));
        assertTrue(edge3.equals(edge1));

        assertTrue(edge1.equals(this.factory.newEdge("A", "C", 2.0)) == true);
        assertTrue(edge1.equals(this.factory.newEdge("C", "A", 2.0)) == true);

        assertTrue(edge1.equals(this.factory.newEdge("B", "C", 2.0)) == false);
        assertTrue(edge1.equals(this.factory.newEdge("A", "B", 2.0)) == false);
        assertTrue(edge1.equals(this.factory.newEdge("A", "C", 1.0)) == false);

        final WeightedEdge<Integer> newEdge1 = new WeightedEdge.Factory<Integer>().newEdge(1, 2, 1.0);
        final WeightedEdge<Double> newEdge2 = new WeightedEdge.Factory<Double>().newEdge(1.0, 2.0, 1.0);
        assertTrue(newEdge1.equals(newEdge2) == false);
    }

    @Test
    public void testHashCode() {
        final WeightedEdge<String> edge1 = this.factory.newEdge("A", "C", 2.0);
        final WeightedEdge<String> edge2 = this.factory.newEdge("A", "D", 2.0);
        final WeightedEdge<String> edge3 = edge1.getSymetric();
        assertTrue(edge1.hashCode() != edge2.hashCode());
        assertTrue(edge1.hashCode() == this.factory.newEdge("A", "C", 2.0).hashCode());
        assertTrue(edge1.hashCode() == this.factory.newEdge("C", "A", 2.0).hashCode());
        assertTrue(edge1.hashCode() == edge3.hashCode());
    }

}