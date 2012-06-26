
package fr.designpattern.pocman.game;

public enum Tile implements TileInterface {

    SPACE(' '),
    PILL('∙'),
    POCMAN('⬤'),
    HORIZONTAL_WALL('⬛'),
    VERTICAL_WALL('┃');

    private final char character;

    private Tile(final char character) {
        this.character = character;
    }

    @Override
    public char toCharacter() {
        return this.character;
    }

    @Override
    public boolean is(final Tile tile) {
        return this.equals(tile);
    }

    @Override
    public boolean isWall() {
        return this.is(VERTICAL_WALL) || this.is(HORIZONTAL_WALL);
    }

    @Override
    public boolean isWalkable() {
        return this.is(SPACE) || this.is(PILL) || this.is(Tile.POCMAN);
    }

}