package org.library.meta;

/**
 * enum class representing return results
 */
public enum ReturnRes {
    SUCCESS("Book return successfully"),
    BORROW_NOT_FOUND("Borrow not found"),
    ALREADY_RETURNED("Borrow already returned"),
    SYSTEM_ERROR("Book return failed");

    private String message;

    ReturnRes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
