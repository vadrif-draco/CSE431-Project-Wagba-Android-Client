package asu.foe.wagba8805.interfaces.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import asu.foe.wagba8805.pojos.Restaurant;

@Dao
public interface RestaurantDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(Restaurant restaurant);

  @Query("SELECT * FROM restaurant_table")
  LiveData<List<Restaurant>> getAllRestaurants();

  @Query("SELECT * FROM restaurant_table WHERE id = :id ORDER BY id ASC")
  LiveData<List<Restaurant>> getRestaurantByID(int id);

}
