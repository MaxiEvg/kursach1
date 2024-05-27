package com.kursovaya.receptorganaizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> implements Filterable {

    private List<Recipe> originalRecipes;
    private List<Recipe> filteredRecipes;
    private RecipeFilter recipeFilter;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        this.originalRecipes = new ArrayList<>(recipes);
        this.filteredRecipes = recipes;
        getFilter();
    }

    @Override
    public int getCount() {
        return filteredRecipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        return filteredRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item, parent, false);
        }

        Recipe recipe = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.recipeTitle);
        TextView descriptionTextView = convertView.findViewById(R.id.recipeDescription);
        ImageView imageView = convertView.findViewById(R.id.recipeImage);

        if (titleTextView != null && recipe != null) {
            titleTextView.setText(recipe.getTitle());
        }

        if (descriptionTextView != null && recipe != null) {
            descriptionTextView.setText(recipe.getDescription());
        }

        if (recipe != null && recipe.getImage() != null) {
            imageView.setImageBitmap(recipe.getImage());
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background); // Default image resource if no image is available
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (recipeFilter == null) {
            recipeFilter = new RecipeFilter();
        }
        return recipeFilter;
    }

    private class RecipeFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = originalRecipes;
                results.count = originalRecipes.size();
            } else {
                List<Recipe> filteredList = new ArrayList<>();
                for (Recipe recipe : originalRecipes) {
                    if (recipe.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            recipe.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(recipe);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredRecipes = (List<Recipe>) results.values;
            notifyDataSetChanged();
        }
    }
}
