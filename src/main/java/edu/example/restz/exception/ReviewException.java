package edu.example.restz.exception;

public enum ReviewException {
    NOT_REGISTERED("Review NOT Registered", 400),
    PRODUCT_NOT_FOUND("Product NOT FOUND for Review", 404),
    NOT_FOUND("Review NOT FOUND", 404),
    NOT_MODIFIED("Review NOT Modified", 400),
    NOT_REMOVED("Review NOT Removed", 400),
    NOT_FETCHED("Review NOT Fetched", 400),
    NOT_MATCHED("Review NOT Matched", 400),
    NOT_MATCHED_REVIEWER("Reviewer NO Matched", 400);

    private ReviewTaskException reviewTaskException;

    ReviewException(String message, int code) {
        reviewTaskException = new ReviewTaskException(message, code);
    }

    public ReviewTaskException get(){
        return reviewTaskException;
    }
}
