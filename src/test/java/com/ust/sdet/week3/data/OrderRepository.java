//package com.ust.sdet.week3.data;
//
//import com.ust.sdet.week3.data.model.Order;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class OrderRepository {
//
//    private final String jdbcUrl;
//    private final String username;
//    private final String password;
//
//    public OrderRepository(String jdbcUrl, String username, String password) {
//        this.jdbcUrl = jdbcUrl;
//        this.username = username;
//        this.password = password;
//    }
//
//    public long save(Order order) {
//        String orderSql = """
//                INSERT INTO retail_orders(status, total_paise, ordered_on, refunded)
//                VALUES (?, ?, ?, ?)
//                """;
//
//        String itemSql = """
//                INSERT INTO retail_order_items(order_id, sku, quantity)
//                VALUES (?, ?, ?)
//                """;
//
//        try (Connection connection = connection()) {
//            connection.setAutoCommit(false);
//
//            try {
//                long orderId;
//
//                try (PreparedStatement orderStatement =
//                             connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
//
//                    orderStatement.setString(1, order.status());
//                    orderStatement.setLong(2, order.totalPaise());
//                    orderStatement.setDate(3, Date.valueOf(order.orderedOn()));
//                    orderStatement.setBoolean(4, order.refunded());
//
//                    orderStatement.executeUpdate();
//                    orderId = generatedId(orderStatement);
//                }
//
//                try (PreparedStatement itemStatement = connection.prepareStatement(itemSql)) {
//                    itemStatement.setLong(1, orderId);
//                    itemStatement.setString(2, order.sku());
//                    itemStatement.setInt(3, order.quantity());
//
//                    itemStatement.executeUpdate();
//                }
//
//                connection.commit();
//                return orderId;
//
//            } catch (SQLException e) {
//                connection.rollback();
//                throw new IllegalStateException("Could not save order test data", e);
//            }
//
//        } catch (SQLException e) {
//            throw new IllegalStateException("Could not save order test data", e);
//        }
//    }
//
//    public int count() {
//        return queryForInt("SELECT COUNT(*) FROM retail_orders");
//    }
//
//    public int countByStatus(String status) {
//        String sql = "SELECT COUNT(*) FROM retail_orders WHERE status = ?";
//
//        try (Connection connection = connection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setString(1, status);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                resultSet.next();
//                return resultSet.getInt(1);
//            }
//
//        } catch (SQLException e) {
//            throw new IllegalStateException("Could not count orders by status", e);
//        }
//    }
//
//    public int referenceStatusCount() {
//        return queryForInt("SELECT COUNT(*) FROM order_statuses");
//    }
//
//    public void resetMutableTables() {
//        try (Connection connection = connection();
//             Statement statement = connection.createStatement()) {
//
//            statement.execute("SET FOREIGN_KEY_CHECKS = 0");
//            statement.execute("TRUNCATE TABLE retail_order_items");
//            statement.execute("TRUNCATE TABLE retail_orders");
//            statement.execute("SET FOREIGN_KEY_CHECKS = 1");
//
//        } catch (SQLException e) {
//            throw new IllegalStateException("Could not reset order test data", e);
//        }
//    }
//
//    private int queryForInt(String sql) {
//        try (Connection connection = connection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(sql)) {
//
//            resultSet.next();
//            return resultSet.getInt(1);
//
//        } catch (SQLException e) {
//            throw new IllegalStateException("Could not run count query", e);
//        }
//    }
//
//    private long generatedId(PreparedStatement statement) throws SQLException {
//        try (ResultSet keys = statement.getGeneratedKeys()) {
//            if (!keys.next()) {
//                throw new SQLException("Insert did not return a generated id");
//            }
//            return keys.getLong(1);
//        }
//    }
//
//    private Connection connection() throws SQLException {
//        return DriverManager.getConnection(jdbcUrl, username, password);
//    }
//}

package com.ust.sdet.week3.data;

import com.ust.sdet.week3.data.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Responsibilities:
 * Persist test orders
 * Query order data
 * Reset mutable test data
 * Support isolated integration testing
 * This class keeps SQL logic separate from test classes,
 * improving maintainability and reusability.
 */
public class OrderRepository {

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public OrderRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * Saves an order and its related item.
     * Both inserts are executed inside a transaction.
     * @param order order test data
     * @return generated order id
     */
    public long save(Order order) {
        logger.info("Creating order. SKU={}, Quantity={}, Status={}", order.sku(), order.quantity(), order.status());

        String orderSql = """
        INSERT INTO retail_orders(status, total_paise, ordered_on, refunded)
        VALUES (?, ?, ?, ?)
        """;

        String itemSql = """
        INSERT INTO retail_order_items(order_id, sku, quantity)
        VALUES (?, ?, ?)
        """;

        try (Connection connection = connection()) {
            connection.setAutoCommit(false);

            try {
                long orderId;
                try (PreparedStatement orderStatement =
                             connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {

                    orderStatement.setString(1, order.status());
                    orderStatement.setLong(2, order.totalPaise());
                    orderStatement.setDate(3, Date.valueOf(order.orderedOn()));
                    orderStatement.setBoolean(4, order.refunded());

                    orderStatement.executeUpdate();

                    orderId = generatedId(orderStatement);

                    logger.info("Order record created successfully. OrderId={}", orderId);
                }

                try (PreparedStatement itemStatement = connection.prepareStatement(itemSql)) {

                    itemStatement.setLong(1, orderId);
                    itemStatement.setString(2, order.sku());
                    itemStatement.setInt(3, order.quantity());

                    itemStatement.executeUpdate();

                    logger.info("Order item inserted successfully for OrderId={}", orderId);
                }

                connection.commit();

                logger.info("Transaction committed successfully for OrderId={}", orderId);
                return orderId;
            } catch (SQLException e) {
                logger.error("Error occurred while saving order. Rolling back transaction.", e);
                connection.rollback();

                throw new IllegalStateException("Could not save order test data", e);
            }

        } catch (SQLException e) {
            logger.error("Database connection failure", e);
            throw new IllegalStateException("Could not save order test data", e);
        }
    }

    /**
     * Returns total order count.
     * @return total orders
     */
    public int count() {
        logger.info("Counting all orders");
        return queryForInt("SELECT COUNT(*) FROM retail_orders");
    }

    /**
     * Returns count of orders matching a status.
     * @param status order status
     * @return matching order count
     */
    public int countByStatus(String status) {
        logger.info("Counting orders with status={}", status);
        String sql = "SELECT COUNT(*) FROM retail_orders WHERE status = ?";

        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Failed to count orders by status", e);
            throw new IllegalStateException("Could not count orders by status", e);
        }
    }

    /**
     * Returns count of seeded reference statuses.
     * @return status count
     */
    public int referenceStatusCount() {

        logger.info("Counting seeded order statuses");
        return queryForInt("SELECT COUNT(*) FROM order_statuses");
    }

    /**
     * Removes mutable test data before test execution.
     * Ensures test isolation and prevents data leakage
     * between tests.
     */
    public void resetMutableTables() {

        logger.info("Resetting mutable database tables");

        try (Connection connection = connection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET FOREIGN_KEY_CHECKS = 0");
            statement.execute("TRUNCATE TABLE retail_order_items");
            statement.execute("TRUNCATE TABLE retail_orders");
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            logger.info("Database tables reset completed");
        } catch (SQLException e) {
            logger.error("Failed to reset database tables", e);
            throw new IllegalStateException("Could not reset order test data", e);

        }
    }

    /**
     * Executes a COUNT query and returns result.
     */
    private int queryForInt(String sql) {

        try (Connection connection = connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            resultSet.next();

            return resultSet.getInt(1);
        } catch (SQLException e) {

            logger.error("Failed to execute count query", e);
            throw new IllegalStateException("Could not run count query", e);

        }
    }


    // Retrieves generated primary key from insert statement.
    private long generatedId(PreparedStatement statement) throws SQLException {
        try (ResultSet keys = statement.getGeneratedKeys()) {

            if (!keys.next()) {
                throw new SQLException("Insert did not return a generated id");
            }

            return keys.getLong(1);
        }
    }

     // Creates a database connection.
    private Connection connection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
