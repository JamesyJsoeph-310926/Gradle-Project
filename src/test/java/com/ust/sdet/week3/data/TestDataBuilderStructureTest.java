package com.ust.sdet.week3.data;

import static com.ust.sdet.week3.data.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TestDataBuilderStructureTest {

    @Test
    void builderUsesSensibleDefaults() {
        Order order = anOrder().build();
        assertEquals("SKU-RET-101", order.sku());
        assertEquals(1, order.quantity());
        assertEquals(129_900, order.totalPaise());
        assertEquals("NEW", order.status());
        assertEquals(LocalDate.of(2026, 7, 8), order.orderedOn());
        assertEquals(false, order.refunded());
    }

    @Test
    void builderStatesOnlyWhatTheTestCaresAbout() {
        Order order = anOrder().withQuantity(3).build();
        assertEquals(3, order.quantity());
        assertEquals("SKU-RET-101", order.sku(), "Unrelated fields come from defaults");
        assertEquals("NEW", order.status(), "Unrelated fields come from defaults");
    }

    @Test
    void builderSupportsNamedBusinessStatesWithoutFixtureDuplication() {
        Order order = anOrder().refunded().build();
        assertEquals("REFUNDED", order.status());
        assertEquals(true, order.refunded());
    }

    @Test
    void invalidBuilderDataFailsBeforeDatabaseSetup() {
        OrderBuilder builder = anOrder().withQuantity(0);
        assertThrows(IllegalArgumentException.class, builder::build);
    }
}