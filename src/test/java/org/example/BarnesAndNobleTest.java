package org.example;

import org.example.Barnes.BarnesAndNoble;
import org.example.Barnes.Book;
import org.example.Barnes.BookDatabase;
import org.example.Barnes.BuyBookProcess;
import org.example.Barnes.PurchaseSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based")
    void testGetPriceForCart_NullOrder() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble store = new BarnesAndNoble(db, process);

        assertNull(store.getPriceForCart(null));
    }

    @Test
    @DisplayName("specification-based")
    void testGetPriceForCart_TotalPrice() {
        // Mock setup
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        Book book = new Book("123", 10, 5);
        when(db.findByISBN("123")).thenReturn(book);

        BarnesAndNoble store = new BarnesAndNoble(db, process);
        Map<String, Integer> order = Map.of("123", 3);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertEquals(30.0, summary.getTotalPrice());
        verify(process).buyBook(book, 3);
    }

    @Test
    @DisplayName("specification-based")
    void testGetPriceForCart_PartialAvailability() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        Book book = new Book("111", 15, 2);
        when(db.findByISBN("111")).thenReturn(book);

        BarnesAndNoble store = new BarnesAndNoble(db, process);
        Map<String, Integer> order = Map.of("111", 5);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertEquals(30.0, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().containsKey(book));
        assertEquals(3, summary.getUnavailable().get(book));
        verify(process).buyBook(book, 2);
    }

    @Test
    @DisplayName("structural-based")
    void test_NullOrder_NoInteraction() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);
        BarnesAndNoble store = new BarnesAndNoble(db, process);

        store.getPriceForCart(null);

        verifyNoInteractions(db, process);
    }

    @Test
    @DisplayName("structural-based")
    void test_MultipleBooks() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess process = mock(BuyBookProcess.class);

        Book b1 = new Book("101", 20, 10);
        Book b2 = new Book("202", 25, 10);
        when(db.findByISBN("101")).thenReturn(b1);
        when(db.findByISBN("202")).thenReturn(b2);

        BarnesAndNoble store = new BarnesAndNoble(db, process);
        Map<String, Integer> order = new HashMap<>();
        order.put("101", 1);
        order.put("202", 2);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertEquals(70.0, summary.getTotalPrice());
        verify(process).buyBook(b1, 1);
        verify(process).buyBook(b2, 2);
    }
}