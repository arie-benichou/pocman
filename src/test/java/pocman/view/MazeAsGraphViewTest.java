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

package pocman.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pocman.game.Maze;

public class MazeAsGraphViewTest { // TODO à compléter

    private final static Maze MAZE = Maze.from("" +
            "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛∙┃∙⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙┃∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛∙⬛⬛⬛⬛⬛┃" +
            "┃∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙∙┃" +
            "┃⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛┃");

    @Test(expected = IllegalArgumentException.class)
    public void testRenderWithNullReference() {
        new MazeAsGraphView().render(null);
    }

    @Test
    public void testRender() {
        final String expected = "" +
                "                           " + "\n" +
                " 1∙∙∙∙3∙∙∙∙∙3∙3∙∙∙∙∙3∙∙∙∙1 " + "\n" +
                "      ∙     ∙ ∙     ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙             ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙     ∙ ∙     ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙             ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙     ∙ ∙     ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙             ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙     ∙ ∙     ∙      " + "\n" +
                " 1∙∙∙∙4∙∙∙∙∙2 2∙∙∙∙∙4∙∙∙∙1 " + "\n" +
                "      ∙             ∙      " + "\n" +
                " 1∙∙∙∙3∙∙∙∙∙∙∙∙∙∙∙∙∙3∙∙∙∙1 " + "\n" +
                "                           " + "\n";
        ;
        assertTrue(expected.equals(new MazeAsGraphView().render(MAZE)));
    }

}