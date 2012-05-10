
package fr.ut7.dojo.pacman;

import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameLoop;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.HalfRandomPacmanMoveEmitter;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.NullMoveEmitter;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;

public final class Launcher1 {

    private Launcher1() {}

    public static void launch(final String data) {
        new GameLoop().start(
                Game.from(
                        new PacmanReferee(),
                        new Pacman(new HalfRandomPacmanMoveEmitter()),
                        new GhostReferee(),
                        new Ghost(new NullMoveEmitter()),
                        GameState.from(data))
                );
    }

    public static void main(final String[] args) {
        launch(Levels.DEBUG113);
    }

}