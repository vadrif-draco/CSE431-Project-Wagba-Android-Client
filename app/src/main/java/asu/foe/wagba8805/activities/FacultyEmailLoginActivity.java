package asu.foe.wagba8805.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import asu.foe.wagba8805.R;

public class FacultyEmailLoginActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    setContentView(R.layout.faculty_email_login);
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}