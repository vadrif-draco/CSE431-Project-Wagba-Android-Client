package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.databinding.OrderItemBinding;
import asu.foe.wagba8805.pojos.Order;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

  private final Context mContext;
  private List<Order> orders = new ArrayList<>();

  public OrderListAdapter(Context context) {
    this.mContext = context;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    OrderItemBinding binding;

    public ViewHolder(OrderItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  @NonNull
  @Override
  public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new OrderListAdapter.ViewHolder(OrderItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
  }

  @Override
  public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
    Order order = orders.get(position);
    holder.binding.orderId.setText(order.order_id);
    holder.binding.orderStatus.setText(String.valueOf(order.order_status));
  }

  @Override
  public int getItemCount() {
    return orders.size();
  }

  public void set(List<Order> items) {
    this.orders = items;
    notifyDataSetChanged();
  }
}
