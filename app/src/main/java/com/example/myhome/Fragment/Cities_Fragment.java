package com.example.myhome.Fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.SimpleOnItemTouchListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myhome.Adapter.city_adapter;
import com.example.myhome.Adapter.one_item_text_Adapter;
import com.example.myhome.MainActivity;
import com.example.myhome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cities_Fragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> pName = new ArrayList<String>();
    ArrayList<String> pID = new ArrayList<String>();
    ArrayList<Integer> pic = new ArrayList<Integer>();


    public Cities_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //anh xa
        View view= inflater.inflate(R.layout.fragment_cities, container, false);
        recyclerView=view.findViewById(R.id.cities_fragment_recycleview);

        //tao du lieu 5 vung
        pName.add("Hà Nội"); pName.add("Cần Thơ");pName.add("Đà Nẵng"); pName.add("TP.Buôn Mê Thuật"); pName.add("TP.Hồ Chí Minh");
        pID.add("hanoi"); pID.add("cantho");pID.add("danang"); pID.add("tpbmt"); pID.add("hcm");
        pic.add(R.drawable.hn); pic.add(R.drawable.ct);pic.add(R.drawable.dn); pic.add(R.drawable.bmt);pic.add(R.drawable.hcm);


        //do du lieu len recycleview
        final city_adapter adapter = new city_adapter(Cities_Fragment.this.getContext(),pic,pID,pName);
        GridLayoutManager manager = new GridLayoutManager(Cities_Fragment.this.getContext(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        // bắt sự kiện click vào từng mục (void này phải viết thêm trong adapter)
        adapter.setOnItemClickedListener(new city_adapter.OnItemClickedListener() {
            @Override
            public void onItemClick(String ID, String Name) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                try {
                    //phải Try catch chỗ này mới ko báo lỗi
                    Fragment fragment = (Fragment) District_Fragment.class.newInstance();
                    // đóng gói pID lấy đc từ adapter
                    Bundle bundle= new Bundle();
                    bundle.putString("pID",ID);
                    bundle.putString("pName",Name);
                    fragment.setArguments(bundle);
                    //chuyển Fragment _data
                    manager.beginTransaction().replace(R.id.flContent,fragment ).commit();


                    // ko cần quan tâm 2 cái catch bên dưới
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

}
