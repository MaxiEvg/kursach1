package com.kursovaya.receptorganaizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private ImageView recipeImage;
    private EditText recipeTitle, recipeDescription;
    private int recipeIndex = -1;
    private Bitmap selectedImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);
        recipeDescription = findViewById(R.id.recipeDescription);
        Button saveButton = findViewById(R.id.saveButton);
        Button removeButton = findViewById(R.id.removeButton);

        Intent intent = getIntent();
        recipeIndex = intent.getIntExtra("recipeIndex", -1);
        if (recipeIndex != -1) {
            // Existing recipe
            recipeTitle.setText(intent.getStringExtra("recipeTitle"));
            recipeDescription.setText(intent.getStringExtra("recipeDescription"));
            byte[] byteArray = intent.getByteArrayExtra("recipeImage");
            if (byteArray != null) {
                selectedImageBitmap = BitmapUtils.byteArrayToBitmap(byteArray);
                recipeImage.setImageBitmap(selectedImageBitmap);
            }
        }

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("recipeIndex", recipeIndex);
                resultIntent.putExtra("recipeTitle", recipeTitle.getText().toString());
                resultIntent.putExtra("recipeDescription", recipeDescription.getText().toString());
                if (selectedImageBitmap != null) {
                    resultIntent.putExtra("recipeImage", BitmapUtils.bitmapToByteArray(selectedImageBitmap));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("recipeIndex", recipeIndex);
                setResult(Activity.RESULT_FIRST_USER, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri selectedImage = data.getData();
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    recipeImage.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

