package asu.foe.wagba8805.pojos;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurant_table")
public class Restaurant {

  @PrimaryKey
  public int id;

  public String name;
  public String address;
  public String imageUrl;

  public Restaurant() {
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Integer getId() {
    return id;
  }

  @Ignore
  public Restaurant(Integer id, String name, String address, String imageUrl) {
    this.id = id == null ? 0 : id;
    this.name = name;
    this.address = address;
    this.imageUrl = imageUrl;
  }
}
