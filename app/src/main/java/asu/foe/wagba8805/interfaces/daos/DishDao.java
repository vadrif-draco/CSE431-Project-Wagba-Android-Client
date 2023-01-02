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

  @Query("SELECT * FROM dish_table WHERE id = :id")
  LiveData<List<Dish>> getDishByID(int id);

  @Query("SELECT * FROM dish_table WHERE restaurant_id = :restaurant_id")
  LiveData<List<Dish>> getDishesByRestaurantId(int restaurant_id);

}
