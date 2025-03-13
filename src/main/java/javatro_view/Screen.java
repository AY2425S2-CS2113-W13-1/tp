package javatro_view;

import javatro_manager.Command;

import java.util.ArrayList;

// ALl screens will extend screen
public abstract class Screen {
    private String optionsTitle;
    protected ArrayList<Command> commandMap = new ArrayList<>();

    public Screen(String optionsTitle) {
        this.optionsTitle = optionsTitle;
    }

    public abstract void displayScreen();

    // Use this to display all the options
    protected void displayOptions() {
        int width = 30; // Fixed width for the menu
        String border = "*".repeat(width);
        int paddingSize = (width - optionsTitle.length() - 2) / 2;
        String paddedTitle =
                "*"
                        .concat(
                                " ".repeat(paddingSize)
                                        + optionsTitle
                                        + " "
                                                .repeat(
                                                        width
                                                                - optionsTitle.length()
                                                                - paddingSize
                                                                - 2)
                                        + "*");

        System.out.println(border);
        System.out.println(paddedTitle);
        System.out.println(border);

        int count = 1;
        for (Command s : commandMap) {
            System.out.printf("*  [%d] %s%n", count, s.getDescription());
            count++;
        }

        System.out.println(border);
    }

    public int getOptionsSize() {
        return commandMap.size();
    }

    public Command getCommand(int index) {
        return commandMap.get(index);
    }
}
