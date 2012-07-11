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

package pocman.maze;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import pocman.game.Tile;

import com.google.common.collect.Sets;

public class TileTest {

    @Test
    public void testToCharacter() {
        final Tile[] tiles = Tile.values();
        final Set<Tile> setOfTiles = Sets.newHashSet(tiles);
        assertEquals(setOfTiles.size(), tiles.length);
    }

    @Test
    public void testIs() {
        assertTrue(Tile.SPACE.is(Tile.SPACE));
        assertFalse(Tile.SPACE.is(Tile.COIN));

        assertTrue(Tile.COIN.is(Tile.COIN));
        assertFalse(Tile.COIN.is(Tile.POCMAN));

        assertTrue(Tile.POCMAN.is(Tile.POCMAN));
        assertFalse(Tile.POCMAN.is(Tile.HORIZONTAL_WALL));

        assertTrue(Tile.HORIZONTAL_WALL.is(Tile.HORIZONTAL_WALL));
        assertFalse(Tile.HORIZONTAL_WALL.is(Tile.VERTICAL_WALL));

        assertTrue(Tile.VERTICAL_WALL.is(Tile.VERTICAL_WALL));
    }

    @Test
    public void testIsWall() {
        assertFalse(Tile.SPACE.isWall());
        assertFalse(Tile.COIN.isWall());
        assertFalse(Tile.POCMAN.isWall());
        assertTrue(Tile.HORIZONTAL_WALL.isWall());
        assertTrue(Tile.VERTICAL_WALL.isWall());
    }

    @Test
    public void testIsWalkable() {
        assertTrue(Tile.SPACE.isWalkable());
        assertTrue(Tile.COIN.isWalkable());
        assertTrue(Tile.POCMAN.isWalkable());
        assertFalse(Tile.HORIZONTAL_WALL.isWalkable());
        assertFalse(Tile.VERTICAL_WALL.isWalkable());
    }

    @Test
    public void testIsCollectable() {
        assertFalse(Tile.SPACE.isCollectable());
        assertTrue(Tile.COIN.isCollectable());
        assertFalse(Tile.POCMAN.isCollectable());
        assertFalse(Tile.HORIZONTAL_WALL.isCollectable());
        assertFalse(Tile.VERTICAL_WALL.isCollectable());
    }

}