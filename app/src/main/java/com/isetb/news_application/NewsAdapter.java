package com.isetb.news_application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private List<Article> articles;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public NewsAdapter(Context context, List<Article> articles, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.articles = articles;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override

    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);


        holder.newsTitle.setText(article.getTitle() != null ? article.getTitle() : "No Title Available");


        String timeAgo = getTimeAgo(article.getPublishedAt());
        holder.newsTime.setText(timeAgo);

        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Picasso.get()
                    .load(article.getUrlToImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.newsImage);
        } else {
            holder.newsImage.setImageResource(R.drawable.ic_launcher_foreground);
        }


        updateFavoriteIcon(holder.saveIcon, article.isFavorite());


        holder.saveIcon.setOnClickListener(v -> {
            boolean newState = !article.isFavorite();
            article.setFavorite(newState);
            updateFavoriteIcon(holder.saveIcon, newState);


            sortArticlesByFavorite();


            Toast.makeText(context, newState ? "Added to favorites" : "Removed from favorites", Toast.LENGTH_SHORT).show();
        });


        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(article));
    }


    private void updateFavoriteIcon(ImageView saveIcon, boolean isFavorite) {
        if (saveIcon == null) {
            Log.e("NewsAdapter", "saveIcon is null. Cannot update favorite icon.");
            return;
        }
        int color = isFavorite ? context.getResources().getColor(R.color.orange_500) : context.getResources().getColor(R.color.gray_400);
        saveIcon.setColorFilter(color);
    }




    @Override
    public int getItemCount() {
        return articles.size();
    }

    private String getTimeAgo(String publishedDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        try {
            Date past = format.parse(publishedDate);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());

            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (minutes < 60) {
                return minutes + " minutes ago";
            } else {
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                if (hours < 24) {
                    return hours + " hours ago";
                } else {
                    long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                    return days + " days ago";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage, saveIcon;
        TextView newsTitle, newsTime;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            saveIcon = itemView.findViewById(R.id.saveIcon);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsTime = itemView.findViewById(R.id.newsTime);

            if (saveIcon == null) {
                Log.e("NewsViewHolder", "saveIcon is null. Check the XML layout.");
            }
        }
    }
    private void sortArticlesByFavorite() {
        articles.sort((a, b) -> Boolean.compare(b.isFavorite(), a.isFavorite()));
        notifyDataSetChanged();
    }

}
