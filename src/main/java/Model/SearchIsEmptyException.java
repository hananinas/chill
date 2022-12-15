package Model;

public class SearchIsEmptyException extends Exception {
    public SearchIsEmptyException() {
        super("No matches found!");
    }
}

