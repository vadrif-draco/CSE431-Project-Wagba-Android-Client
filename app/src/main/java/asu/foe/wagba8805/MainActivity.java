package asu.foe.wagba8805;

import static asu.foe.wagba8805.Constants.DISABLE_LOGIN_BUTTONS;
import static asu.foe.wagba8805.Constants.ENABLE_LOGIN_BUTTONS;
import static asu.foe.wagba8805.Constants.FINISH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import asu.foe.wagba8805.activities.FacultyEmailLoginActivity;
import asu.foe.wagba8805.activities.PersonalEmailLoginActivity;
import asu.foe.wagba8805.activities.RestaurantsActivity;
import asu.foe.wagba8805.databinding.EntryBinding;

public class MainActivity extends AppCompatActivity {

  public EntryBinding entryBinding;

  private BroadcastReceiver mainActivityBR = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      switch (intent.getIntExtra("message", 0)) {
        case ENABLE_LOGIN_BUTTONS:
          enableLoginBtns();
          break;
        case DISABLE_LOGIN_BUTTONS:
          disableLoginBtns();
          break;
        case FINISH:
          finish();
          break;
        case 0:
        default:
          break;
      }
    }
  };

  private void disableLoginBtns() {
    entryBinding.personalEmailLoginBtn.setEnabled(false);
    entryBinding.facultyEmailLoginBtn.setEnabled(false);
    entryBinding.personalEmailLoginBtn.setAlpha(0.5f);
    entryBinding.facultyEmailLoginBtn.setAlpha(0.5f);
  }

  private void enableLoginBtns() {
    entryBinding.personalEmailLoginBtn.setEnabled(true);
    entryBinding.facultyEmailLoginBtn.setEnabled(true);
    entryBinding.personalEmailLoginBtn.setAlpha(1.f);
    entryBinding.facultyEmailLoginBtn.setAlpha(1.f);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if (user != null) {
      startActivity(new Intent(this, RestaurantsActivity.class));
      finish();
    }

    entryBinding = EntryBinding.inflate(getLayoutInflater());
    setContentView(entryBinding.getRoot());

    LocalBroadcastManager.getInstance(this).registerReceiver(mainActivityBR, new IntentFilter("toMainActivity"));

    entryBinding.facultyEmailLoginBtn.setOnClickListener(v -> {
      startActivity(new Intent(this, FacultyEmailLoginActivity.class));
    });

    entryBinding.personalEmailLoginBtn.setOnClickListener(v -> {
      startActivity(new Intent(this, PersonalEmailLoginActivity.class));
    });

  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  protected void onDestroy() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mainActivityBR);
    super.onDestroy();
  }
}