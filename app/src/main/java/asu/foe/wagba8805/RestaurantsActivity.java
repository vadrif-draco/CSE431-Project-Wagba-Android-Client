package asu.foe.wagba8805;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import asu.foe.wagba8805.databinding.RestaurantsBinding;
import asu.foe.wagba8805.pojos.RestaurantsItem;
import asu.foe.wagba8805.rvAdapters.RestaurantsItemListAdapter;

public class RestaurantsActivity extends AppCompatActivity {

  RestaurantsBinding restaurantsBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    restaurantsBinding = RestaurantsBinding.inflate(getLayoutInflater());
    setContentView(restaurantsBinding.getRoot());

    RecyclerView restaurantsRV = restaurantsBinding.restaurantsRV;
    RestaurantsItemListAdapter restaurantsAdapter = new RestaurantsItemListAdapter(this);
    restaurantsAdapter.set(new RestaurantsItem[]{
        new RestaurantsItem("Some restaurant", "Some address", "https://www.osiristours.com/wp-content/uploads/2018/03/Four-Seasons-Nile-Plaza-1-1024x720-1.jpg"),
        new RestaurantsItem("Nagaf Restaurants", "91 Abbassia St., Abdo Pasha Sq.", "https://i.imgur.com/MIh5P37.jpeg"),
        new RestaurantsItem("Some restaurant", "Some address", "some.image.url"),
        new RestaurantsItem("Some restaurant", "Some address", "some.image.url"),
        new RestaurantsItem("Some restaurant", "Some address", "some.image.url"),
    });
    restaurantsRV.setAdapter(restaurantsAdapter);
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      restaurantsRV.setLayoutManager(new GridLayoutManager(this, 2));
      restaurantsRV.setPadding(
          48,
          restaurantsRV.getPaddingTop(),
          48,
          0
      );
    } else {
      restaurantsRV.setLayoutManager(new GridLayoutManager(this, 1));
    }

    restaurantsBinding.profileIcon.setOnClickListener(v -> {
      startActivity(new Intent(this, ProfilePageActivity.class));
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}