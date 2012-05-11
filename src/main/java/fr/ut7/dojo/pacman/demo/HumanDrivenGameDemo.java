
package fr.ut7.dojo.pacman.demo;

import fr.ut7.dojo.pacman.controller.HumanController;
import fr.ut7.dojo.pacman.model.Game;
import fr.ut7.dojo.pacman.model.GameLoop;
import fr.ut7.dojo.pacman.model.GameState;
import fr.ut7.dojo.pacman.model.GhostReferee;
import fr.ut7.dojo.pacman.model.Levels;
import fr.ut7.dojo.pacman.model.NullMoveEmitter;
import fr.ut7.dojo.pacman.model.PacmanReferee;
import fr.ut7.dojo.pacman.model.characters.Ghost;
import fr.ut7.dojo.pacman.model.characters.Pacman;

public final class HumanDrivenGameDemo {

    private HumanDrivenGameDemo() {}

    public static void launch(final String data) {
        new GameLoop().start(
                Game.from(
                        new PacmanReferee(),
                        new Pacman(new HumanController()),
                        new GhostReferee(),
                        new Ghost(new NullMoveEmitter()),
                        GameState.from(data))
                );
    }

    public static void main(final String[] args) {
        launch(Levels.DEBUG113);
    }

}