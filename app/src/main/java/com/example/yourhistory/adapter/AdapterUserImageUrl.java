package com.example.yourhistory.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.yourhistory.R;
import com.example.yourhistory.model.User;
import com.example.yourhistory.model.VolleySingleton;

import java.util.List;

public class AdapterUserImageUrl extends RecyclerView.Adapter<AdapterUserImageUrl.UserHolder>{

    List<User> listUser;
    Context context;

    public AdapterUserImageUrl(List<User> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_image, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.lblNameUser.setText(listUser.get(position).getNameUser());
        holder.lblName.setText(listUser.get(position).getName() +" "+listUser.get(position).getLastName());
        if (listUser.get(position).getUrlPhotoProfile() != null) {
            loadImage(listUser.get(position).getUrlPhotoProfile(), holder);
        }else {
            holder.imageViewUser.setImageResource(R.drawable.usuario);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    private void loadImage(String url,final UserHolder holder){
        url = url.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imageViewUser.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error load image: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    public class UserHolder extends RecyclerView.ViewHolder{

        TextView lblNameUser, lblName;
        ImageView imageViewUser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            lblNameUser = itemView.findViewById(R.id.lblTemplateNameUser);
            lblName = itemView.findViewById(R.id.lblTemplateName);
            imageViewUser = itemView.findViewById(R.id.imageUserTemplate);
        }
    }
}
