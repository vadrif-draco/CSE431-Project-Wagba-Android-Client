package asu.foe.wagba8805.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.ProfilePageBinding;
import asu.foe.wagba8805.services.AuthService;
import asu.foe.wagba8805.services.ProfileService;

public class ProfilePageActivity extends AppCompatActivity {

  ProfilePageBinding ppBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    ppBinding = ProfilePageBinding.inflate(getLayoutInflater());
    setContentView(ppBinding.getRoot());

    ppBinding.signOutBtn.setOnClickListener(v -> {
      AuthService.signOut();
      startActivity(new Intent(this, MainActivity.class));
      finishAffinity();
    });

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    UserInfo userInfo;
    if (user != null) {
      if (user.getProviderData().size() > 0) {
        // 1 stands for the actual provider after firebase, in our case google.com and microsoft.com
        userInfo = user.getProviderData().get(1);
      } else {
        // 0 stands for firebase itself
        userInfo = user.getProviderData().get(0);
      }
      ppBinding.username.setText(ProfileService.getDisplayName(userInfo));
      Glide.with(this)
          .load(ProfileService.getGlideUrl(userInfo))
          .thumbnail(Glide.with(this).load(R.drawable.user_placeholder))
          .centerCrop()
          .apply(RequestOptions.circleCropTransform())
          .into(ppBinding.img);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}