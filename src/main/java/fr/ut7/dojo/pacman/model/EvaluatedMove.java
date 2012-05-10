
package fr.ut7.dojo.pacman.model;

import java.util.List;

public class EvaluatedMove implements Comparable<EvaluatedMove> {

    private final Move move;
    private final int score;
    private final List<Move> path;

    public EvaluatedMove(final Move move, final int score, final List<Move> path) {
        this.move = move;
        this.score = score;
        this.path = path;
    }

    public Move getMove() {
        return this.move;
    }

    public int getScore() {
        return this.score;
    }

    public List<Move> getPath() {
        return this.path;
    }

    public int compareTo(final EvaluatedMove that) {
        return this.score - that.score;
    }

    @Override
    public String toString() {
        /*
        return Objects.toStringHelper(this)
                .add("move", this.move)
                .add("score", this.score)
                .toString();
        */
        return "" + this.getMove() + this.getScore();
    }
}