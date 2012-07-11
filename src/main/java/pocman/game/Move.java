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


public enum Move { // TODO à revoir

    GO_NOWHERE(0) {

        @Override
        public Move getOpposite() {
            return GO_NOWHERE;
        }

        @Override
        public String toString() {
            return "⬤";
        }

    },

    GO_UP(-27) {

        @Override
        public Move getOpposite() {
            return GO_DOWN;
        }

        @Override
        public String toString() {
            return "▲";
        }

    },

    GO_RIGHT(1) {

        @Override
        public Move getOpposite() {
            return GO_LEFT;
        }

        @Override
        public String toString() {
            return "▶";
        }

    },
    GO_DOWN(+27) {

        @Override
        public Move getOpposite() {
            return GO_UP;
        }

        @Override
        public String toString() {
            return "▼";
        }

    },
    GO_LEFT(-1) {

        @Override
        public Move getOpposite() {
            return GO_RIGHT;
        }

        @Override
        public String toString() {
            return "◀";
        }

    };

    public static Move from(final Direction direction) {
        return Move.valueOf("GO_" + direction);
    }

    private int delta;

    private Move(final int delta) {
        this.delta = delta;
    }

    public int getDelta() {
        return this.delta;
    }

    public abstract Move getOpposite();

}