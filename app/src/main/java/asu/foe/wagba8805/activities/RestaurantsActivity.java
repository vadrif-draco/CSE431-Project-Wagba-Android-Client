package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.TAG;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.RestaurantsBinding;
import asu.foe.wagba8805.pojos.RestaurantsItem;
import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.rvAdapters.RestaurantsItemListAdapter;
import asu.foe.wagba8805.services.FirebaseProfileService;
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
    restaurantsAdapter.set(new RestaurantsItem[0]);

    // TODO: Find a better place for this
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("restaurants");
    myRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<RestaurantsItem> items = new ArrayList<>();
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
          items.add(childSnapshot.getValue(RestaurantsItem.class));
        }
        restaurantsAdapter.set(items.toArray(new RestaurantsItem[0]));
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
      }
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
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}