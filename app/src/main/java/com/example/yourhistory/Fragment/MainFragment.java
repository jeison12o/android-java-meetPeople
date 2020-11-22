package com.example.yourhistory.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourhistory.R;

/*1) create the class fragment
2)more class of type fragment
3)create document xml of the first fragment
 */
public class MainFragment extends Fragment {

    //3
    private onFragmentBtnSelected listener;
    View view;


    //create view references fragment xml
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view; view = inflater.inflate(R.layout.fragment_main, container, false);
        Button btnLoadFragment = view.findViewById(R.id.btnLoadFragment);


        btnLoadFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainFragment.class));
            }
        });
        return view;
    }

    //2.
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof onFragmentBtnSelected){
            listener = (onFragmentBtnSelected) context;
        } else{
            throw new ClassCastException(context.toString() + "must implement listener");
        }
    }

    //create 1.
    public interface onFragmentBtnSelected{
        public void onButtonSelected();
    }

}
