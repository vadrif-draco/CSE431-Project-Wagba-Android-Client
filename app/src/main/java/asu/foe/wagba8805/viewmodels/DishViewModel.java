package asu.foe.wagba8805.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import asu.foe.wagba8805.pojos.Dish;
import asu.foe.wagba8805.repositories.DishRepository;

public class DishViewModel extends AndroidViewModel {

  private final DishRepository dishRepository;

  public DishViewModel(Application application) {
    super(application);
    dishRepository = new DishRepository(application);
  }

  // updatedDish should have the same id as the one to be updated, where the insert method replaces on id conflict
  public void updateDishData(Dish updatedDish) {
    dishRepository.insert(updatedDish);
  }

  // Although I only need one Dish, the architecture forces me to use a "list"
  public LiveData<List<Dish>> getDish(int dish_id, int restaurant_id) {
    return dishRepository.getDish(dish_id, restaurant_id);
  }

  public LiveData<List<Dish>> getDishesByRestaurantID(int restaurant_id) {
    return dishRepository.getDishesByRestaurantId(restaurant_id);
  }

  public LiveData<List<Dish>> getDishesInCart() {
    return dishRepository.getDishesInCart();
  }

  public LiveData<List<Dish>> getDishesInCartForRestaurant(int restaurant_id) {
    return dishRepository.getDishesInCartForRestaurant(restaurant_id);
  }
}
