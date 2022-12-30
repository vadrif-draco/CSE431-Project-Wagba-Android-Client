package asu.foe.wagba8805;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import asu.foe.wagba8805.interfaces.daos.UserDao;
import asu.foe.wagba8805.pojos.User;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {

  private static volatile MyRoomDatabase INSTANCE;

  public static MyRoomDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (MyRoomDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context.getApplicationContext(), MyRoomDatabase.class, "my_room_database")
                  .fallbackToDestructiveMigration()
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract UserDao getUserDao();

}
