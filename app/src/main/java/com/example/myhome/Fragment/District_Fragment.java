package com.example.myhome.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myhome.Adapter.one_item_text_Adapter;
import com.example.myhome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class District_Fragment extends Fragment {
    RecyclerView recyclerView;
    String pID,pName;
    public District_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_district, container, false);
        recyclerView=view.findViewById(R.id.district_fragment_recycleview);
        // lấy id của cái tỉnh
        Bundle b= getArguments();
        if (b != null){
            pID=b.getString("pID");
            pName=b.getString("pName");
        }
        // set lại title để biết vừa chọn cái gì
        getActivity().setTitle(pName);
        //Truy cập vào data base để lấy dữ liệu ra
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference=db.collection(pID);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<String> dName = new ArrayList<String>();
                ArrayList<String> dID = new ArrayList<String>();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    if (doc.getId() != null) {
                        dID.add(doc.getId());
                        dName.add(String.valueOf(doc.get("name")));
                    }
                }
                // dùng lại adapter để đỏ dữ liệu lên
                one_item_text_Adapter adapter = new one_item_text_Adapter(District_Fragment.this.getContext(),dName,dID);
                LinearLayoutManager manager = new LinearLayoutManager(District_Fragment.this.getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);

                // bắt sự kiện click vào từng mục (void này phải viết thêm trong adapter)
                adapter.setOnItemClickedListener(new one_item_text_Adapter.OnItemClickedListener() {
                    @Override
                    public void onItemClick(String ID,String Name) {
                        // lấy dữ liệu (ID là id của cái quận đc chọn )và chuyển qua Fragment house
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        try {
                            //phải Try catch chỗ này mới ko báo lỗi
                            Fragment fragment = (Fragment) House_Fragment.class.newInstance();
                            // đóng gói ID lấy đc từ adapter
                            Bundle bundle= new Bundle();
                            bundle.putString("dID",ID);
                            bundle.putString("dName",Name);
                            bundle.putString("pID",pID);
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
            }
        });

        return view;
    }



}
