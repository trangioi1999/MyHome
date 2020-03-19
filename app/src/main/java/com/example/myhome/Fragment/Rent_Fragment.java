package com.example.myhome.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myhome.Adapter.house_adapter;
import com.example.myhome.Model.House;
import com.example.myhome.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rent_Fragment extends Fragment {
    RecyclerView recyclerView;
    Button rent;
    String ID;
    ArrayList<House> houses = new ArrayList<House>();

    public Rent_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_rent, container, false);
        recyclerView=view.findViewById(R.id.rent_recycleview);
        rent=view.findViewById(R.id.rent);
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    //phải Try catch chỗ này mới ko báo lỗi
                        try {
                        Fragment fragment = (Fragment) Create_house_Fragment.class.newInstance();
                        manager.beginTransaction().replace(R.id.flContent,fragment ).commit();
                    } catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    } catch (java.lang.InstantiationException e1) {
                        e1.printStackTrace();
                    }

            }
        });
        // recycleview

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn){
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                ID=object.getString("id");
                                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                final DocumentReference documentReference = db.document("user/"+ID);
                                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        ArrayList<String> rent_id= (ArrayList<String>) documentSnapshot.get("house_id");
                                        for (final String rID:rent_id){
                                            DocumentReference documentRef = db.document(rID);
                                            documentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                    if (documentSnapshot != null && documentSnapshot.exists()) {
                                                        final String address=documentSnapshot.get("house_address").toString();
                                                        final String price=documentSnapshot.get("house_price").toString();
                                                        final String detail=documentSnapshot.get("house_detail").toString();
                                                        final String house=documentSnapshot.getId();
                                                        ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("house_picture_id");
                                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                                        storageRef.child("images/" + list.get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                Uri picture=uri;
                                                                houses.add(new House(house,address,price,detail,picture));
                                                                // dùng adapter để đỏ dữ liệu lên
                                                                house_adapter adapter = new house_adapter(Rent_Fragment.this.getContext(),houses);
                                                                LinearLayoutManager manager = new LinearLayoutManager(Rent_Fragment.this.getContext());
                                                                recyclerView.setLayoutManager(manager);
                                                                recyclerView.setAdapter(adapter);

                                                                //bắt sự kiện click vào từng mục (void này phải viết thêm trong adapter)
                                                                adapter.setOnItemClickedListener(new house_adapter.OnItemClickedListener() {
                                                                    @Override
                                                                    public void onItemClick(String ID) {
                                                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                                                        //phải Try catch chỗ này mới ko báo lỗi
                                                                        try {
                                                                            Fragment fragment = (Fragment) BlankFragment.class.newInstance();
                                                                            // đóng gói ID lấy đc từ adapter
                                                                            Bundle bundle= new Bundle();
                                                                            bundle.putString("like",rID);
                                                                            bundle.putString("id",ID);
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
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name");
            request.setParameters(parameters);
            request.executeAsync();
        }
        return view;
    }

}
