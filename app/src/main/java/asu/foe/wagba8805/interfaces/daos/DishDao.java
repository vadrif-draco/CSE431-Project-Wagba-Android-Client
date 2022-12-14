package asu.foe.wagba8805.interfaces.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import asu.foe.wagba8805.pojos.Dish;

@Dao
public interface DishDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Dish dish);

  @Query("SELECT * FROM dish_table WHERE id = :dish_id AND restaurant_id = :restaurant_id")
  LiveData<List<Dish>> getDish(int dish_id, int restaurant_id);

  @Query("SELECT * FROM dish_table WHERE restaurant_id = :restaurant_id ORDER BY id ASC")
  LiveData<List<Dish>> getDishesByRestaurantId(int restaurant_id);

  @Query("SELECT * FROM dish_table WHERE inCartQuantity > 0 ORDER BY restaurant_id, id ASC")
  LiveData<List<Dish>> getDishesInCart();

  @Query("SELECT * FROM dish_table WHERE restaurant_id = :restaurant_id AND inCartQuantity > 0 ORDER BY id ASC")
  LiveData<List<Dish>> getDishesInCartForRestaurant(int restaurant_id);

}
