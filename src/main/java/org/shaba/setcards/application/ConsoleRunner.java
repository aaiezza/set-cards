package org.shaba.setcards.application;

import static io.vavr.API.Try;
import static java.lang.Integer.parseInt;
import static org.shaba.setcards.application.ConsoleRunner.Commands.DO_NOTHING;
import static org.shaba.setcards.application.ConsoleRunner.Commands.printHelp;

import io.vavr.API;
import io.vavr.control.Try;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import one.util.streamex.StreamEx;
import org.shaba.setcards.representation.CardParser;
import org.shaba.setcards.representation.StandardCardParser;

@lombok.Data
public class ConsoleRunner {
  private final Game initialGame;
  private final Commands commands;
  private final InputStream inputStream;
  private final PrintStream out;
  private final PrintStream err;

  public void run() {
    clearScreen(out);

    out.println(initialGame);

    try (final Scanner sc = new Scanner(inputStream)) {
      final AtomicReference<Game> game = new AtomicReference<>(initialGame);
      while (sc.hasNextLine()) {
        final String input = sc.nextLine().trim();
        final Command command =
            commands
                .parseCommand(input)
                .map(cmd -> cmd == Commands.HELP ? cmd.also(printHelp(out)) : cmd)
                .onFailure(printErrorMessage(err))
                .getOrElse(DO_NOTHING);

        if (command == Commands.EXIT) break;

        out.println(
            game.updateAndGet(
                g -> command.apply(g).onFailure(printErrorMessage(err)).getOrElse(g)));
      }
    }

    out.println(" :)");
  }

  private static Consumer<Throwable> printErrorMessage(final PrintStream err) {
    return t -> err.printf("𝔼rror: %s%n", t.getMessage());
  }

  private static void clearScreen(final PrintStream out) {
    out.print("\033[H\033[2J");
    out.flush();
  }

  public static ConsoleRunner standardConsoleRunner() {
    return new ConsoleRunner(
        Game.newGame(), Commands.standardCommands(), System.in, System.out, System.err);
  }

  @lombok.Data
  public static class Commands {
    public static final Command EXIT = null;
    public static final Command DO_NOTHING = game -> Try(() -> game);
    public static final Command HELP = game -> Try(() -> game);

    private final CardParser cardParser;

    public static Commands standardCommands() {
      return new Commands(StandardCardParser.standardCardParser());
    }

    public Try<Command> parseCommand(final String commandString) {
      return API.<Command>Try(
          () -> {
            final String[] cmd = commandString.split("\\s+");
            switch (cmd[0].toLowerCase()) {
              case "exit":
                return EXIT;
              case "?":
              case "h":
              case "help":
                return HELP;
              case "rm":
              case "rem":
                return game -> Try(() -> game.removeFieldCard(parseInt(cmd[1])));
              case "c":
              case "clear":
                return game -> Try(game::clearField);
              case "set":
                return DO_NOTHING; // TODO
              default:
                return game -> Try(() -> cardParser.apply(cmd[0]).map(game::addFieldCard).get());
            }
          });
    }

    public static Runnable printHelp(final PrintStream out) {
      return () -> {
        out.println("𝔸vailable commands:");
        out.printf(
            StreamEx.of(
                    "[123][RrGgPp][EeSsFf][OoFfDd] (card quantity, color, fill, shape)",
                    "set",
                    "rm,rem",
                    "clear,c",
                    "exit",
                    "help,h,?")
                .joining("%n ", " ", "%n%n"));
      };
    }
  }

  @FunctionalInterface
  public static interface Command extends Function<Game, Try<Game>> {
    public default Command also(final Runnable runnable) {
      return andThen(
              game -> {
                runnable.run();
                return game;
              })
          ::apply;
    }
  }

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
