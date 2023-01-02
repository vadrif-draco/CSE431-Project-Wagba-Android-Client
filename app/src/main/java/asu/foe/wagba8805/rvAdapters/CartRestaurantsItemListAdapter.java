package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.databinding.CartRestaurantBinding;
import asu.foe.wagba8805.pojos.Dish;
import asu.foe.wagba8805.pojos.Restaurant;
import asu.foe.wagba8805.viewmodels.DishViewModel;

public class CartRestaurantsItemListAdapter extends RecyclerView.Adapter<CartRestaurantsItemListAdapter.ViewHolder> {

  private final Context mContext;
  private List<Restaurant> restaurantsInCart = new ArrayList<>();
  private List<Dish> dishesInCart = new ArrayList<>();

  public CartRestaurantsItemListAdapter(Context context) {
    this.mContext = context;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    CartRestaurantBinding binding;

    public ViewHolder(CartRestaurantBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  @Override
  public void onBindViewHolder(CartRestaurantsItemListAdapter.ViewHolder holder, int position) {
    Restaurant restaurant = restaurantsInCart.get(position);
    holder.binding.restaurantNameTV.setText(restaurant.name);

    RecyclerView dishesRV = holder.binding.dishesRV;
    CartRestaurantDishListAdapter cartRestaurantDishListAdapter = new CartRestaurantDishListAdapter(mContext);
    DishViewModel dishViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(DishViewModel.class);
    dishViewModel.getDishesInCartForRestaurant(restaurant.id).observe((LifecycleOwner) mContext, dishes ->
    {
      dishesInCart = dishes;
      cartRestaurantDishListAdapter.setDishesInCart(dishes);
    });
    dishesRV.setAdapter(cartRestaurantDishListAdapter);
    dishesRV.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

    holder.binding.orderBtn.setOnClickListener(v -> {

      FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference myRef = database.getReference("orders");
      myRef.child(myRef.push().getKey()).setValue(dishesInCart);
    });
  }

  @NonNull
  @Override
  public CartRestaurantsItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CartRestaurantsItemListAdapter.ViewHolder(CartRestaurantBinding.inflate(LayoutInflater.from(mContext), parent, false));
  }

  @Override
  public int getItemCount() {
    return restaurantsInCart.size();
  }

  public void setRestaurantsInCart(List<Restaurant> restaurants) {
    this.restaurantsInCart = restaurants;
    notifyDataSetChanged();
  }
}
