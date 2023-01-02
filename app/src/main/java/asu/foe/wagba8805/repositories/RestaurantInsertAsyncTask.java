package asu.foe.wagba8805.repositories;

import android.os.AsyncTask;

import asu.foe.wagba8805.interfaces.daos.RestaurantDao;
import asu.foe.wagba8805.pojos.Restaurant;

public class RestaurantInsertAsyncTask extends AsyncTask<Restaurant, Void, Void> {

  private final RestaurantDao mAsyncTaskDao;

  RestaurantInsertAsyncTask(RestaurantDao dao) {
    mAsyncTaskDao = dao;
  }

  @Override
  protected Void doInBackground(final Restaurant... params) {
    mAsyncTaskDao.insert(params[0]);
    return null;
  }
}
