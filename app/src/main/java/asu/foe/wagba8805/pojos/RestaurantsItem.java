package asu.foe.wagba8805.pojos;

public class RestaurantsItem {
  public String name;
  public String address;
  public String imageUrl;

  public RestaurantsItem() {
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

  public RestaurantsItem(String name, String address, String imageUrl) {
    this.name = name;
    this.address = address;
    this.imageUrl = imageUrl;
  }
}
