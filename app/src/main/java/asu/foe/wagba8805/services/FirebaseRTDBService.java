package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import asu.foe.wagba8805.pojos.Dish;
import asu.foe.wagba8805.pojos.Restaurant;
import asu.foe.wagba8805.repositories.DishRepository;
import asu.foe.wagba8805.repositories.RestaurantRepository;

public class FirebaseRTDBService {

  private static boolean initialized = false;
  private static final FirebaseDatabase database = FirebaseDatabase.getInstance();

  public static void init(Application application) {

    if (initialized) return;

    initialized = true;

    database.getReference("restaurants").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        RestaurantRepository restaurantRepository = new RestaurantRepository(application);
        DishRepository dishRepository = new DishRepository(application);

        for (DataSnapshot childRestaurantSnapshot : dataSnapshot.getChildren()) {

          Integer restaurant_id;
          restaurantRepository.insert(
              new Restaurant(
                  restaurant_id = childRestaurantSnapshot.child("id").getValue(int.class),
                  childRestaurantSnapshot.child("name").getValue(String.class),
                  childRestaurantSnapshot.child("address").getValue(String.class),
                  childRestaurantSnapshot.child("imageUrl").getValue(String.class)
              )
          );

          for (DataSnapshot childDishSnapshot : childRestaurantSnapshot.child("dishes").getChildren()) {

            dishRepository.insert(
                new Dish(
                    childDishSnapshot.child("id").getValue(int.class),
                    restaurant_id,
                    childDishSnapshot.child("price").getValue(Float.class),
                    childDishSnapshot.child("available").getValue(Boolean.class),
                    childDishSnapshot.child("name").getValue(String.class),
                    childDishSnapshot.child("description").getValue(String.class),
                    childDishSnapshot.child("imageUrl").getValue(String.class)
                )
            );

          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
      }
    });

  }
}
