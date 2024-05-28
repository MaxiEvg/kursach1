package com.kursovaya.receptorganaizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_RECIPE = 1;
    private RecipeDatabaseHelper dbHelper;
    private RecipeAdapter adapter;
    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new RecipeDatabaseHelper(this);
        recipes = dbHelper.getAllRecipes();

        adapter = new RecipeAdapter(this, recipes);
        ListView listView = findViewById(R.id.recipeListView);
        listView.setAdapter(adapter);

        Button fab = findViewById(R.id.addRecipeButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT_RECIPE);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = recipes.get(position);
            Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipeIndex", position);
            intent.putExtra("recipeId", recipe.getId());
            intent.putExtra("recipeTitle", recipe.getTitle());
            intent.putExtra("recipeDescription", recipe.getDescription());
            intent.putExtra("recipeImagePath", recipe.getImagePath());
            startActivityForResult(intent, REQUEST_CODE_EDIT_RECIPE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_RECIPE && resultCode == RESULT_OK && data != null) {
            int recipeIndex = data.getIntExtra("recipeIndex", -1);
            int recipeId = data.getIntExtra("recipeId", -1);
            String title = data.getStringExtra("recipeTitle");
            String description = data.getStringExtra("recipeDescription");
            String imagePath = data.getStringExtra("recipeImagePath");

            Recipe recipe = new Recipe(title, description, imagePath);
            recipe.setId(recipeId);

            if (recipeIndex == -1) {
                // New Recipe
                dbHelper.addRecipe(recipe);
                recipes.clear();
                recipes.addAll(dbHelper.getAllRecipes());
            } else {
                // Edit existing recipe
                recipes.set(recipeIndex, recipe);
                dbHelper.updateRecipe(recipe);
            }

            adapter.notifyDataSetChanged();
        } else if (resultCode == Activity.RESULT_FIRST_USER && data != null) {
            int recipeIndex = data.getIntExtra("recipeIndex", -1);
            int recipeId = data.getIntExtra("recipeId", -1);
            if (recipeIndex != -1) {
                dbHelper.deleteRecipe(recipeId);
                recipes.remove(recipeIndex);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
