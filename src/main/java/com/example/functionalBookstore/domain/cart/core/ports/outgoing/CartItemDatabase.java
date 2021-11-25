package com.example.functionalBookstore.domain.cart.core.ports.outgoing;

import com.example.functionalBookstore.domain.cart.core.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemDatabase {
    Optional<List<CartItem>> getLoggedUserCartItems(Long loggedUserId);
}
