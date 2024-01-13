package com.giaodoan.Controller;

import static android.content.ContentValues.TAG;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.giaodoan.R;
import com.giaodoan.databinding.DetailItemFragmentBinding;
import com.giaodoan.model.Item;
import com.giaodoan.model.ItemOrder;
import com.giaodoan.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderController extends Fragment {

    private DetailItemFragmentBinding binding;
    private CollectionReference collectionReference;
    private DatabaseReference cartDatabase;

    private CollectionReference orderDatabaseReference;

    private FirebaseAuth auth;
    private ArrayList<Item> itemList = new ArrayList<>();
    private ArrayList<Order> orderList = new ArrayList<>();

    private String currentUID;
    private String orderImageUrl;
    private String orderName;
    private int orderQuantity;
    private String orderPrice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        collectionReference = FirebaseFirestore.getInstance().collection("products");
        cartDatabase = FirebaseDatabase.getInstance("https://acofee-order-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");
        this.orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");
        currentUID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    }

    //Người dùng tạo đơn
    private void createOrder(ItemOrder[] orderedProducts, String timerTime, String note ) {
        float total = countMoneyFromCart(orderedProducts);
        String strTotal = Float.toString(total);
        String uid = currentUID;
        Order order = new Order(
                uid,
                "orderId", // orderId để phân biệt các đơn hàng ( Primary Key)
                //1: Mới tạo
                //2: Admin đã chấp nhận -> đơn đc chấp nhận
                //3: Hủy
                //4: Hoàn thành
                "1", //Trạng thái của đơn hàng
                strTotal,
                "",
                timerTime
        );

        this.orderDatabaseReference.add(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Thành công tạo Order!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Tính tổng tiền cho đơn hàng
    public float countMoneyFromCart(ItemOrder[] orders) {
        float total = 0;
        for(int i =0 ; i < orders.length; i++) {
            float curPrice = parseFloat(orders[i].getPrice());
            total += curPrice;
        }
        return total;
    }

    //Người dùng hủy đơn: Thay đổi trạng thái order thành 3 với id của order
    public void UserCancelOrder(String oid){
        this.orderDatabaseReference.document(oid).update("status", 3).addOnSuccessListener(aVoid ->{
            Log.d("CancelOrder","CancelOrder: "+oid);
            Toast.makeText(requireContext(), "Hủy đơn hàng" + oid + "thành công", Toast.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show());;
    }

    //Người dùng lấy danh sách order
    public void UserGetOrders(String uid) {
        this.orderDatabaseReference.whereEqualTo("userId",uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Order order = document.toObject(Order.class);
                    orderList.add(order);
                }

                if (!orderList.isEmpty()) {
                    //binding...
                }

            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //admin lấy danh sách order
    public ArrayList<Order> AdminGetOrders(CollectionReference orderDatabaseReference) {
        orderList = new ArrayList<Order>();
        orderDatabaseReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                orderList.add(document.toObject(Order.class));
                            }
                            Log.d("abcd12345", String.valueOf(orderList.size()));

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
        });

        return orderList;
    }

    //admin chấp nhận đơn
    public void AdminAcceptOrder(String oid){
        this.orderDatabaseReference.document(oid).update("status", 2).addOnSuccessListener(aVoid ->{
                    Log.d("AcceptOrder","AcceptOrder: "+oid);
                    Toast.makeText(requireContext(), "Xác nhận đơn hàng" + oid + "thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show());;
    }

    //admin hoàn thành đơn
    public void AdminFinishOrder(String oid){
        this.orderDatabaseReference.document(oid).update("status", 4).addOnSuccessListener(aVoid ->{
                    Log.d("FinishOrder","FinishOrder: "+oid);
                    Toast.makeText(requireContext(), "Hoàn thành đơn hàng" + oid + "thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show());;
    }

    //Thanh toán
}

