//package com.ust.sdet.week3.data;
//
//import org.flywaydb.core.Flyway;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import static com.ust.sdet.week3.data.OrderBuilder.anOrder;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@Testcontainers
//@Tag("integration")
//public class OrdersDataIT {
//
//    @Container
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
//                    .withDatabaseName("retail_test")
//                    .withUsername("jamesy")
//                    .withPassword("2026");
//
//    static OrderRepository repository;
//    static OrderFactory factory;
//
//    @BeforeAll
//    static void migrateSchema() {
//        Flyway.configure()
//                .dataSource(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword())
//                .locations("classpath:db/migration")
//                .load()
//                .migrate();
//
//        repository = new OrderRepository(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
//        factory = new OrderFactory(repository);
//    }
//
//    @BeforeEach
//    void resetMutableTables() {
//        repository.resetMutableTables();
//    }
//
//    @Test
//    void flywaySeedsReferenceDataButNoPerTestOrders() {
//        assertEquals(4, repository.referenceStatusCount(), "Reference statuses are seeded by migration");
//        assertEquals(0, repository.count(), "Per-test order rows should not come from migrations");
//    }
//
//    @Test
//    void factoryPersistsBuilderDataAgainstIsolatedMySql() {
//        long id = factory.persisted(OrderBuilder.anOrder().withQuantity(3));
//
//        assertTrue(id > 0);
//        assertEquals(1, repository.count());
//    }
//
//    @Test
//    void countsOnlyThisTestsOrders() {
//        factory.persisted(OrderBuilder.anOrder());
//        factory.persisted(OrderBuilder.anOrder().withSku("SKU-RET-202").withQuantity(2));
//
//        assertEquals(2, repository.count());
//    }
//
//    @Test
//    void resetMakesTestsOrderIndependent() {
//        assertEquals(0, repository.count(), "Previous tests must not leak rows into this test");
//
//        factory.persisted(anOrder().refunded());
//
//        assertEquals(1, repository.count());
//        assertEquals(1, repository.countByStatus("REFUNDED"));
//    }
//}

package com.ust.sdet.week3.data;

import io.qameta.allure.*;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.ust.sdet.week3.data.OrderBuilder.anOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Framework Hardening Features:
 * Testcontainers for isolated MySQL instances
 * Flyway for schema versioning
 * Builder Pattern for test data creation
 * Repository Pattern for DB access
 * Allure Reporting and Tracing
 * Test Isolation through table reset
 */
@Testcontainers
@Tag("integration")
@Epic("Framework Hardening")
@Feature("Test Data Management")
public class OrdersDataIT {

    // Dedicated MySQL container used for integration testing.
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
                    .withDatabaseName("retail_test")
                    .withUsername("jamesy")
                    .withPassword("2026");

    static OrderRepository repository;
    static OrderFactory factory;


    // Runs Flyway migrations before executing tests.
    @BeforeAll
    static void migrateSchema() {
        Allure.step("Execute Flyway database migrations");
        Flyway.configure().dataSource(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();

        repository = new OrderRepository(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
        factory = new OrderFactory(repository);
    }

    // Reset mutable tables before every test.
    // Ensures complete test isolation.
    @BeforeEach
    void resetMutableTables() {
        Allure.step("Reset mutable tables before test execution");
        repository.resetMutableTables();
    }

    @Test
    @Story("Flyway Seed Verification")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Flyway seeds reference data and test tables start empty")
    void flywaySeedsReferenceDataButNoPerTestOrders() {

        Allure.step("Verify reference status data");
        assertEquals(4, repository.referenceStatusCount(), "Reference statuses are seeded by migration");
        Allure.step("Verify no test orders exist");

        assertEquals(0, repository.count(), "Per-test order rows should not come from migrations");

    }

    @Test
    @Story("Order Creation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify builder data can be persisted into isolated MySQL database")
    void factoryPersistsBuilderDataAgainstIsolatedMySql() {

        Allure.step("Create test order");
        long id = factory.persisted(anOrder().withQuantity(3));
        Allure.step("Verify generated order id");

        assertTrue(id > 0);
        Allure.step("Verify order count");
        assertEquals(1, repository.count());

    }

    @Test
    @Story("Order Counting")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify repository counts only orders created by current test")
    void countsOnlyThisTestsOrders() {

        Allure.step("Create first order");

        factory.persisted(anOrder());
        Allure.step("Create second order");

        factory.persisted(anOrder().withSku("SKU-RET-202").withQuantity(2));

        Allure.step("Verify total order count");
        assertEquals(2, repository.count());

    }

    @Test
    @Story("Test Isolation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify test data does not leak between tests")
    void resetMakesTestsOrderIndependent() {

        Allure.step("Verify clean database state");
        assertEquals(0, repository.count(), "Previous tests must not leak rows into this test");
        Allure.step("Create refunded order");
        factory.persisted(anOrder().refunded());
        Allure.step("Verify refunded order");
        assertEquals(1, repository.count());
        assertEquals(1, repository.countByStatus("REFUNDED"));
    }
}
