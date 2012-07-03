
package /*
         * Copyright 2012 Arie Benichou
         * 
         * This program is free software: you can redistribute it and/or modify
         * it under the terms of the GNU General Public License as published by
         * the Free Software Foundation, either version 3 of the License, or (at
         * your option) any later version.
         * 
         * This program is distributed in the hope that it will be useful, but
         * WITHOUT ANY WARRANTY; without even the implied warranty of
         * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
         * General Public License for more details.
         * 
         * You should have received a copy of the GNU General Public License
         * along with this program. If not, see <http://www.gnu.org/licenses/>.
         */pocman.maze;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DirectionTest {

    private final static String[] NAMES = { "NOWHERE", "UP", "RIGHT", "DOWN", "LEFT" };

    @Test
    public void testDirection() {
        final Direction[] values = Direction.values();
        assertEquals(NAMES.length, values.length);
        int i = 0;
        for (final String string : NAMES) {
            assertTrue(Direction.valueOf(string).equals(values[i]));
            ++i;
        }
    }

}