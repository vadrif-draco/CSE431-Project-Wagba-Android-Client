package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.DishesItemBinding;
import asu.foe.wagba8805.pojos.DishesItem;

public class DishesItemListAdapter extends RecyclerView.Adapter<DishesItemListAdapter.ViewHolder> {

  private final Context mContext;
  private DishesItem[] dishesItems;

  public DishesItemListAdapter(Context context) {
    this.mContext = context;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    DishesItemBinding binding;

    public ViewHolder(DishesItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(DishesItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    DishesItem item = dishesItems[position];
    Glide.with(holder.itemView)
        .load(item.imageUrl)
        .thumbnail(
            Glide.with(holder.itemView)
                .load(R.drawable.dish_placeholder)
                .centerCrop()
        )
        .centerCrop()
        .into(holder.binding.img);
    holder.binding.dishNameTV.setText(item.name);
    holder.binding.dishDescTV.setText(item.description);
    holder.binding.proceedBtn.setOnClickListener(v -> {
      Toast.makeText(mContext, "Add to cart", Toast.LENGTH_SHORT).show();
    });
  }

  @Override
  public int getItemCount() {
    return dishesItems.length;
  }

  public void set(DishesItem[] items) {
    this.dishesItems = items;
    notifyDataSetChanged();
  }
}
