package com.example.fruitvision;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {
    private List<Fruit> fruitList;

    public FruitAdapter(List<Fruit> fruitList) {
        this.fruitList = fruitList;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fruit_item, parent, false);
        return new FruitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        Fruit fruit = fruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        holder.fruitOrder.setText("Bộ: " + fruit.getOrder());
        holder.fruitFamily.setText("Họ: " + fruit.getFamily());
        holder.fruitGenus.setText("Chi: " + fruit.getGenus());
        holder.fruitImage.setImageResource(fruit.getImageResId());

        // Xử lý sự kiện click để mở ActivityDetail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityDetail.class);
                intent.putExtra("fruit_name", fruit.getName());
                intent.putExtra("fruit_details", fruit.getDetails());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    public static class FruitViewHolder extends RecyclerView.ViewHolder {
        TextView fruitName, fruitOrder, fruitFamily, fruitGenus;
        ImageView fruitImage;

        public FruitViewHolder(@NonNull View itemView) {
            super(itemView);
            fruitName = itemView.findViewById(R.id.fruit_name);
            fruitOrder = itemView.findViewById(R.id.fruit_order);
            fruitFamily = itemView.findViewById(R.id.fruit_family);
            fruitGenus = itemView.findViewById(R.id.fruit_genus);
            fruitImage = itemView.findViewById(R.id.fruit_image);
        }
    }
}