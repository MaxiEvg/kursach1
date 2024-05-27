package com.kursovaya.receptorganaizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.widget.AdapterView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_RECIPE = 1;
    private ListView listView;
    private RecipeAdapter adapter;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipes = new ArrayList<>();
        listView = findViewById(R.id.recipeListView);
        adapter = new RecipeAdapter(this, recipes);
        listView.setAdapter(adapter);

        Button addButton = findViewById(R.id.addRecipeButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_RECIPE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipes.get(position);
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipeIndex", position);
                intent.putExtra("recipeTitle", recipe.getTitle());
                intent.putExtra("recipeDescription", recipe.getDescription());
                intent.putExtra("recipeImage", BitmapUtils.bitmapToByteArray(recipe.getImage()));
                startActivityForResult(intent, REQUEST_CODE_EDIT_RECIPE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_RECIPE) {
            if (resultCode == RESULT_OK) {
                int recipeIndex = data.getIntExtra("recipeIndex", -1);
                String title = data.getStringExtra("recipeTitle");
                String description = data.getStringExtra("recipeDescription");
                byte[] byteArray = data.getByteArrayExtra("recipeImage");
                Bitmap image = null;
                if (byteArray != null) {
                    image = BitmapUtils.byteArrayToBitmap(byteArray);
                }

                if (recipeIndex == -1) {
                    // New Recipe
                    recipes.add(new Recipe(title, description, image));
                } else {
                    // Edit existing recipe
                    Recipe recipe = recipes.get(recipeIndex);
                    recipe.setTitle(title);
                    recipe.setDescription(description);
                    recipe.setImage(image);
                }

                adapter.notifyDataSetChanged();
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                int recipeIndex = data.getIntExtra("recipeIndex", -1);
                if (recipeIndex != -1) {
                    recipes.remove(recipeIndex);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}



