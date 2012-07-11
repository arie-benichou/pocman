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

public enum Tile {

    SPACE(' '),
    COIN('∙'),
    POCMAN('⬤'),
    HORIZONTAL_WALL('⬛'),
    VERTICAL_WALL('┃');

    private final char character;

    private Tile(final char character) {
        this.character = character;
    }

    public char toCharacter() {
        return this.character;
    }

    public boolean is(final Tile tile) {
        return this.equals(tile);
    }

    public boolean isWall() {
        return this.is(VERTICAL_WALL) || this.is(HORIZONTAL_WALL);
    }

    public boolean isWalkable() {
        return this.is(SPACE) || this.is(COIN) || this.is(Tile.POCMAN);
    }

    public boolean isCollectable() {
        return this.is(COIN);
    }

}