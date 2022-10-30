package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.ListView;
import android.widget.Spinner;

import java.util.Date;

public class IngredientsActivity extends AppCompatActivity implements IngredientItemFragment.OnFragmentInteractionListener {
    Button RecipesNav, MealPlanNav, ShoppingListNav;
    FirebaseFirestore db;
    CollectionReference IngrCollec;


    ListView ingredientsListView;
    FoodItemAdapter ingredientAdapter;
    ArrayList<Ingredient> dataList;
    Map<String, Ingredient> foodMap;
    IngredientItemFragment fragment;


    // Declare the variables so that you will be able to reference it later.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_activity);



        // init firebase reference
        db = FirebaseFirestore.getInstance();
        IngrCollec = db.collection("Ingredients");

        initDBListener(IngrCollec);
        initViews();

        // ingredient item storage
        dataList = new ArrayList<Ingredient>(); // used for displaying list
        foodMap = new HashMap<String, Ingredient>(); // used for storing unique ingredients

        // ingredient lists
        ingredientsListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientAdapter = new FoodItemAdapter(this, R.layout.content_ingredients, dataList);
        ingredientsListView.setAdapter(ingredientAdapter);

        // adding or editing a new item button
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        fragment = new IngredientItemFragment();
        addFoodButton.setOnClickListener(view -> fragment.show(getSupportFragmentManager(), "ADD_INGREDIENT"));



        // navbar listeners
        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, RecipeActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, ShoppingListActivity.class));
        });

        //Open fragment when ingredient_list item is clicked
        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
            }
        });

    }
    @Override
    public void onOkPressed(String name, String description, Date bestBefore, Location location, Integer count, Integer cost, IngredientCategory category) {
        System.out.println(bestBefore);

        Ingredient newIngredient = new Ingredient(name, description, bestBefore, location, count, cost, category);

        // add the new food to our current ingr list if new
        if (!foodMap.containsKey(name)) {
            dataList.add(newIngredient);
            ingredientAdapter.notifyDataSetChanged();

        }
        foodMap.put(newIngredient.getName(), newIngredient);
        // update ingr list in db by overwriting it with the current ingredientsList
        // by leaving document() blank we let firestore autogen an id .document("xT7aCsl8iLNnZkwKWvr3")
        IngrCollec.document("trp7wjjPuEizVaN62hjA").set(foodMap) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Delete Food obj (delete from ingredientsList then run add code above (this will overwrite the list in the db)

        // Edit Food obj (edit from ingriendsList then same as above ^^)
    }

    @Override
    public void deleteIngredient(Integer position) {
        String name = dataList.get(position).getName();

        if (foodMap.containsKey(name)) {
            dataList.remove(position);
            ingredientAdapter.notifyDataSetChanged();
            foodMap.remove(name);
        }

        IngrCollec.document("trp7wjjPuEizVaN62hjA").set(foodMap) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void initDBListener(CollectionReference ingrCollec) {

        ingrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // for deleting documents
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    ingrCollec.document(document.getId())
//                            .delete()
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    System.out.println("Success");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    System.out.println("Failure");
//                                }
//                            });
//                }
                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    dataList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // convert document objects back into Ingredient class objects
                        for (Object data : document.getData().values()) {
                            HashMap<String, Object> foodData = (HashMap<String, Object>) data;

                            String name = (String) foodData.get("name");
                            String description = (String) foodData.get("description");
                            Timestamp timestamp = (Timestamp) foodData.get("bestBefore");
                            Date bestBefore = (Date) timestamp.toDate();
                            Location location = Location.valueOf(foodData.get("location").toString().toUpperCase());
                            Integer count = (Integer) ((Long) foodData.get("count")).intValue();
                            Integer cost = (Integer) ((Long) foodData.get("cost")).intValue();
                            IngredientCategory category = IngredientCategory.valueOf(foodData.get("category").toString().toUpperCase());

                            Ingredient food = new Ingredient(name, description, bestBefore, location, count, cost, category);

                            // add to map for unique ingredient entries
                            foodMap.put(name, food);

                            // add to arraylist for adapter
                            dataList.add(food);
                        }

                    }
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });
//        ingrCollec.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//            }
//        })

    }


    private void initViews() {
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }


}

