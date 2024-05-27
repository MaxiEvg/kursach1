package com.kursovaya.receptorganaizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class RecipeAdapter extends BaseAdapter {
    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_item, parent, false);
        }

        ImageView recipeImage = convertView.findViewById(R.id.recipeImage);
        TextView recipeTitle = convertView.findViewById(R.id.recipeTitle);
        TextView recipeDescription = convertView.findViewById(R.id.recipeDescription);

        Recipe recipe = recipes.get(position);

        recipeTitle.setText(recipe.getTitle());
        recipeDescription.setText(recipe.getDescription());
        Bitmap image = recipe.getImage();
        if (image != null) {
            recipeImage.setImageBitmap(image);
        } else {
            recipeImage.setImageResource(android.R.color.darker_gray);
        }

        return convertView;
    }
}

