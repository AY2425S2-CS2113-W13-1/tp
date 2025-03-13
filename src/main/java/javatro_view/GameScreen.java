package javatro_view;


public class GameScreen extends Screen {
    public GameScreen() {
        super("Game Screen");
    }

    @Override
    public void displayScreen() {}

    //    private static final List<String> options =
    //            Arrays.asList("Discard Hand", "See Target Score", "Play Hand", "Exit Game");
    //    private String blindType;
    //    private int roundScore;
    //    private int handsLeft;
    //    private int moneyInfo;
    //    private int discardCount;
    //    private int currentRound;
    //
    //    public GameScreen() {
    //        super(options,"GAME MENU");
    //        blindType = "SMALL";
    //        roundScore = 0;
    //        handsLeft = 0;
    //        moneyInfo = 0;
    //        discardCount = 0;
    //        currentRound = 0;
    //    }
    //
    //    // Shows all necessary components in a compact view
    //    private void displayGameScreenUI() {
    //        System.out.println("+------------------------------------------------+");
    //
    //        // blindType is max 10 chars
    //        String blindDisplay = String.format("%-10.10s", blindType);
    //
    //        System.out.printf("|          %-10s BLIND                      |\n", blindDisplay);
    //        System.out.printf(
    //                "|  [● %-10s BLIND]  Score: %-5d            |\n",
    //                blindDisplay, Math.min(10, 99999));
    //
    //        System.out.println("|------------------------------------------------|");
    //
    //        System.out.printf(
    //                "|  Round Score: ○%-6d                          |\n", Math.min(roundScore,
    // 999999));
    //        System.out.println("|------------------------------------------------|");
    //
    //        System.out.println("|  Run Info                                      |");
    //
    //        // Ensure handsLeft and discardCount fit within space
    //        System.out.printf(
    //                "|  Hands: %-2d       Discards: %-2d                  |\n",
    //                Math.min(handsLeft, 99), Math.min(discardCount, 99));
    //
    //        // Format money
    //        String moneyDisplay =
    //                moneyInfo >= 1_000_000
    //                        ? String.format("$%dM", moneyInfo / 1_000_000)
    //                        : moneyInfo >= 1000
    //                                ? String.format("$%dK", moneyInfo / 1000)
    //                                : String.format("$%d", moneyInfo);
    //
    //        System.out.printf("|  Money: %-8s                               |\n", moneyDisplay);
    //
    //        System.out.println("|------------------------------------------------|");
    //
    //        System.out.printf(
    //                "|  Round: %-3d                                    |\n",
    //                Math.min(currentRound, 999));
    //        System.out.println("|------------------------------------------------|");
    //
    //        System.out.printf("|  Deck: [▒▒▒▒▒▒]    Cards Left: %2d/52           |\n", Math.min(0,
    // 52));
    //
    //        System.out.println("|------------------------------------------------|");
    //
    //        // Print Card
    //        System.out.println();
    //        String[] cards = {
    //            "[ K♠ ]", "[ Q♠ ]", "[ 10♥ ]", "[ 9♣ ]", "[ 8♣ ]", "[ 6♠ ]", "[ 5♦ ]", "[ 3♦ ]"
    //        };
    //        int maxPerRow = 5;
    //        int rowCount = (int) Math.ceil(cards.length / (double) maxPerRow);
    //
    //        for (int row = 0; row < rowCount; row++) {
    //            int min = Math.min(cards.length, (row + 1) * maxPerRow);
    //            for (int i = row * maxPerRow; i < min; i++) {
    //                System.out.printf("%-8s", cards[i]);
    //            }
    //            // Fill remaining space if fewer than maxPerRow cards
    //            for (int i = min; i < (row + 1) * maxPerRow; i++) {
    //                System.out.print("         "); // Empty 8-char space
    //            }
    //            System.out.println();
    //            System.out.println();
    //        }
    //    }
    //
    //    // Method that shows Current Round
    //    public void displayRound() {}
    //
    //    // Method that displays blind score
    //    public void displayBlindScore() {}
    //
    //    // Method that displays current score
    //    public void displayCurrentScore() {}
    //
    //    // Method that displays the holding hand
    //    public void displayHoldingHand() {}
    //
    //    // Method that displays the number of plays
    //    public void displayPlays() {}
    //
    //    // Method that displays the number of discards so far
    //    public void displayDiscards() {}
    //
    //    // Method that displays the hand selected to play
    //    public void displayPlayingHand() {}
    //
    //    @Override
    //    public void displayScreen() {
    //        displayGameScreenUI();
    //        super.displayOptions();
    //    }
    //
    //    // Setters
    //    public void setBlindType(String blindType) {
    //        this.blindType = blindType;
    //    }
    //
    //    public void setRoundScore(int roundScore) {
    //        this.roundScore = roundScore;
    //    }
    //
    //    public void setHandsLeft(int handsLeft) {
    //        this.handsLeft = handsLeft;
    //    }
    //
    //    public void setMoneyInfo(int moneyInfo) {
    //        this.moneyInfo = moneyInfo;
    //    }
    //
    //    public void setDiscardCount(int discardCount) {
    //        this.discardCount = discardCount;
    //    }
    //
    //    public void setCurrentRound(int currentRound) {
    //        this.currentRound = currentRound;
    //    }
}
