package org.example;

import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AmazonUnitTest {

    private ShoppingCart cart;
    private PriceRule rule1;
    private PriceRule rule2;
    private Amazon amazon;

    @BeforeEach
    void setup() {
        cart = mock(ShoppingCart.class);
        rule1 = mock(PriceRule.class);
        rule2 = mock(PriceRule.class);
        amazon = new Amazon(cart, List.of(rule1, rule2));
    }


    @Test
    @DisplayName("specification-based")
    void testCalculateAggregatesRuleResults() {
        when(cart.getItems()).thenReturn(List.of());
        when(rule1.priceToAggregate(anyList())).thenReturn(100.0);
        when(rule2.priceToAggregate(anyList())).thenReturn(50.0);

        double result = amazon.calculate();
        assertEquals(150.0, result);
    }

    @Test
    @DisplayName("specification-based")
    void testAddToCartDelegatesToCart() {
        Item item = new Item(null, "Book", 1, 10.0);
        amazon.addToCart(item);

        verify(cart).add(item);
    }


    @Test
    @DisplayName("structural-based")
    void testAllRulesInvoked() {
        when(cart.getItems()).thenReturn(List.of());
        amazon.calculate();

        verify(rule1, times(1)).priceToAggregate(anyList());
        verify(rule2, times(1)).priceToAggregate(anyList());
    }


    @Test
    @DisplayName("structural-based: should return zero if no rules are provided")
    void testNoRules() {
        Amazon emptyAmazon = new Amazon(cart, List.of());
        when(cart.getItems()).thenReturn(List.of());

        double result = emptyAmazon.calculate();
        assertEquals(0.0, result);
    }
}

