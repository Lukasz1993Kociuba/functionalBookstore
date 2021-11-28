package com.example.functionalBookstore.domain.cart.core;

import com.example.functionalBookstore.domain.cart.core.model.CartItem;
import com.example.functionalBookstore.domain.cart.core.ports.incoming.AddCartItem;
import com.example.functionalBookstore.domain.cart.core.ports.incoming.GetLoggedUserCartItems;
import com.example.functionalBookstore.domain.cart.core.ports.outgoing.CartItemDatabase;
import com.example.functionalBookstore.domain.inventory.core.model.Book;
import com.example.functionalBookstore.domain.inventory.core.ports.incoming.GetBookFromBookId;
import com.example.functionalBookstore.domain.user.core.model.User;
import com.example.functionalBookstore.domain.user.core.ports.incoming.GetLoggedUser;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class CartService implements GetLoggedUserCartItems, AddCartItem {

    private final CartItemDatabase cartItemDatabase;

    private final GetLoggedUser getLoggedUser;

    @Override
    public List<CartItem> getLoggedUserCartItems() {
        return cartItemDatabase.findCartItemsByUser(getLoggedUserId())
                .orElseGet(ArrayList::new);
    }

    @Override
    public void addCartItem(Long bookId) {
        User user = getLoggedUser.getLoggedUser();

        //check if any CartItem contain book from bookId
            //function interface - using getLoggedUser
        List<CartItem> cartItems = getLoggedUserCartItems();
        CartItem cartItem = new CartItem();
        for (CartItem item : cartItems){
            if (item.getBook().getBookId().equals(bookId)){
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            } else {
            //    cartItem = this.createNewCartItem;
            }
        }
        //cartItemDatabase.save(cartItem);

    }

    private Long getLoggedUserId() {
        return getLoggedUser.getLoggedUser().getId();
    }
}
