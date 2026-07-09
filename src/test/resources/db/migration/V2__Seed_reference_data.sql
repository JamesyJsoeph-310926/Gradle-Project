INSERT INTO order_statuses(code, description) VALUES
('NEW', 'Order created and waiting processing'),
('PAID', 'Payment captured successfully'),
('SHIPPED', 'Order handed to shipping partner'),
('REFUNDED', 'Refund completed for the order');