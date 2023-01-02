package asu.foe.wagba8805.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import asu.foe.wagba8805.pojos.Restaurant;
import asu.foe.wagba8805.repositories.RestaurantRepository;

public class RestaurantViewModel extends AndroidViewModel {

  private final RestaurantRepository restaurantRepository;

  public RestaurantViewModel(Application application) {
    super(application);
    restaurantRepository = new RestaurantRepository(application);
  }

  // updatedRestaurant should have the same id as the one to be updated, where the insert method replaces on id conflict
  public void updateRestaurantData(Restaurant updatedRestaurant) {
    restaurantRepository.insert(updatedRestaurant);
  }

  public LiveData<List<Restaurant>> getAllRestaurants() {
    return restaurantRepository.getAllRestaurants();
  }

  // Although I only need one restaurant, the architecture forces me to use a "list"
  public LiveData<List<Restaurant>> getRestaurantByID(int id) {
    return restaurantRepository.getRestaurantByID(id);
  }
}
