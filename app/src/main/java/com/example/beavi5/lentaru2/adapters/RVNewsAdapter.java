package com.example.beavi5.lentaru2.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beavi5.lentaru2.ImageActivity;
import com.example.beavi5.lentaru2.MainActivity;
import com.example.beavi5.lentaru2.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beavi5 on 20.07.2017.
 */

public class RVNewsAdapter extends RecyclerView.Adapter<RVNewsAdapter.NewsHolder> {
    List<RSSItem> listNews = new ArrayList();

    public RVNewsAdapter(List<RSSItem> listNews) {
        this.listNews = listNews;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);

        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {
            holder.mTitle.setText(listNews.get(position).getTitle());
            holder.mDesc.setText(listNews.get(position).getDesc());
        new DownloadImageTask(holder.mImg).execute(listNews.get(position).getImg());

        holder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*AlertDialog.Builder alertadd = new AlertDialog.Builder(view.getContext());
                LayoutInflater factory = LayoutInflater.from(view.getContext());
                final View imageview = factory.inflate(R.layout.image_dialog, null);

                new DownloadImageTask( (ImageView) imageview.findViewById(R.id.dialog_imageview)).execute(listNews.get(position).getImg());
                alertadd.setView(imageview);


                alertadd.show();*/

                Intent intentImage = new Intent(view.getContext(), ImageActivity.class);
                intentImage.putExtra("img",listNews.get(position).getImg());
                intentImage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                view.getContext().startActivity(intentImage);

            }
        });


    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDesc;
        ImageView mImg;

        public NewsHolder(View itemView) {
            super(itemView);
            mDesc = itemView.findViewById(R.id.newsDesc);
            mTitle = itemView.findViewById(R.id.newsTitle);
            mImg = itemView.findViewById(R.id.newsImg);


        }
    }
}

