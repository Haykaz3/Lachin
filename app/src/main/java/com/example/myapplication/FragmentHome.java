package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProductService productService;
    private List<Product> productList;

    Button button, button2, button3, button4;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        recyclerView = view.findViewById(R.id.recyclerView);
        int itemWidthInDp = 190; // Desired item width in dp
        float screenWidthDp = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
        int spanCount = Math.max(1, (int) (screenWidthDp / itemWidthInDp));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2)); // 2 columns like your screenshot
        productService = new ProductServiceImpl();
        productList = new ArrayList<>();
        productService.getAll(new ProductService.ProductServiceCallback() {
            @Override
            public void onProductFetched(Product product) {}

            @Override
            public void onProductsFetched(List<Product> products) {
                if (!isAdded()) return; // prevent crash if fragment is not attached
                requireActivity().runOnUiThread(() -> {
                    adapter = new ProductAdapter(products, requireContext());
                    recyclerView.setAdapter(adapter);
                });
            }

            @Override
            public void onFailure(String error) {
                if (!isAdded()) return; // prevent crash
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Failed: " + error, Toast.LENGTH_SHORT).show()
                );
            }
        });

        return view;

    }
}