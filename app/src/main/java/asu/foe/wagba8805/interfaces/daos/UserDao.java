package asu.foe.wagba8805.interfaces.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import asu.foe.wagba8805.pojos.User;

@Dao
public interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(User user);

  @Query("SELECT * FROM user_table WHERE uuid = :uuid")
  LiveData<List<User>> getUserByUUID(String uuid);

}
