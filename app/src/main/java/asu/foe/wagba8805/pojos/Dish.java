package asu.foe.wagba8805.pojos;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
    tableName = "dish_table",
    indices = {
        @Index(value = {"restaurant_id"})
    },
    primaryKeys = {
        "id", "restaurant_id"
    },
    foreignKeys = {
        @ForeignKey(
            entity = Restaurant.class,
            parentColumns = "id",
            childColumns = "restaurant_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Dish {

  public int id;
  public int restaurant_id;
  public Float price;
  public Boolean available;
  public String name;
  public String description;
  public String imageUrl;
  public int inCartQuantity;

  public Dish() {
  }

  public int getId() {
    return id;
  }

  public int getRestaurant_id() {
    return restaurant_id;
  }

  public Float getPrice() {
    return price;
  }

  public Boolean isAvailable() {
    return available;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getInCartQuantity() {
    return inCartQuantity;
  }

  @Ignore
  public Dish(Integer id, Integer restaurant_id, Float price, Boolean available, String name, String description, String imageUrl, int inCartQuantity) {
    this.id = id == null ? 0 : id;
    this.restaurant_id = restaurant_id == null ? 0 : restaurant_id;
    this.price = price;
    this.available = available;
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
    this.inCartQuantity = inCartQuantity;
  }
}
