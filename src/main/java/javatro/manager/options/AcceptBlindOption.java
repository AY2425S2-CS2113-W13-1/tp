package javatro.manager.options;

import javatro.core.JavatroException;

//@@author swethaiscool
/**
 * Represents an option to accept the current blind in the game. This class implements the {@code
 * Option} interface.
 */
public class AcceptBlindOption implements Option {

    /**
     * Returns the description of this option.
     *
     * @return A string representing the description of the option.
     */
    @Override
    public String getDescription() {
        return "Accept Blind";
    }

    /**
     * Executes the action associated with accepting the blind. Currently, this method does not
     * perform any operations.
     *
     * @throws JavatroException if an error occurs during execution.
     */
    @Override
    public void execute() throws JavatroException {}
}
