package pl.javastart.dnd;

public class CanNotCalculateSetSortOrderException extends RuntimeException{
    public CanNotCalculateSetSortOrderException() {
    }

    public CanNotCalculateSetSortOrderException(String message) {
        super(message);
    }
}
