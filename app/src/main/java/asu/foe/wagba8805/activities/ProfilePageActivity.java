package asu.foe.wagba8805.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.ProfilePageBinding;
import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.services.FirebaseAuthService;
import asu.foe.wagba8805.services.FirebaseProfileService;
import asu.foe.wagba8805.viewmodels.UserViewModel;

public class ProfilePageActivity extends AppCompatActivity {

  ProfilePageBinding ppBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    ppBinding = ProfilePageBinding.inflate(getLayoutInflater());
    setContentView(ppBinding.getRoot());

    ppBinding.signOutBtn.setOnClickListener(v -> {
      FirebaseAuthService.signOut();
      startActivity(new Intent(this, MainActivity.class));
      finishAffinity();
    });

    // TODO: Place admin as user with uuid 1 in roomDB; insert if not already exists (to simulate persistence)!!
    UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    userViewModel.getUserByUUID(FirebaseProfileService.getUuid()).observe(this, users -> {

      // There is only ever one user (or null) returned from this query
      User user = (users.size() > 0) ? users.get(0) : null;

      // TODO: For the following to work I need to ensure that in case of first sign in, roomDB gets updated
      GlideUrl imgUrl = (user == null) ? FirebaseProfileService.getGlideUrl() : (user.imgUrl == null) ? null : new GlideUrl(user.imgUrl);
      String displayName = (user == null) ? FirebaseProfileService.getDisplayName() : user.displayName;
      String email = (user == null) ? FirebaseProfileService.getEmail() : user.email;
      Boolean isMale = (user == null) ? null : user.isMale; // Null means unspecified

      // FIXME: Admin login shows no pic (not even placeholder)
      Glide.with(this)
          .load(imgUrl)
          .thumbnail(Glide.with(this).load(R.drawable.user_placeholder))
          .centerCrop()
          .apply(RequestOptions.circleCropTransform())
          .into(ppBinding.img);

      ppBinding.username.setText(displayName);
      // TODO: ppBinding.gender
      // TODO: ppBinding.email

    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}