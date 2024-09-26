package edu.example.restz.repository;
import edu.example.restz.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(" SELECT c FROM CartItem c JOIN FETCH c.product " +
           " JOIN FETCH c.product.images " +
           " WHERE c.cart.customer = :customer ORDER BY c.itemNo DESC ")
    Optional<List<CartItem>> getCartItems(@Param("customer") String customer);

    @Query(" SELECT ci.cart.customer FROM CartItem ci " +
           " WHERE  ci.itemNo = :itemNo ")
    Optional<String> getCartItemCustomer(@Param("itemNo") Long itemNo);

}
