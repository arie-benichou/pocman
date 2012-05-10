
package fr.ut7.dojo.pacman.model;

public enum Move {

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