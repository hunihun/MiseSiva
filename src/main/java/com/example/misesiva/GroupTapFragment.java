package com.example.misesiva;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misesiva.utils.Constant;

public class GroupTapFragment extends Fragment {
    private TextView mise;
    private TextView chomise;
    private TextView place;
    private TextView text1;
    private TextView text2;
    private ImageView image;
    private Button select;
    private LinearLayout linear;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    Integer misepoint;
    Integer chomisepoint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_fragment,container,false);
    }
    @Override
    public void onResume(){
        super.onResume();
        linear = getView().findViewById(R.id.background);
        mise = getView().findViewById(R.id.misestate);
        text1 = getView().findViewById(R.id.text1);
        text2 = getView().findViewById(R.id.text2);
        select = getView().findViewById(R.id.select);
        chomise = getView().findViewById(R.id.chomisestate);
        place = getView().findViewById(R.id.juso);

        image = getView().findViewById(R.id.view1);

        // 미세먼지 수치 체크 후 상태표시
        if(Constant.mise < 30) {
            Constant.mstate = "매우좋음";
            misepoint = 1;
        }
        else if (31 < Constant.mise && Constant.mise< 40) {
            Constant.mstate = "좋음";
            misepoint = 2;
        }
        else if (41 < Constant.mise && Constant.mise< 50) {
            Constant.mstate = "보통";
            misepoint = 3;
        }
        else if (51 < Constant.mise && Constant.mise< 100) {
            Constant.mstate = "나쁨";
            misepoint = 4;
        }
        else if (101 < Constant.mise) {
            Constant.mstate = "매우나쁨";
            misepoint = 5;
        }

        //초미세먼지 수치 체크 후 상태표시
        if(Constant.chomise < 15) {
            Constant.cstate = "매우좋음";
            chomisepoint = 1;
        }
        else if (16 < Constant.chomise && Constant.chomise< 20) {
            Constant.cstate = "좋음";
            chomisepoint = 2;
        }
        else if (21 < Constant.chomise && Constant.chomise< 25) {
            Constant.cstate = "보통";
            chomisepoint = 3;
        }
        else if (26 < Constant.chomise && Constant.chomise< 50) {
            Constant.cstate = "나쁨";
            chomisepoint = 4;
        }
        else if (50 < Constant.chomise) {
            Constant.cstate = "매우나쁨";
            chomisepoint = 5;
        }

        //미세먼지 수치와 초미세먼지 수치 비교 후 더 나쁜 상태를 보여줌
        if (misepoint >= chomisepoint) {
            if (misepoint == 1) {
                image.setImageResource(R.drawable.verygood2);
                linear.setBackgroundColor(Color.parseColor("#0080FF"));
                text1.setText("매우 좋은 공기\n");
                text2.setText("데이트하기 딱 좋아요!\n");
            } else if (misepoint == 2) {
                image.setImageResource(R.drawable.good2);
                linear.setBackgroundColor(Color.parseColor("#F7FE2E"));
                text1.setText("좋은 공기\n");
                text2.setText("꽃 보러 가자~\n");
            } else if (misepoint == 3) {
                image.setImageResource(R.drawable.soso2);
                linear.setBackgroundColor(Color.parseColor("#58FA58"));
                text1.setText("보통 공기\n");
                text2.setText("그럭저럭 이군요\n");
            } else if (misepoint == 4) {
                image.setImageResource(R.drawable.bad2);
                linear.setBackgroundColor(Color.parseColor("#FF0000"));
                text1.setText("나쁜 공기\n");
                text2.setText("마스크 필수~\n");
            } else if (misepoint == 5) {
                image.setImageResource(R.drawable.verybad2);
                linear.setBackgroundColor(Color.parseColor("#1C1C1C"));
                text1.setText("매우 나쁜 공기\n");
                text2.setText("외출 금지!\n");
            }
        } else {
            if (chomisepoint == 1) {
                image.setImageResource(R.drawable.verygood2);
                linear.setBackgroundColor(Color.parseColor("#0080FF"));
                text1.setText("매우 좋은 공기\n");
                text2.setText("데이트하기 딱 좋아요!\n");
            } else if (chomisepoint == 2) {
                image.setImageResource(R.drawable.good2);
                linear.setBackgroundColor(Color.parseColor("#F7FE2E"));
                text1.setText("좋은 공기\n");
                text2.setText("꽃 보러 가자~\n");
            } else if (chomisepoint == 3) {
                image.setImageResource(R.drawable.soso2);
                linear.setBackgroundColor(Color.parseColor("#58FA58"));
                text1.setText("보통 공기\n");
                text2.setText("그럭저럭 이군요\n");
            } else if (chomisepoint == 4) {
                image.setImageResource(R.drawable.bad2);
                linear.setBackgroundColor(Color.parseColor("#FF0000"));
                text1.setText("나쁜 공기\n");
                text2.setText("마스크 필수~\n");
            } else if (chomisepoint == 5) {
                image.setImageResource(R.drawable.verybad2);
                linear.setBackgroundColor(Color.parseColor("#1C1C1C"));
                text1.setText("매우 나쁜 공기\n");
                text2.setText("외출 금지!\n");
            }
        }
        mise.setText("미세먼지 \n" + Constant.mise + "\n" + Constant.mstate+ "\n");
        chomise.setText("초미세먼지 \n" + Constant.chomise + "\n" + Constant.cstate+ "\n");
        place.setText("\n현위치 \n" + Constant.place);

        // 이미지 클릭시 상세보기
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(),"상세보기",Toast.LENGTH_SHORT).show();
            }
        });

        // 새로고침 클릭시 view 새로고침
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().recreate();
//                Toast.makeText(getContext(),"새로고침",Toast.LENGTH_SHORT).show();
            }
        });

    }

}