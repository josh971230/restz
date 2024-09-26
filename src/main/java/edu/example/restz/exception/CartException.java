package edu.example.restz.exception;

public enum CartException {
    NOT_FOUND_CART("Cart NOT FOUND", 404),
    NOT_FOUND_CARTITEM("CartItem NOT FOUND", 404),
    NOT_FOUND_PRODUCT("Product NOT FOUND for Cart", 404),
    FAIL_ADD("Cart Add Fail", 400),
    FAIL_MODIFY("Cart Modify Fail", 400),
    NOT_MATCHED_CARTITEM("CartItem NOT Matched", 400),
    NOT_MATCHED_CUSTOMER("Customer NOT Matched", 400),

    FAIL_REMOVE("Cart Remove Fail", 400),
    /////////////////////////////////////////////////////,
    NOT_FETCHED("Cart NOT Fetched", 400);

    private CartTaskException cartTaskException;

    CartException(String message, int code) {
        cartTaskException = new CartTaskException(message, code);
    }

    public CartTaskException get(){
        return cartTaskException;
    }
}
