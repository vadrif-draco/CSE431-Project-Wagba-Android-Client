package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.CartRestaurantDishBinding;
import asu.foe.wagba8805.pojos.Dish;
import asu.foe.wagba8805.viewmodels.DishViewModel;

public class CartRestaurantDishListAdapter extends RecyclerView.Adapter<CartRestaurantDishListAdapter.ViewHolder> {

  private final Context mContext;
  private List<Dish> dishesInCart = new ArrayList<>();

  CartRestaurantDishListAdapter(Context context) {
    this.mContext = context;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    CartRestaurantDishBinding binding;

    public ViewHolder(CartRestaurantDishBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  @Override
  public void onBindViewHolder(CartRestaurantDishListAdapter.ViewHolder holder, int position) {
    Dish dish = dishesInCart.get(position);

    holder.binding.dishNameTV.setText(dish.name);

    Glide.with(holder.itemView)
        .load(dish.imageUrl)
        .thumbnail(
            Glide.with(holder.itemView)
                .load(R.drawable.dish_placeholder)
                .centerCrop()
        )
        .centerCrop()
        .into(holder.binding.img);

    holder.binding.inCartQuantity.setText(String.valueOf(dish.inCartQuantity));

    holder.binding.incrementBtn.setOnClickListener(v -> {

      DishViewModel dishViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(DishViewModel.class);
      LiveData<List<Dish>> dishLiveData = dishViewModel.getDish(dish.id, dish.restaurant_id);
      Observer<List<Dish>> dishObserver = new Observer<List<Dish>>() {
        @Override
        public void onChanged(List<Dish> dishes) {
          Dish dish = dishes.get(0);
          dish.inCartQuantity++;
          dishViewModel.updateDishData(dish);
          dishLiveData.removeObserver(this);
        }
      };
      dishLiveData.observeForever(dishObserver);

    });
    holder.binding.decrementBtn.setOnClickListener(v -> {

      if (dish.inCartQuantity > 1) {

        DishViewModel dishViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(DishViewModel.class);
        LiveData<List<Dish>> dishLiveData = dishViewModel.getDish(dish.id, dish.restaurant_id);
        Observer<List<Dish>> dishObserver = new Observer<List<Dish>>() {
          @Override
          public void onChanged(List<Dish> dishes) {
            Dish dish = dishes.get(0);
            dish.inCartQuantity--;
            dishViewModel.updateDishData(dish);
            dishLiveData.removeObserver(this);
          }
        };
        dishLiveData.observeForever(dishObserver);
      }

    });
  }

  @NonNull
  @Override
  public CartRestaurantDishListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CartRestaurantDishListAdapter.ViewHolder(CartRestaurantDishBinding.inflate(LayoutInflater.from(mContext), parent, false));
  }

  @Override
  public int getItemCount() {
    return dishesInCart.size();
  }

  public void setDishesInCart(List<Dish> dishes) {
    this.dishesInCart = dishes;
    notifyDataSetChanged();
  }
}
