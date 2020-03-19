package com.example.myhome.Fragment;


import android.net.Uri;
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

import com.example.myhome.Adapter.house_adapter;
import com.example.myhome.Adapter.one_item_text_Adapter;
import com.example.myhome.Adapter.picture_adapter;
import com.example.myhome.Model.House;
import com.example.myhome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class House_Fragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<House> houses = new ArrayList<House>();
    String dID,pID,dName;
    public House_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_house_, container, false);
        recyclerView=view.findViewById(R.id.house_fragment_recycleview);

        // lấy id của cái quận, tỉnh
        Bundle b= getArguments();
        if (b != null){
            dID=b.getString("dID");
            dName=b.getString("dName");
            pID=b.getString("pID");
        }
        // set lại title để biết vừa chọn cái gì
        getActivity().setTitle(getActivity().getTitle()+"/"+dName);
        //Truy cập vào data base để lấy dữ liệu ra
        getRealtimeData(pID,dID);
        return view;
    }

    public void getRealtimeData(final String pID, final String dID) {
        // chạy lên database
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        // truy cập vào đường dẫn
        CollectionReference collectionReference = db.collection(pID+"/"+dID+"/house");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable final FirebaseFirestoreException e) {
                // chỗ này để lấy hết tập con của cái house
                for (final QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.getId() != null) {
                        final String house = doc.getId();
                        System.out.println(house);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = db.collection(pID).document(dID).collection("house").document(house);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                    final String address=documentSnapshot.get("house_address").toString();
                                    final String price=documentSnapshot.get("house_price").toString();
                                    final String detail=documentSnapshot.get("house_detail").toString();
                                    ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("house_picture_id");
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    storageRef.child("images/" + list.get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri picture=uri;
                                            houses.add(new House(house,address,price,detail,picture));
//                                        // dùng adapter để đỏ dữ liệu lên
                                            house_adapter adapter = new house_adapter(House_Fragment.this.getContext(),houses);
                                            LinearLayoutManager manager = new LinearLayoutManager(House_Fragment.this.getContext());
                                            recyclerView.setLayoutManager(manager);
                                            recyclerView.setAdapter(adapter);
//
//                                        //bắt sự kiện click vào từng mục (void này phải viết thêm trong adapter)
                                            adapter.setOnItemClickedListener(new house_adapter.OnItemClickedListener() {
                                                @Override
                                                public void onItemClick(String ID) {
                                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                                    //phải Try catch chỗ này mới ko báo lỗi
                                                    try {
                                                        Fragment fragment = (Fragment) Room_info_Fragment.class.newInstance();
                                                        // đóng gói ID lấy đc từ adapter
                                                        Bundle bundle= new Bundle();
                                                        bundle.putString("pID",pID);
                                                        bundle.putString("dID",dID);
                                                        bundle.putString("hID",ID);
                                                        fragment.setArguments(bundle);
                                                        manager.beginTransaction().replace(R.id.flContent,fragment ).commit();
                                                    } catch (IllegalAccessException e1) {
                                                        e1.printStackTrace();
                                                    } catch (java.lang.InstantiationException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
