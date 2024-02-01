package com.hasitha.springboottesting.repository;

import com.hasitha.springboottesting.model.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderRepositoryUnitTest {

    /*
    * I have autowired the repository object
    * without mocking — not an issue because
    * we are not using the real database.
    * So, no real data will be saved even the real
    * repository methods are called!
    * */
    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.save(new Order(100L, "jane", 200.0, 2));
        orderRepository.save(new Order(200L, "ben", 100.0, 5));
    }

    @AfterEach
    public void destroy() {
        orderRepository.deleteAll();
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList.size()).isEqualTo(2);
        assertThat(orderList.get(0).getId()).isNotNegative();
        assertThat(orderList.get(0).getId()).isGreaterThan(0);
        assertThat(orderList.get(0).getBuyer()).isEqualTo("jane");
    }

    @Test
    public void testGetInvalidOrder() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            orderRepository.findById(120L).get();
        });
        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(NoSuchElementException.class);
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Test
    public void testCreateOrder() {
        Order saved = new Order(300L, "tim", 50.0, 4);
        Order returned = orderRepository.save(saved);
        assertThat(returned).isNotNull();
        assertThat(returned.getBuyer()).isNotEmpty();
        assertThat(returned.getId()).isGreaterThan(1);
        assertThat(returned.getId()).isNotNegative();
        assertThat(saved.getBuyer()).isEqualTo(returned.getBuyer());
    }

    @Test
    public void testDeleteOrder() {
        Order saved = new Order(400L, "ron", 60.0, 3);
        orderRepository.save(saved);
        orderRepository.delete(saved);
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            orderRepository.findById(400L).get();
        });
        assertThat(exception).isNotNull();
        assertThat(exception.getClass()).isEqualTo(NoSuchElementException.class);
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }
}
