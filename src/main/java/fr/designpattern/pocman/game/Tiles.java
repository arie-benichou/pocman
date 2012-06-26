
package fr.designpattern.pocman.game;

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