package com.paynestsystem.app;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.Product;
import com.paynestsystem.payment.BuyNowPayLaterPayment;
import com.paynestsystem.payment.CardPayment;
import com.paynestsystem.payment.EftPayment;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.PaymentProcessor;
import com.paynestsystem.payment.WalletPayment;
import com.paynestsystem.service.CheckoutService;
import com.paynestsystem.service.OrderService;
import com.paynestsystem.service.OrderSummary;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PayNestApplication {

   /* Demo wiring: rail name to the object that implements it.
     * Adding a new rail means adding one entry here, nothing in checkout.
     */
private static Map<String, Supplier<PaymentMethod>> availableRails() {
        Map<String, Supplier<PaymentMethod>> rails = new LinkedHashMap<>();
        rails.put("card", CardPayment::new);
        rails.put("eft", EftPayment::new);
        rails.put("wallet", WalletPayment::new);
        rails.put("bnpl", BuyNowPayLaterPayment::new);
        rails.put("wallet-low", () -> new WalletPayment(500.00));
        return rails;
    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService(new OrderSummary());
        CheckoutService checkoutService = new CheckoutService(new PaymentProcessor());

        Product hammer = new Product(1, "Hammer", BigDecimal.valueOf(150));
        Product drill = new Product(2, "Electric Drill", BigDecimal.valueOf(2000));
        Product nails = new Product(3, "Box of Nails", BigDecimal.valueOf(120));
        Customer customer = new Customer(1, "Tshifhiwa Makherana", "Fifi@example.org");

        Order order = orderService.createOrder(35, customer);
        orderService.addItem(order, hammer, 1);
        orderService.addItem(order, drill, 1);
        orderService.addItem(order, nails, 3);

        orderService.printInvoice(order);

        //checkout through the PaymentMethod abstraction
        String railName = args.length > 0 ? args[0].toLowerCase() : "card";
        Supplier<PaymentMethod> rail = availableRails().get(railName);
        if (rail == null) {
            System.out.println("Unknown payment rail '" + railName + "'. Choose one of: "
                    + String.join(", ", availableRails().keySet()));
            return;
        }

        PaymentMethod paymentMethod = rail.get();
        checkoutService.checkout(order, paymentMethod);
    }
}

