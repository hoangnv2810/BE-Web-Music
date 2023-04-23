package com.example.btlsoc.repository;

import com.example.btlsoc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select o from Order o, User u where o.user.id = ?1")
    Optional<List<Order>> findAllOrderByUserId(int user_id);

    @Query("select o from Order o where o.user.id = ?1 order by o.id desc ")
    Optional<Order> findOrder(int user_id);
    @Query("select o from Order o where o.id = ?1")
    Optional<Order> findByIdString(String order_id);

}
