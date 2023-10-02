package org.library.meta;

/**
 * enum class representing borrow results
 */
public enum BorrowRes {
    SUCCESS("Book borrowed successfully"),
    BOOK_NOT_FOUND("Book not found"),
    ALREADY_BORROWED("You have already borrowed this book"),
    OVERDUE("You have overdue borrowed book"),
    BORROW_OVER_LIMIT("You have already borrowed 5 books"),
    NO_COPY("The book has no copies"),
    SYSTEM_ERROR("Book borrowed failed");

    private String message;

    BorrowRes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
