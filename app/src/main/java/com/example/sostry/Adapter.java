package com.example.sostry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder> implements Filterable {

    private ArrayList<ModelClass> mEampleList;
    private List<ModelClass> exampleListFull;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int positions);
        void onDeleteClick(int position);
    }



    public void setOnItemClickListener(OnItemClickListener listener)
    {
       mListener = listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public ImageView mimageView , mDeleteImage;
        public TextView mTextView1 , mTextView2;

        public ExampleViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);
               mimageView = itemView.findViewById(R.id.imageviewcoddd);
               mTextView1 = itemView.findViewById(R.id.textvieewcod);
               mTextView2 = itemView.findViewById(R.id.seconfview);
               mDeleteImage = itemView.findViewById(R.id.image_delete);

               itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       if(listener != null)
                       {
                           int position = getAdapterPosition();
                           if(position != RecyclerView.NO_POSITION){
                               listener.onItemClick(position);
                           }
                       }
                   }
               });

               mDeleteImage.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(listener != null)
                       {
                           int position = getAdapterPosition();
                           if(position != RecyclerView.NO_POSITION){
                               listener.onDeleteClick(position);
                           }
                       }
                   }
               });

        }
    }

    public Adapter(ArrayList<ModelClass> examplelist)
    {
        mEampleList = examplelist;
        exampleListFull = new ArrayList<>(examplelist);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v , mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        ModelClass modelClass = mEampleList.get(position);

        holder.mimageView.setImageResource(modelClass.getImageResourse());
        holder.mTextView1.setText(modelClass.getTitle());
        holder.mTextView2.setText(modelClass.getBody());
    }

    @Override
    public int getItemCount() {
        return mEampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelClass> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(exampleListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ModelClass item: exampleListFull)
                {
                    if(item.getBody().toLowerCase().contains(filterPattern) || item.getTitle().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mEampleList.clear();
            mEampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
