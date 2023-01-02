package asu.foe.wagba8805.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.RestaurantsBinding;
import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.rvAdapters.RestaurantsItemListAdapter;
import asu.foe.wagba8805.services.FirebaseProfileService;
import asu.foe.wagba8805.viewmodels.RestaurantViewModel;
import asu.foe.wagba8805.viewmodels.UserViewModel;

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
    RestaurantViewModel restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    restaurantViewModel.getAllRestaurants().observe(this, restaurantsAdapter::set);
    restaurantsRV.setAdapter(restaurantsAdapter);

    UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    userViewModel.getUserByUUID(FirebaseProfileService.getUuid()).observe(this, users -> {

      User user = (users.size() > 0) ? users.get(0) : null;
      GlideUrl imgUrl = (user == null) ? FirebaseProfileService.getGlideUrl() : (user.imgUrl == null) ? null : new GlideUrl(user.imgUrl);
      Glide.with(this)
          .load(imgUrl)
          .thumbnail(Glide.with(this).load(R.drawable.user_placeholder))
          .centerCrop()
          .apply(RequestOptions.circleCropTransform())
          .into(restaurantsBinding.profileIcon);

    });

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      restaurantsRV.setLayoutManager(new GridLayoutManager(this, 2));
      restaurantsRV.setPadding(48, restaurantsRV.getPaddingTop(), 48, 0);
    } else {
      restaurantsRV.setLayoutManager(new GridLayoutManager(this, 1));
    }

    restaurantsBinding.profileIcon.setOnClickListener(v -> {
      startActivity(new Intent(this, MasterNavigationPageActivity.class));
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}