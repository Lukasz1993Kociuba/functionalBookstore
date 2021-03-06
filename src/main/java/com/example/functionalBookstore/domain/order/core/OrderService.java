package com.example.functionalBookstore.domain.order.core;

import com.example.functionalBookstore.domain.cart.core.model.CartItem;
import com.example.functionalBookstore.domain.cart.core.ports.incoming.DeleteCartItemAndCustomerInfoAfterOrderSave;
import com.example.functionalBookstore.domain.cart.core.ports.incoming.GetCartFinalAmount;
import com.example.functionalBookstore.domain.cart.core.ports.incoming.GetCustomerInfo;
import com.example.functionalBookstore.domain.cart.core.ports.incoming.GetLoggedUserCartItems;
import com.example.functionalBookstore.domain.order.core.model.AddOrderCommand;
import com.example.functionalBookstore.domain.order.core.model.Order;
import com.example.functionalBookstore.domain.order.core.model.OrderItem;
import com.example.functionalBookstore.domain.order.core.ports.incoming.AddNewOrder;
import com.example.functionalBookstore.domain.order.core.ports.incoming.GetOrders;
import com.example.functionalBookstore.domain.order.core.ports.outgoing.OrderDatabase;
import com.example.functionalBookstore.domain.order.core.ports.outgoing.OrderItemDatabase;
import com.example.functionalBookstore.domain.user.core.ports.incoming.GetLoggedUser;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class OrderService implements AddNewOrder, GetOrders {

    private final GetLoggedUser getLoggedUser;
    private final OrderDatabase orderDatabase;
    private final GetCartFinalAmount getCartFinalAmount;
    private final GetCustomerInfo getCustomerInfo;
    private final OrderItemDatabase orderItemDatabase;
    private final GetLoggedUserCartItems getLoggedUserCartItems;
    private final DeleteCartItemAndCustomerInfoAfterOrderSave deleteCartItemAndCustomerInfoAfterOrderSave;

    @Override
    public void saveOrder() {
        var order = this.createNewOrder();
        orderDatabase.save(order);

        this.saveOrderItemsToOrder(order);

        deleteCartItemAndCustomerInfoAfterOrderSave.deleteCartItemAndCustomerInfoAfterOrderSave();
    }

    @Override
    public Optional<List<Order>> getOrders() {
        return orderDatabase.findAllOrders();
    }

    private final Function<Double, BigDecimal> transformPriceToBigDecimal = BigDecimal::valueOf;

    private Order createNewOrder() {
        var addOrderCommand = new AddOrderCommand()
                .setUser(getLoggedUser.getLoggedUser())
                .setOrderNumber(this.newOrderNumber())
                .setCalculatedPrice(transformPriceToBigDecimal.apply(getCartFinalAmount.calculateCartFinalAmount()))
                .setCustomerInfo(getCustomerInfo.getCustomerInfo());

        return addOrderCommand.getOrder();
    }

    private void saveOrderItemsToOrder(Order order) {
        var loggedUserCartItems = getLoggedUserCartItems.getLoggedUserCartItems();
        for (CartItem cartItem : loggedUserCartItems) {
            var orderItem = new OrderItem(order, cartItem);
            orderItemDatabase.save(orderItem);
        }
    }

    private int newOrderNumber() {
        var orderList = this.getOrders();

        var maxOrderNumber = orderList.get().stream()
                .max(Comparator.comparing(Order::getOrderNumber))
                .map(Order::getOrderNumber);
        return maxOrderNumber.orElseGet(() -> 0) + 1;
    }
}
