package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lunchmunch.databinding.ActivityRecipeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lunchmunch.Location;
import com.example.lunchmunch.databinding.IngredientsActivityBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.example.lunchmunch.FoodItemClass;

public class IngredientsActivity extends AppCompatActivity implements IngredientItemFragment.OnFragmentInteractionListener {
    Button RecipesNav, MealPlanNav, ShoppingListNav;
    ArrayList<Food> ingredientsList;
    FirebaseFirestore db;


    ListView ingredientsListView;
    FoodItemAdapter ingredientAdapter;
    ArrayList<FoodItemClass> dataList;

    // Declare the variables so that you will be able to reference it later.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_activity);

        db = FirebaseFirestore.getInstance();
        final CollectionReference IngrCollec = db.collection("Ingredients");

        ingredientsListView = (ListView) findViewById(R.id.ingredient_list);

        FoodItemClass[] foods ={new FoodItemClass("banana", "your mom", new Date(), Location.FREEZER, 6, 6)};

        dataList = new ArrayList<>();

        dataList.addAll(Arrays.asList(foods));
        System.out.println(dataList);
        ingredientAdapter = new FoodItemAdapter(this, R.layout.content_ingredients, dataList);
        System.out.println(ingredientsListView);
        ingredientsListView.setAdapter(ingredientAdapter);

        // Adding/Editing a new item
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        addFoodButton.setOnClickListener(view -> new IngredientItemFragment().show(getSupportFragmentManager(), "ADD_INGREDIENT"));


        //initDBListener(IngrCollec);
        initViews();

        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, RecipeActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, ShoppingListActivity.class));
        });

        //Add Food obj to Ingredients list in database
        /*
        Food newIngredient = new Food(get attr from user input modal);
        // add the new food to our current ingr list
        ingredientsList.add(newIngredient);
        // update ingr list in db by overwriting it with the current ingredientsList
        // by leaving document() blank we let firestore autogen an id
        IngrCollec.add(ingredientsList) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
        */

        // Delete Food obj (delete from ingredientsList then run add code above (this will overwrite the list in the db)

        // Edit Food obj (edit from ingriendsList then same as above ^^)

    }
    @Override
    public void onOkPressed() {
        ingredientAdapter.add(new FoodItemClass("banana", "your mom", new Date(), Location.FREEZER, 6, 6));
    }

    private void initDBListener(CollectionReference ingrCollec) {
        ingrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    ingredientsList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ingredientsList.add(document.toObject(Food.class));
                    }
                    //IngredientsListAdapter.update() whatever this command is, too lazy to search up
                }
            }
        });
        /*
        ingrCollec.get().addOnCompleteListener(task->{
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> users = (List<Map<String, Object>>) document.get("users");
                }
            }
        });*/

    }

    private void initViews() {
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }

}

