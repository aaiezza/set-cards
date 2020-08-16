package org.shaba.setcards.application;

import static io.vavr.API.Try;
import static org.shaba.setcards.application.ConsoleRunner.Commands.DO_NOTHING;

import io.vavr.API;
import io.vavr.control.Try;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.shaba.setcards.representation.CardParser;
import org.shaba.setcards.representation.StandardCardParser;

@lombok.Data
public class ConsoleRunner {
  private final Game initialGame;
  private final Commands commands;
  private final InputStream inputStream;

  public void run() {
    System.out.println(initialGame);

    try (final Scanner sc = new Scanner(inputStream)) {
      final AtomicReference<Game> game = new AtomicReference<>(initialGame);
      while (sc.hasNextLine()) {
        final String input = sc.nextLine().trim();
        final Command command =
            commands
                .parseCommand(input)
                .onFailure(t -> System.err.println(t.getMessage()))
                .getOrElse(DO_NOTHING);

        if (command == Commands.EXIT) break;

        System.out.println(
            game.updateAndGet(
                g ->
                    command
                        .apply(g)
                        .onFailure(t -> System.err.println(t.getMessage()))
                        .getOrElse(g)));
      }
    }

    System.out.println(" :)");
  }

  public static ConsoleRunner standardConsoleRunner() {
    return new ConsoleRunner(Game.newGame(), Commands.standardCommands(), System.in);
  }

  @SuppressWarnings("unused")
  private static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  @lombok.Data
  public static class Commands {
    public static final Command EXIT = null;
    public static final Command DO_NOTHING = game -> Try(() -> game);

    private final CardParser cardParser;

    public static Commands standardCommands() {
      return new Commands(StandardCardParser.standardCardParser());
    }

    public Try<Command> parseCommand(final String commandString) {
      return API.<Command>Try(
          () -> {
            final String[] cmd = commandString.split("\\s+");
            switch (cmd[0]) {
              case "exit":
                return EXIT;
              case "rm":
                return game -> Try(() -> game.removeFieldCard(Integer.parseInt(cmd[1])));
              case "clear":
                return game -> Try(game::clearField);
              case "set":
                return DO_NOTHING; // TODO
              default:
                return game -> Try(() -> cardParser.apply(cmd[0]).map(game::addFieldCard).get());
            }
          });
    }
  }

  @FunctionalInterface
  public static interface Command extends Function<Game, Try<Game>> {}

  /*
   * Welcome to Set
   *    𝔽ield
   *     𝕊ets
   *
   * > 1rsf
   *    𝔽ield
   *  0: 1RSF
   *     𝕊ets
   *
   * > 2rsf
   *    𝔽ield
   *  0: 1RSF │  1: 2RSF
   *     𝕊ets
   *
   * > 3rsf
   *    𝔽ield
   *  0: 1RSF │  1: 2RSF │  2: 3RSF
   *     𝕊ets
   *   0: 1RSF, 2RSF, 3RSF
   *
   * > 1ged
   *    𝔽ield
   *  0: 1RSF │  1: 2RSF │  2: 3RSF
   *  3: 1GED
   *     𝕊ets
   *   0: 1RSF, 2RSF, 3RSF
   *
   * > 1PFO
   *    𝔽ield
   *  0: 1RSF │  1: 2RSF │  2: 3RSF
   *  3: 1GED │  4: 1PFO
   *     𝕊ets
   *   0: 1RSF, 2RSF, 3RSF
   *   1: 1RSF, 1GED, 1PFO
   *
   * > rem 1
   *    𝔽ield
   *  0: 1RSF │  1: 3RSF │  2: 1GED
   *  3: 1PFO
   *     𝕊ets
   *   0: 1RSF, 1GED, 1PFO
   *
   * > set 0
   *    𝔽ield
   *  0: 3RSF
   *     𝕊ets
   *
   * > 4fwe
   *  𝔼rror: `4FWE` is invalid
   *    𝔽ield
   *  0: 3RSF
   *     𝕊ets
   *
   * > 2rfd
   *    𝔽ield
   *  0: 3RSF │  1: 2RFD
   *     𝕊ets
   *
   * > clear
   *    𝔽ield
   *     𝕊ets
   *
   * > exit
   *  :)
   */
}
