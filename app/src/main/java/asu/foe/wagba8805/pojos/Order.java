package asu.foe.wagba8805.pojos;

import androidx.room.Embedded;
import androidx.room.Entity;

import java.util.List;

@Entity(tableName = "order_table")
public class Order {
  public String order_id;
  public int restaurant_id;
  public int order_status;
  @Embedded
  public List<Dish> dishes;

  public String getOrder_id() {
    return order_id;
  }

  public int getRestaurant_id() {
    return restaurant_id;
  }

  public int getOrder_status() {
    return order_status;
  }

  public List<Dish> getDishes() {
    return dishes;
  }

  public Order() {
  }

  public Order(String order_id, int restaurant_id, int order_status, List<Dish> dishes) {
    this.order_id = order_id;
    this.restaurant_id = restaurant_id;
    this.order_status = order_status;
    this.dishes = dishes;
  }
}
