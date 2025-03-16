package javatro_manager;

public class ExitGameCommand implements Command {

    @Override
    public String getDescription() {
        return "Exit Game";
    }

    @Override
    public void execute() {
        System.out.println("================================================");
        System.out.println(" GGGG   OOO   OOO   DDDD    BBBB   Y   Y  EEEEE ");
        System.out.println("G       O   O O   O  D   D   B   B   Y Y   E     ");
        System.out.println("G  GG   O   O O   O  D   D   BBBB     Y    EEEE  ");
        System.out.println("G   G   O   O O   O  D   D   B   B    Y    E     ");
        System.out.println(" GGGG   OOO   OOO   DDDD    BBBB     Y    EEEEE ");
        System.out.println("================================================");
        System.out.println("      Thanks for playing! See you next time!     ");
        System.out.println("================================================");
        System.exit(0);
    }
}
