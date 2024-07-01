package org.yearup.data;
import org.springframework.stereotype.Repository;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import java.sql.SQLException;

//@Repository
public interface ShoppingCartDao{
    ShoppingCart getByUserId(int userId) throws SQLException;
    ShoppingCart addProductToCart(int productId, int userId);
    ShoppingCart updateCartItem(int userId, int productId, ShoppingCartItem shoppingCartItem);
    void saveCart(ShoppingCart cart);
    void clearCart(int userId);

    void executeAndUpdate(String query, Object... params);
}
