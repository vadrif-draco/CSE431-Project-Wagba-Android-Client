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
import asu.foe.wagba8805.databinding.DishesBinding;
import asu.foe.wagba8805.pojos.DishesItem;
import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.rvAdapters.DishesItemListAdapter;
import asu.foe.wagba8805.services.FirebaseProfileService;
import asu.foe.wagba8805.viewmodels.UserViewModel;

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

    UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    userViewModel.getUserByUUID(FirebaseProfileService.getUuid()).observe(this, users -> {

      User user = (users.size() > 0) ? users.get(0) : null;
      GlideUrl imgUrl = (user == null) ? FirebaseProfileService.getGlideUrl() : (user.imgUrl == null) ? null : new GlideUrl(user.imgUrl);
      Glide.with(this)
          .load(imgUrl)
          .thumbnail(Glide.with(this).load(R.drawable.user_placeholder))
          .centerCrop()
          .apply(RequestOptions.circleCropTransform())
          .into(dishesBinding.profileIcon);

    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}