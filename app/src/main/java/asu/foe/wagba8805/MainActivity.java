package asu.foe.wagba8805;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import asu.foe.wagba8805.databinding.EntryBinding;

public class MainActivity extends AppCompatActivity {

  EntryBinding entryBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    entryBinding = EntryBinding.inflate(getLayoutInflater());
    setContentView(entryBinding.getRoot());

    entryBinding.facultyEmailLoginBtn.setOnClickListener(v -> {
      startActivity(new Intent(this, FacultyEmailLoginActivity.class));
      // finish();
    });

    entryBinding.personalEmailLoginBtn.setOnClickListener(v -> {
      startActivity(new Intent(this, PersonalEmailLoginActivity.class));
      // finish();
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}