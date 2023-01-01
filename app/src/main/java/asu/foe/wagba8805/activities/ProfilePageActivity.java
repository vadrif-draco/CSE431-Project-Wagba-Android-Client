package asu.foe.wagba8805.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;

import java.util.Arrays;
import java.util.List;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.ProfilePageBinding;
import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.services.FirebaseAuthService;
import asu.foe.wagba8805.services.FirebaseProfileService;
import asu.foe.wagba8805.viewmodels.UserViewModel;

public class ProfilePageActivity extends AppCompatActivity {

  ProfilePageBinding ppBinding;
  UserViewModel userViewModel;
  Boolean editing = false;
  List<Boolean> genderSpinnerBooleanList = Arrays.asList(true, false, null);
  List<String> genderSpinnerStringList = Arrays.asList("Male", "Female", "Gender Unspecified");
  ListPopupWindow genderListPopupWindow;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    ppBinding = ProfilePageBinding.inflate(getLayoutInflater());
    setContentView(ppBinding.getRoot());

    Intent isNewUserIntent = getIntent();
    if (isNewUserIntent.getBooleanExtra("isNewUser", false)) {
      ppBinding.signOutBtn.setVisibility(View.GONE);
      ppBinding.proceedToAppBtn.setVisibility(View.VISIBLE);
      new AlertDialog.Builder(this)
          .setCancelable(false)
          .setTitle("Welcome to Wagba!")
          .setMessage("Hi there!\n\n" +
              "Since this is your first sign-in, an account has been created for you, " +
              "with some details pre-filled based on your public profile from your sign-in provider of choice. " +
              "Please feel free to customize any of these details, and proceed at any time to the app content.\n\n" +
              "You can always change your profile information later from the top-right profile icon.")
          .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss())
          .create()
          .show();
    }

    ppBinding.proceedToAppBtn.setOnClickListener(v -> {
      startActivity(new Intent(this, RestaurantsActivity.class));
      finishAffinity();
    });

    ppBinding.signOutBtn.setOnClickListener(v -> {
      FirebaseAuthService.signOut();
      startActivity(new Intent(this, MainActivity.class));
      finishAffinity();
    });

    ppBinding.profilePicChangeBtn.setOnClickListener(v -> {
      startActivity(new Intent(this, ProfilePicChangeActivity.class));
    });

    ppBinding.displayNameEditBtn.setOnClickListener(v -> startEditDisplayName());

    ppBinding.displayNameEditCancelBtn.setOnClickListener(v -> endEditDisplayName());

    ppBinding.displayNameEditConfirmBtn.setOnClickListener(v -> {

      LiveData<List<User>> l = userViewModel.getUserByUUID(FirebaseProfileService.getUuid());
      Observer<List<User>> o = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
          User user = users.get(0);
          user.displayName = ppBinding.displayNameET.getText().toString();
          userViewModel.updateUserData(user);
          l.removeObserver(this);
        }
      };
      l.observeForever(o);

      endEditDisplayName();
    });

    genderListPopupWindow = new ListPopupWindow(this);
    ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genderSpinnerStringList);
    genderListPopupWindow.setAdapter(aa);
    genderListPopupWindow.setAnchorView(ppBinding.genderArea);
    genderListPopupWindow.setModal(true);
    genderListPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT);
    genderListPopupWindow.setOnItemClickListener((adapterView, view, i, l_) -> {

      LiveData<List<User>> l = userViewModel.getUserByUUID(FirebaseProfileService.getUuid());
      Observer<List<User>> o = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
          User user = users.get(0);
          user.isMale = genderSpinnerBooleanList.get(i);
          userViewModel.updateUserData(user);
          l.removeObserver(this);
        }
      };
      l.observeForever(o);

      genderListPopupWindow.dismiss();
    });

    ppBinding.genderEditBtn.setOnClickListener(v -> genderListPopupWindow.show());

    userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    userViewModel.getUserByUUID(FirebaseProfileService.getUuid()).observe(this, users -> {

      // There is only ever one user (or null) returned from this query
      User user = (users.size() > 0) ? users.get(0) : null;

      GlideUrl imgUrl = (user == null) ? FirebaseProfileService.getGlideUrl() : (user.imgUrl == null) ? null : new GlideUrl(user.imgUrl);
      String displayName = (user == null) ? FirebaseProfileService.getDisplayName() : user.displayName;
      String email = (user == null) ? FirebaseProfileService.getEmail() : user.email;
      Boolean isMale = (user == null) ? null : user.isMale; // Null means unspecified

      Glide.with(this)
          .load(imgUrl)
          .thumbnail(Glide.with(this).load(R.drawable.user_placeholder))
          .centerCrop()
          .apply(RequestOptions.circleCropTransform())
          .into(ppBinding.img);

      if (displayName != null && !displayName.isEmpty()) {
        ppBinding.displayNameTV.setText(displayName);
      }
      ppBinding.email.setText(email);
      ppBinding.genderTV.setText(genderSpinnerStringList.get(genderSpinnerBooleanList.indexOf(isMale)));

    });
  }

  private void startEditDisplayName() {
    editing = true;

    ppBinding.displayNameTV.setVisibility(View.GONE);
    ppBinding.displayNameEditBtn.setVisibility(View.GONE);

    ppBinding.displayNameEditCancelBtn.setVisibility(View.VISIBLE);
    ppBinding.displayNameEditConfirmBtn.setVisibility(View.VISIBLE);
    ppBinding.displayNameET.setText(ppBinding.displayNameTV.getText());
    ppBinding.displayNameET.setVisibility(View.VISIBLE);
    ppBinding.displayNameET.requestFocus();
    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    new Handler().postDelayed(
        () -> ppBinding.scrollview.smoothScrollTo(0, ppBinding.scrollview.getBottom()),
        1000
    );
  }

  private void endEditDisplayName() {
    editing = false;

    ppBinding.displayNameEditCancelBtn.setVisibility(View.GONE);
    ppBinding.displayNameEditConfirmBtn.setVisibility(View.GONE);
    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
        .hideSoftInputFromWindow(ppBinding.displayNameET.getWindowToken(), 0);
    ppBinding.displayNameET.clearFocus();
    ppBinding.displayNameET.setVisibility(View.GONE);

    ppBinding.displayNameTV.setVisibility(View.VISIBLE);
    ppBinding.displayNameEditBtn.setVisibility(View.VISIBLE);
  }

  @Override
  public void onBackPressed() {
    if (editing) endEditDisplayName();
    else super.onBackPressed();
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}