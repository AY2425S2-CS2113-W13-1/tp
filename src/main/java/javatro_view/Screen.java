package javatro_view;

import java.util.List;

// ALl screens will extend screen
public abstract class Screen {
    private List<String> options = List.of();

    public Screen(List<String> options) {
        this.options = options;
    }

    public abstract void displayScreen();

    // Use this to display all the options
    protected void displayOptions() {
        int count = 1;
        for (String s : options) {
            System.out.println(count + ". " + s);
            count++;
        }
    }

    public int getOptionsSize() {
        return options.size();
    }
}
