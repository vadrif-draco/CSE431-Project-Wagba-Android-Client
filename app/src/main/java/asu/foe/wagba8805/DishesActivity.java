package asu.foe.wagba8805;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import asu.foe.wagba8805.databinding.DishesBinding;
import asu.foe.wagba8805.pojos.DishesItem;
import asu.foe.wagba8805.rvAdapters.DishesItemListAdapter;

public class DishesActivity extends AppCompatActivity {

  DishesBinding dishesBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    dishesBinding = DishesBinding.inflate(getLayoutInflater());
    setContentView(dishesBinding.getRoot());

    RecyclerView dishesRV = dishesBinding.dishesRV;
    DishesItemListAdapter dishesAdapter = new DishesItemListAdapter(this);
    dishesAdapter.set(new DishesItem[]{
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
        new DishesItem("Some dish", "Some description", "some.image.url"),
    });
    dishesRV.setAdapter(dishesAdapter);
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      dishesRV.setLayoutManager(new GridLayoutManager(this, 2));
      dishesRV.setPadding(
          48,
          dishesRV.getPaddingTop(),
          48,
          0
      );
    } else {
      dishesRV.setLayoutManager(new GridLayoutManager(this, 1));
    }

    dishesBinding.profileIcon.setOnClickListener(v -> {
      startActivity(new Intent(this, ProfilePageActivity.class));
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}