package asu.foe.wagba8805;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import asu.foe.wagba8805.databinding.ProfilePageBinding;

public class ProfilePageActivity extends AppCompatActivity {

  ProfilePageBinding ppBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    ppBinding = ProfilePageBinding.inflate(getLayoutInflater());
    setContentView(ppBinding.getRoot());
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}