package edu.example.restz.exception;

public enum ProductException {
    NOT_FOUND("Product NOT FOUND", 404),
    NOT_REGISTERED("Product NOT Registered", 400),
    NOT_MODIFIED("Product NOT Modified", 400),
    NOT_REMOVED("Product NOT Removed", 400),
    NOT_FETCHED("Product NOT Fetched", 400),
    NO_IMAGE("NO Product Image", 400),
    REGISTER_ERR("NO Authenticated user", 403);

    private ProductTaskException productTaskException;

    ProductException(String message, int code) {
        productTaskException = new ProductTaskException(message, code);
    }

    public ProductTaskException get(){
        return productTaskException;
    }
}
