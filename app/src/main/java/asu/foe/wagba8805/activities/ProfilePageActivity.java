package asu.foe.wagba8805.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.ProfilePageBinding;
import asu.foe.wagba8805.services.AuthService;

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
    if (user != null) {
      ppBinding.username.setText(user.getDisplayName());

      // FIXME: This is a little hacky... Microsoft provider returns null on getPhotoUrl
      // So here I check on that and manually retrieve the picture using their Graph API
      // The hacky part is that check, as well as the get(1) part...
      Uri photoUrl = user.getPhotoUrl();
      GlideUrl glidePhotoUrl;
      if (photoUrl != null) {
        glidePhotoUrl = new GlideUrl(String.valueOf(photoUrl));
      } else {
        glidePhotoUrl =
            new GlideUrl(
                "https://graph.microsoft.com/v1.0/users/" + user.getProviderData().get(1).getUid() + "/photo/$value",
                new LazyHeaders.Builder().addHeader("Authorization", AuthService.accessToken).build()
            );
      }
      Glide.with(this)
          .load(glidePhotoUrl)
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