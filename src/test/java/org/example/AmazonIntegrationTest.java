package org.example;

import org.example.Amazon.Cost.*;
import org.example.Amazon.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class AmazonIntegrationTest {

    private Database db;
    private ShoppingCartAdaptor cart;
    private List<PriceRule> rules;
    private Amazon amazon;

    @BeforeEach
    void setup() {
        db = new Database();
        db.resetDatabase();
        cart = new ShoppingCartAdaptor(db);
        rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        amazon = new Amazon(cart, rules);
    }

    @AfterEach
    void cleanup() {
        db.close();
    }


    @Test
    @DisplayName("specification-based")
    void testCalculateTotalPriceWithElectronics() {
        amazon.addToCart(new Item(ItemType.ELECTRONIC, "Phone", 1, 500.0));
        amazon.addToCart(new Item(ItemType.OTHER, "Book", 2, 20.0));

        double total = amazon.calculate();

        // RegularCost = (500*1) + (20*2) = 540
        // DeliveryPrice (2 items) = 5
        // ExtraCostForElectronics = 7.5
        // Total = 552.5
        assertEquals(552.5, total, 0.001);
    }

    @Test
    @DisplayName("specification-based")
    void testEmptyCart() {
        double total = amazon.calculate();
        assertEquals(0.0, total);
    }


    @Test
    @DisplayName("structural-based")
    void testAddToCartPersistsToDB() {
        amazon.addToCart(new Item(ItemType.OTHER, "Pen", 3, 2.0));
        amazon.addToCart(new Item(ItemType.OTHER, "Notebook", 1, 5.0));

        List<Item> storedItems = cart.getItems();
        assertEquals(2, storedItems.size());
        assertEquals("Pen", storedItems.get(0).getName());
    }

    @Test
    @DisplayName("structural-based")
    void testDatabaseResetAndRecalculate() {
        amazon.addToCart(new Item(ItemType.OTHER, "Pencil", 2, 1.0));
        double totalBefore = amazon.calculate();
        assertEquals(7.0, totalBefore); // 2 * 1 + 5 delivery = 7.0

        db.resetDatabase();

        double totalAfter = amazon.calculate();
        assertEquals(0.0, totalAfter);
    }
}

