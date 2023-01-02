package asu.foe.wagba8805.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import asu.foe.wagba8805.MyRoomDatabase;
import asu.foe.wagba8805.interfaces.daos.DishDao;
import asu.foe.wagba8805.pojos.Dish;

public class DishRepository {

  private final DishDao dishDao;

  public DishRepository(Application application) {
    MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
    dishDao = db.getDishDao();
  }

  public void insert(Dish dish) {
    new DishInsertAsyncTask(dishDao).execute(dish);
  }

  public LiveData<List<Dish>> getDish(int dish_id, int restaurant_id) {
    return dishDao.getDish(dish_id, restaurant_id);
  }

  public LiveData<List<Dish>> getDishesByRestaurantId(int restaurant_id) {
    return dishDao.getDishesByRestaurantId(restaurant_id);
  }
}
