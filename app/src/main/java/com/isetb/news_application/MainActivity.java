package com.isetb.news_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout categoryContainer;
    private String selectedCategory = null;

    private final String[] categories = {
            "All", "Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryContainer = findViewById(R.id.categoryContainer);


        setupCategoryButtons();


        fetchNews(selectedCategory);
    }

    private void setupCategoryButtons() {
        Button defaultButton = null;

        for (String category : categories) {
            Button button = new Button(this);
            button.setText(category);
            button.setAllCaps(false);
            button.setTextSize(12);
            button.setBackgroundResource(R.drawable.button_selector);
            button.setTextColor(getResources().getColorStateList(R.color.button_text_selector));

            if (category.equals("All")) {
                defaultButton = button;
            }


            button.setOnClickListener(v -> {
                selectedCategory = category.equals("All") ? null : category.toLowerCase();
                fetchNews(selectedCategory);
                updateButtonStyles(button);
            });


            categoryContainer.addView(button);


            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
            params.setMargins(10, 0, 10, 0);
            button.setLayoutParams(params);
        }


        if (defaultButton != null) {
            updateButtonStyles(defaultButton);
        }
    }


    private void updateButtonStyles(Button selectedButton) {
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            View view = categoryContainer.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                if (button == selectedButton) {
                    button.setBackgroundResource(R.drawable.button_selected);
                    button.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    button.setBackgroundResource(R.drawable.button_unselected);
                    button.setTextColor(getResources().getColor(R.color.gray_600));
                }
            }
        }
    }

    private void fetchNews(String category) {
        NewsApiService apiService = RetrofitInstance.getInstance().create(NewsApiService.class);
        apiService.getTopHeadlines("aa925b7b0d9d4469b77730058ec4de02", "us", category, "MyNewsApp/1.0").enqueue(new Callback<News>() {

            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();

                    if (articles != null && !articles.isEmpty()) {
                        List<Article> filteredArticles = articles.stream()
                                .filter(article -> article.getUrlToImage() != null && !article.getUrlToImage().isEmpty())
                                .filter(article -> article.getDescription() != null && !article.getDescription().isEmpty())
                                .collect(Collectors.toList());


                        filteredArticles.sort((a, b) -> Boolean.compare(b.isFavorite(), a.isFavorite()));

                        NewsAdapter adapter = new NewsAdapter(MainActivity.this, filteredArticles, article -> {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("content", article.getContent());
                            intent.putExtra("source", article.getSource().getName());
                            intent.putExtra("title", article.getTitle());
                            intent.putExtra("description", article.getDescription());
                            intent.putExtra("imageUrl", article.getUrlToImage());
                            intent.putExtra("author", article.getAuthor());
                            intent.putExtra("publishedAt", article.getPublishedAt());
                            intent.putExtra("url", article.getUrl());

                            startActivity(intent);
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("API Response Failure", t.getMessage(), t);
            }
        });
    }

}
