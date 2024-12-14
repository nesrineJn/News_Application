package com.isetb.news_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailTitle = findViewById(R.id.detailTitle);
        TextView detailSource = findViewById(R.id.detailSource);
        TextView detailContent = findViewById(R.id.detailContent);
        TextView detailAuthor = findViewById(R.id.detailAuthor);
        TextView detailDate = findViewById(R.id.detailDate);
        TextView readMoreLink = findViewById(R.id.readMoreLink);

        ImageView backButton = findViewById(R.id.backButton);
        ImageView shareButton = findViewById(R.id.shareButton);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String content = getIntent().getStringExtra("content");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String author = getIntent().getStringExtra("author");
        String date = getIntent().getStringExtra("publishedAt");
        String source = getIntent().getStringExtra("source");
        String url = getIntent().getStringExtra("url");


        detailTitle.setText(title);
        detailSource.setText(source != null ? "Source: " + source : "Source: Unknown");
        detailAuthor.setText(!TextUtils.isEmpty(author) ? "Author: " + author : "Author: Unknown");
        detailDate.setText(!TextUtils.isEmpty(date) ? "Published on: " + date : "Published on: Unknown");
        Picasso.get().load(imageUrl).into(detailImage);

        if (content != null && content.length() > 200) {
            detailContent.setText(content.substring(0, 200) + "...");
            readMoreLink.setVisibility(View.VISIBLE);
        } else {
            detailContent.setText(content != null ? content : "Content not available");
        }


        readMoreLink.setOnClickListener(v -> {
            if (url != null && !url.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        backButton.setOnClickListener(v -> onBackPressed());


        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this article!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + description + "\n\n" + url);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        TextView headerTitle = findViewById(R.id.headerTitle);




        if (source != null && !source.isEmpty()) {
            headerTitle.setText(source);
        } else {
            headerTitle.setText("Article Details");
        }

    }
}
