package com.example.yourhistory.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.yourhistory.R;
import com.example.yourhistory.model.Multimedia;
import com.example.yourhistory.model.VolleySingleton;

import java.util.List;

public class AdapterPhotosUser extends RecyclerView.Adapter<AdapterPhotosUser.PhotoHolder>{

    List<Multimedia> list;
    Context context;

    public AdapterPhotosUser(List<Multimedia> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        return new PhotoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        loadImage(list.get(position).getUrl(), holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void loadImage(String url,final PhotoHolder holder){
        url = url.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.photoItem.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error load image: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {

        ImageView photoItem;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            photoItem = itemView.findViewById(R.id.photoItem);
        }
    }
}
