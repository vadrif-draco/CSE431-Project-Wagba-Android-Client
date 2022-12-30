package asu.foe.wagba8805.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import asu.foe.wagba8805.MyRoomDatabase;
import asu.foe.wagba8805.interfaces.daos.UserDao;
import asu.foe.wagba8805.pojos.User;

public class UserRepository {

  private final UserDao userDao;

  public UserRepository(Application application) {
    MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
    userDao = db.getUserDao();
  }

  public void insert(User user) {
    new UserInsertAsyncTask(userDao).execute(user);
  }

  public LiveData<List<User>> getUserByUUID(String uuid) {
    return userDao.getUserByUUID(uuid);
  }
}
