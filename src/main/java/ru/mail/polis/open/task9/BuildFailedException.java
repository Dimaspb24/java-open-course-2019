package ru.mail.polis.open.task9;

public class BuildFailedException extends Exception {

    public BuildFailedException() {
        super("Error in build.");
    }

    public BuildFailedException(String description) {
        super(description);
    }

}
