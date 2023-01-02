package asu.foe.wagba8805.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.databinding.CartBinding;
import asu.foe.wagba8805.pojos.Dish;
import asu.foe.wagba8805.pojos.Restaurant;
import asu.foe.wagba8805.rvAdapters.CartRestaurantsItemListAdapter;
import asu.foe.wagba8805.viewmodels.DishViewModel;
import asu.foe.wagba8805.viewmodels.RestaurantViewModel;

public class CartActivity extends AppCompatActivity {

  CartBinding cBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    cBinding = CartBinding.inflate(getLayoutInflater());
    setContentView(cBinding.getRoot());

    RecyclerView restaurantsInCartRV = cBinding.restaurantsRV;
    CartRestaurantsItemListAdapter cartRestaurantsItemListAdapter = new CartRestaurantsItemListAdapter(this);
    DishViewModel dishViewModel = new ViewModelProvider(this).get(DishViewModel.class);
    RestaurantViewModel restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    dishViewModel.getDishesInCart().observe(this, dishes -> {

      List<Integer> restaurants_ids = new ArrayList<>();
      for (Dish dish : dishes) {
        if (!restaurants_ids.contains(dish.restaurant_id)) {
          restaurants_ids.add(dish.restaurant_id);
        }
      }

      restaurantViewModel.getAllRestaurants().observe(this, restaurants -> {
        List<Restaurant> restaurantsInCart = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
          if (restaurants_ids.contains(restaurant.id)) {
            restaurantsInCart.add(restaurant);
          }
        }
        cartRestaurantsItemListAdapter.setRestaurantsInCart(restaurantsInCart);
      });

    });
    restaurantsInCartRV.setAdapter(cartRestaurantsItemListAdapter);
    restaurantsInCartRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}