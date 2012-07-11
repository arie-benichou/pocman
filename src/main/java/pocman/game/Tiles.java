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

package pocman.game;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;

public final class Tiles {

    private static final Tile[] TILES = Tile.values();
    private final static Builder<Character, Tile> TILE_BY_CHARACTER_MAP_BUILDER = new ImmutableSortedMap.Builder<Character, Tile>(Ordering.natural());
    static {
        for (final Tile tile : TILES)
            TILE_BY_CHARACTER_MAP_BUILDER.put(tile.toCharacter(), tile);
    }
    private final static ImmutableSortedMap<Character, Tile> TILE_BY_CHARACTER_MAP = TILE_BY_CHARACTER_MAP_BUILDER.build();

    public static Tile fromCharacter(final Character character) {
        Preconditions.checkArgument(character != null);
        final Tile tile = TILE_BY_CHARACTER_MAP.get(character);
        Preconditions.checkState(tile != null, "Unknown tile from character: " + character);
        return tile;
    }

    private Tiles() {}

}