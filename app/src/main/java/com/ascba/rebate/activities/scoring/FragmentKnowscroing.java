package com.ascba.rebate.activities.scoring;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ascba.rebate.R;

public class FragmentKnowscroing extends Fragment {
    private ImageView ivCover;
    private TextView tvTitle,tvContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowscroing, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        ivCover = (ImageView) view.findViewById(R.id.iv_know_cover);
        tvTitle = (TextView) view.findViewById(R.id.tv_know_title);
        tvContent = (TextView) view.findViewById(R.id.tv_know_content);

        Bundle argument = getArguments();

        ivCover.setBackgroundResource(argument.getInt("pic"));
        tvTitle.setText(argument.getString("title"));
        tvContent.setText(argument.getString("content"));
    }

}
