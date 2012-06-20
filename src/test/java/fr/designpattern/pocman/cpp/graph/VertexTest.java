
package fr.designpattern.pocman.cpp.graph;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.designpattern.pocman.cpp.graph.Vertex.Type;
import fr.designpattern.pocman.model.Move;

public class VertexTest {

    private static Map<String, Vertex> vertices;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        vertices = new ImmutableMap.Builder<String, Vertex>()
                .put(Type.ISLAND.name(), Vertex.from(0, new HashSet<Move>()))
                .put(Type.DEAD_END.name(), Vertex.from(1, Sets.newHashSet(Move.GO_UP)))
                .put(Type.STREET.name(), Vertex.from(2, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN)))
                .put(Type.CORNER.name(), Vertex.from(3, Sets.newHashSet(Move.GO_UP, Move.GO_RIGHT)))
                .put(Type.CROSSROADS.name(), Vertex.from(4, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT)))
                .put(Type.ROUNDABOUT.name(), Vertex.from(5, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT)))
                .build();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        vertices = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromUnsupportedMazeNode() {
        Vertex.from(-1, Sets.newHashSet(Move.GO_NOWHERE, Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT));
    }

    @Test
    public void testGetId() {
        assertTrue(vertices.get(Type.ISLAND.name()).getId() == 0);
        assertTrue(vertices.get(Type.DEAD_END.name()).getId() == 1);
        assertTrue(vertices.get(Type.STREET.name()).getId() == 2);
        assertTrue(vertices.get(Type.CORNER.name()).getId() == 3);
        assertTrue(vertices.get(Type.CROSSROADS.name()).getId() == 4);
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).getId() == 5);
    }

    @Test
    public void testGetType() {
        assertTrue(vertices.get(Type.ISLAND.name()).getType().equals(Type.ISLAND));
        assertTrue(vertices.get(Type.DEAD_END.name()).getType().equals(Type.DEAD_END));
        assertTrue(vertices.get(Type.STREET.name()).getType().equals(Type.STREET));
        assertTrue(vertices.get(Type.CORNER.name()).getType().equals(Type.CORNER));
        assertTrue(vertices.get(Type.CROSSROADS.name()).getType().equals(Type.CROSSROADS));
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).getType().equals(Type.ROUNDABOUT));
    }

    @Test
    public void testIs() {
        assertTrue(vertices.get(Type.ISLAND.name()).is(Type.ISLAND));
        assertTrue(vertices.get(Type.DEAD_END.name()).is(Type.DEAD_END));
        assertTrue(vertices.get(Type.STREET.name()).is(Type.STREET));
        assertTrue(vertices.get(Type.CORNER.name()).is(Type.CORNER));
        assertTrue(vertices.get(Type.CROSSROADS.name()).is(Type.CROSSROADS));
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).is(Type.ROUNDABOUT));
    }

    @Test
    public void testGetNumberOfOptions() {
        assertTrue(vertices.get(Type.ISLAND.name()).getNumberOfOptions() == 0);
        assertTrue(vertices.get(Type.DEAD_END.name()).getNumberOfOptions() == 1);
        assertTrue(vertices.get(Type.STREET.name()).getNumberOfOptions() == 2);
        assertTrue(vertices.get(Type.CORNER.name()).getNumberOfOptions() == 2);
        assertTrue(vertices.get(Type.CROSSROADS.name()).getNumberOfOptions() == 3);
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).getNumberOfOptions() == 4);
    }

    @Test
    public void testGetOptions() {
        assertTrue(vertices.get(Type.ISLAND.name()).getOptions().equals(new HashSet<Move>()));
        assertTrue(vertices.get(Type.DEAD_END.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP)));
        assertTrue(vertices.get(Type.STREET.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_DOWN)));
        assertTrue(vertices.get(Type.CORNER.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_RIGHT)));
        assertTrue(vertices.get(Type.CROSSROADS.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT)));
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).getOptions().equals(Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet0() {
        vertices.get(Type.ISLAND.name()).getOptions().clear();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet1() {
        vertices.get(Type.ISLAND.name()).getOptions().add(Move.GO_NOWHERE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet2() {
        vertices.get(Type.ISLAND.name()).getOptions().addAll(Lists.newArrayList(Move.GO_NOWHERE, Move.GO_NOWHERE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet3() {
        vertices.get(Type.ISLAND.name()).getOptions().remove(Move.GO_NOWHERE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet4() {
        vertices.get(Type.ISLAND.name()).getOptions().removeAll(Lists.newArrayList(Move.GO_NOWHERE, Move.GO_NOWHERE));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionsAsImmutableSet5() {
        vertices.get(Type.ISLAND.name()).getOptions().retainAll(Lists.newArrayList(Move.GO_NOWHERE, Move.GO_NOWHERE));
    }

    @Test
    public void testEqualsObject() {
        assertTrue(vertices.get(Type.ISLAND.name()).equals(null) == false);
        assertTrue(vertices.get(Type.ISLAND.name()).equals(vertices.get(Type.ISLAND.name())) == true);
        assertTrue(vertices.get(Type.ISLAND.name()).equals(new Object()) == false);
        assertTrue(vertices.get(Type.ISLAND.name()).equals(vertices.get(Type.DEAD_END.name())) == false);
        assertTrue(vertices.get(Type.ISLAND.name()).equals(Vertex.from(0, new HashSet<Move>())) == true);
        assertTrue(vertices.get(Type.ISLAND.name()).equals(Vertex.from(1, new HashSet<Move>())) == false);
        assertTrue(vertices.get(Type.ISLAND.name()).equals(Vertex.from(0, Sets.newHashSet(Move.GO_NOWHERE))) == false);
    }

    @Test
    public void testHashCode() { // TODO à compléter...

        assertTrue(vertices.get(Type.ISLAND.name()).hashCode() != vertices.get(Type.DEAD_END.name()).hashCode());
        assertTrue(vertices.get(Type.DEAD_END.name()).hashCode() != vertices.get(Type.STREET.name()).hashCode());
        assertTrue(vertices.get(Type.STREET.name()).hashCode() != vertices.get(Type.CORNER.name()).hashCode());
        assertTrue(vertices.get(Type.CORNER.name()).hashCode() != vertices.get(Type.CROSSROADS.name()).hashCode());
        assertTrue(vertices.get(Type.CROSSROADS.name()).hashCode() != vertices.get(Type.ROUNDABOUT.name()).hashCode());

        assertTrue(vertices.get(Type.ISLAND.name()).hashCode() == Vertex.from(0, new HashSet<Move>()).hashCode());
        assertTrue(vertices.get(Type.ISLAND.name()).hashCode() != Vertex.from(1, new HashSet<Move>()).hashCode());
        assertTrue(vertices.get(Type.ISLAND.name()).hashCode() != Vertex.from(0, Sets.newHashSet(Move.GO_NOWHERE)).hashCode());

        assertTrue(vertices.get(Type.DEAD_END.name()).hashCode() == Vertex.from(1, Sets.newHashSet(Move.GO_UP)).hashCode());

        assertTrue(vertices.get(Type.STREET.name()).hashCode() == Vertex.from(2, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN)).hashCode());
        assertTrue(vertices.get(Type.STREET.name()).hashCode() == Vertex.from(2, Sets.newHashSet(Move.GO_DOWN, Move.GO_UP)).hashCode());

        assertTrue(vertices.get(Type.CORNER.name()).hashCode() == Vertex.from(3, Sets.newHashSet(Move.GO_UP, Move.GO_RIGHT)).hashCode());
        assertTrue(vertices.get(Type.CORNER.name()).hashCode() == Vertex.from(3, Sets.newHashSet(Move.GO_RIGHT, Move.GO_UP)).hashCode());

        assertTrue(vertices.get(Type.CROSSROADS.name()).hashCode() == Vertex.from(4, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT)).hashCode());
        assertTrue(vertices.get(Type.CROSSROADS.name()).hashCode() == Vertex.from(4, Sets.newHashSet(Move.GO_RIGHT, Move.GO_UP, Move.GO_DOWN)).hashCode());

        assertTrue(vertices.get(Type.ROUNDABOUT.name()).hashCode() == Vertex.from(5, Sets.newHashSet(Move.GO_UP, Move.GO_DOWN, Move.GO_RIGHT, Move.GO_LEFT))
                .hashCode());
        assertTrue(vertices.get(Type.ROUNDABOUT.name()).hashCode() == Vertex.from(5, Sets.newHashSet(Move.GO_RIGHT, Move.GO_LEFT, Move.GO_UP, Move.GO_DOWN))
                .hashCode());
    }

    //@Test
    public void testToString() { // TODO tester la vue...
        fail("Not yet implemented");
    }

}