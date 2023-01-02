package asu.foe.wagba8805.repositories;

import android.os.AsyncTask;

import asu.foe.wagba8805.interfaces.daos.DishDao;
import asu.foe.wagba8805.pojos.Dish;

public class DishInsertAsyncTask extends AsyncTask<Dish, Void, Void> {

  private final DishDao mAsyncTaskDao;

  DishInsertAsyncTask(DishDao dao) {
    mAsyncTaskDao = dao;
  }

  @Override
  protected Void doInBackground(final Dish... params) {
    mAsyncTaskDao.insert(params[0]);
    return null;
  }
}
