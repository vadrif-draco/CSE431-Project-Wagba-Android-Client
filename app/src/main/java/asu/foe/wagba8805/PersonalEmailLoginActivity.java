package asu.foe.wagba8805;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import asu.foe.wagba8805.databinding.PersonalEmailLoginBinding;

public class PersonalEmailLoginActivity extends AppCompatActivity {

  PersonalEmailLoginBinding pelBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    pelBinding = PersonalEmailLoginBinding.inflate(getLayoutInflater());
    setContentView(pelBinding.getRoot());

    pelBinding.registerHyperlink.setOnClickListener(v -> {
      Toast.makeText(this, "Registration is not allowed yet", Toast.LENGTH_SHORT).show();
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}