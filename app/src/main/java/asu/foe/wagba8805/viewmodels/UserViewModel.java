package asu.foe.wagba8805.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

  private final UserRepository userRepository;

  public UserViewModel(Application application) {
    super(application);
    userRepository = new UserRepository(application);
  }

  // updatedUser should have the same UUID as the user to be updated, where the insert method replaces on UUID conflict
  public void updateUserData(User updatedUser) {
    userRepository.insert(updatedUser);
  }

  // Although I only need one user, the architecture forces me to use a "list"
  public LiveData<List<User>> getUserByUUID(String uuid) {
    return userRepository.getUserByUUID(uuid);
  }

}
