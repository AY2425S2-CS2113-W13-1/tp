package Javatro;

public class Command {

    private PlayerDeck playerDeck;
    private int score;

    // Constructor
    public Command(Deck deck) {
        this.playerDeck = new PlayerDeck(deck);
        this.score = 0;
    }

    // Method to start a new game and shuffle a fresh deck
    public void startNewGame() {
        score = 0; // Reset score
        System.out.println("Welcome to Javatro!");
        System.out.println("Your game has started.");
        System.out.println("You have 8 cards in your hand.");
        System.out.println("Your current score: " + score);
        System.out.println("Type 'help' to see available commands.");
    }

    // Show current deck (the 8 cards for this game)
    public void showDeck() {
        playerDeck.showDeck();
    }

    // Show score
    public void showScore() {
        System.out.println("Your current score: " + score);
    }

    // Help command
    public void showHelp() {
        System.out.println("Available commands:");
        System.out.println("- newgame: Start a new game with a fresh deck.");
        System.out.println("- play <cards>: Play a hand of poker with the cards you have.");
        System.out.println("- score: View your current score.");
        System.out.println("- help: Display this help message.");
        System.out.println("- quit: Quit the game.");
    }

    // Quit the game
    public void quitGame() {
        System.out.println("Thanks for playing Javatro!");
        System.exit(0);
    }
}
