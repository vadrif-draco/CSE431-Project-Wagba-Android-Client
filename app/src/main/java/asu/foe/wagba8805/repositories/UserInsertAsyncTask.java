package asu.foe.wagba8805.repositories;

import android.os.AsyncTask;

import asu.foe.wagba8805.interfaces.daos.UserDao;
import asu.foe.wagba8805.pojos.User;

class UserInsertAsyncTask extends AsyncTask<User, Void, Void> {

  private final UserDao mAsyncTaskDao;

  UserInsertAsyncTask(UserDao dao) {
    mAsyncTaskDao = dao;
  }

  @Override
  protected Void doInBackground(final User... params) {
    mAsyncTaskDao.insert(params[0]);
    return null;
  }
}
