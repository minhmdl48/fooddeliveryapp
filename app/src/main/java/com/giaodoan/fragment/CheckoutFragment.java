package com.giaodoan.fragment;

import androidx.fragment.app.Fragment;

import com.giaodoan.databinding.CheckoutFragmentBinding;
import com.giaodoan.model.ItemOrder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {
//    private CheckoutFragmentBinding binding;
//    private ArrayList<ItemOrder> cartList;
//    private FirebaseAuth auth;
//    private JudulOnlyAdapter adapter;
//    private int subTotalPrice = 10000;
//    private int totalPrice = 0;
//
//    private CollectionReference orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders");
//    private CollectionReference pesananDatabaseReference = FirebaseFirestore.getInstance().collection("pesanan");
//
//    private AlertDialog.Builder builder;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = FragmentCheckoutBinding.inflate(inflater);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        binding = FragmentCheckoutBinding.bind(view);
//        auth = FirebaseAuth.getInstance();
//
//        //HIDE NAV BOTTOM
//        BottomNavigationView bottomNavigation =
//                requireActivity().findViewById(R.id.bottom_navigationView);
//        bottomNavigation.setVisibility(View.GONE);
//
//        binding.checkoutBackButton.setNavigationOnClickListener(v -> Navigation.findNavController(requireView()).popBackStack());
//    }
//    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//    ArrayList<Judul> cartList = new ArrayList<>();
//
//    retrieveCartItems();
//
//    JudulOnlyAdapter adapter = new JudulOnlyAdapter(context, cartList);
//binding.rvJudul.setAdapter(adapter);
//binding.rvJudul.setLayoutManager(layoutManager);
//
//    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//binding.kotakSend.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            builder.setTitle("Pengiriman")
//                    .setMessage("Saat ini kami hanya menerima pengiriman melalui GoSend sahaja")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    }).show();
//        }
//    });
//
//binding.checkoutBuatPesanan.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(getActivity(), "Wah! kamu baru saja memesan dengan total Rp " + subTotalPrice, Toast.LENGTH_LONG).show();
//
//            binding.keranjangSubtotal.setText("0");
//
//            deleteCart(cartList.get(0));
//            addToPesanan(cartList.get(0));
//
//            cartList.clear();
//            adapter.notifyDataSetChanged();
//
//            Navigation.findNavController(v).popBackStack();
//            Navigation.findNavController(v).popBackStack();
//        }
//    });
//    private void retrieveCartItems() {
//        orderDatabaseReference
//                .whereEqualTo("uid", auth.getCurrentUser().getUid())
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    for (QueryDocumentSnapshot item : querySnapshot) {
//                        KeranjangModel cartProduct = item.toObject(KeranjangModel.class);
//
//                        cartList.add(cartProduct);
//                        subTotalPrice += Integer.parseInt(cartProduct.getPrice());
//                        totalPrice += Integer.parseInt(cartProduct.getPrice());
//
//                        binding.keranjangSubtotal.setText("Rp " + subTotalPrice);
//                        adapter.notifyDataSetChanged();
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//    private void deleteCart(KeranjangModel keranjang) {
//        CoroutineScope scope = new CoroutineScope(Dispatchers.IO);
//        scope.launch(() -> {
//            QuerySnapshot personQuery = orderDatabaseReference
//                    .whereEqualTo("uid", keranjang.getUid())
//                    .get()
//                    .await();
//            if (!personQuery.isEmpty()) {
//                for (QueryDocumentSnapshot document : personQuery) {
//                    try {
//                        orderDatabaseReference.document(document.getId()).delete().await();
//                        Toast.makeText(getContext(), "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Dispatchers.Main.dispatch(() -> {
//                            // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                        });
//                    }
//                }
//            } else {
//                Dispatchers.Main.dispatch(() -> {
//                    Toast.makeText(getContext(), "Cart tidak ditemukan di database", Toast.LENGTH_SHORT).show();
//                });
//            }
//        });
//    }
//
//    private void addToPesanan(KeranjangModel keranjang) {
//        PesananModel pesanan = new PesananModel(keranjang.getUid(), "Sedang Dikirim", String.valueOf(subTotalPrice));
//        pesananDatabaseReference.add(pesanan).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Toast.makeText(getContext(), "Berhasil Menambahkan ke Pesanan", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//

}
