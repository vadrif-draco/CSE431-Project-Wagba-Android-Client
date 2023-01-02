package asu.foe.wagba8805.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import asu.foe.wagba8805.MyRoomDatabase;
import asu.foe.wagba8805.interfaces.daos.RestaurantDao;
import asu.foe.wagba8805.pojos.Restaurant;

public class RestaurantRepository {

  private final RestaurantDao restaurantDao;

  public RestaurantRepository(Application application) {
    MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
    restaurantDao = db.getRestaurantDao();
  }

  public void insert(Restaurant restaurant) {
    new RestaurantInsertAsyncTask(restaurantDao).execute(restaurant);
  }

  public LiveData<List<Restaurant>> getAllRestaurants() {
    return restaurantDao.getAllRestaurants();
  }

  public LiveData<List<Restaurant>> getRestaurantByID(int id) {
    return restaurantDao.getRestaurantByID(id);
  }
}
