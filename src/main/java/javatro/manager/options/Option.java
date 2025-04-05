package javatro.manager.options;

import javatro.core.JavatroException;

public interface Option {


    /**
     * Retrieves a description of the command. This method has a default implementation returning an
     * empty string.
     *
     * @return A string representing the command description.
     */
    default String getDescription() {
        return "";
    }


    /**
     * Executes the command. Implementing classes must define specific behavior.
     *
     * @throws JavatroException If an error occurs during execution.
     */
    void execute() throws JavatroException;

    /**
     * Updates the description of the command. Default implementation does nothing.
     *
     * @param description The new description to set.
     */
    default void setDescription(String description) {
        throw new UnsupportedOperationException("Description updates are not supported by this option.");
    }


}
