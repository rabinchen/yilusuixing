package com.clb.school.opencv__ndk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clb.school.opencv__ndk.R;

/**
 * Created by Administrator on 2017/11/24.
 */

public class FragmentLDW extends Fragment {

    private Button startLDW;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_ldw,viewGroup,false);
        startLDW = (Button)view.findViewById(R.id.startDetection);
        startLDW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CameraActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
