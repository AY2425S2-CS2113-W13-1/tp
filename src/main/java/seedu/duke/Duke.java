package seedu.duke;

import java.util.Scanner;

public final class Duke {
  // Private constructor to prevent instantiation
  /**
   * Main entry-point for the java.duke.Duke application.
   *
   * @param args The command-line arguments passed to the program.
   */
  public static void main(final String[] args) {
    String logo =
        " ____        _        \n"
            + "|  _ \\ _   _| | _____ \n"
            + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";
    System.out.println("Hello from\n" + logo);
    System.out.println("What is your name?");
    int valuetest = 2;

    Scanner in = new Scanner(System.in);
    System.out.println("Hello " + in.nextLine());
  }
}
