package Model;
/**
 * Search is empty exception for when a search result is no found.
 * **/
public class SearchIsEmptyException extends Exception {
    public SearchIsEmptyException() {
        super("No matches found!");
    }
}

