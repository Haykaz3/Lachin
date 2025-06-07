package com.example.myapplication;

import static android.view.View.INVISIBLE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdd extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGES_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText nameInput, descriptionInput, priceInput, cityInput, regionInput, statusInput, categoryIdInput, userIdInput;
    private Button submitButton;

    private ImageButton selectImagesButton;
    private Spinner categorySpinner;

    private TextView text;
    private LinearLayout attributesContainer;
    private List<Category> categoryList;
    private Map<Integer, EditText> attributeInputs = new HashMap<>();

    private CategoryService categoryService;
    private AttributeService attributeService;
    private ProductService productService;

    private ImageView image;

    LinearLayout imageContainer;

    private List<Uri> selectedImageUris = new ArrayList<>();

    public FragmentAdd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAdd.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAdd newInstance(String param1, String param2) {
        FragmentAdd fragment = new FragmentAdd();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        categorySpinner = view.findViewById(R.id.spinnerCategory);
        attributesContainer = view.findViewById(R.id.attributesContainer);
        nameInput = view.findViewById(R.id.etName);
        descriptionInput = view.findViewById(R.id.etDescription);
        priceInput = view.findViewById(R.id.etPrice);
        cityInput = view.findViewById(R.id.etCity);
        regionInput = view.findViewById(R.id.etRegion);
        statusInput = view.findViewById(R.id.etStatus);

        imageContainer = view.findViewById(R.id.image_container);


        text = view.findViewById(R.id.text);
        //categoryIdInput = view.findViewById(R.id.cate);

        selectImagesButton = view.findViewById(R.id.btnSelectImages);
        submitButton = view.findViewById(R.id.btnSubmit);


        categoryService = new CategoryServiceImpl(); // Implement this
        attributeService = new com.example.myapplication.AttributeServiceImpl(); // Implement this
        productService = new ProductServiceImpl();

        loadCategories();

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedCategoryId = categoryList.get(position).id;
                loadAttributesForCategory(selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        selectImagesButton.setOnClickListener(v -> openImagePicker());
        submitButton.setOnClickListener(v -> submitProduct());

        return view;
    }
    private void openImagePicker()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK && data != null)
        {
            selectedImageUris.clear();
            if (data.getClipData() != null)
            {
                for (int i = 0; i < data.getClipData().getItemCount(); i++)
                {
                    selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                }

            } else if (data.getData() != null) {
                selectedImageUris.add(data.getData());

            }
            Toast.makeText(getContext(), selectedImageUris.size() + "image(s) selected", Toast.LENGTH_SHORT).show();

            text.setVisibility(INVISIBLE);
            imageContainer.removeAllViews(); // Optional: clear previous images if needed

            for (Uri imageUri : selectedImageUris) {
                // Create a CardView
                CardView cardView = new CardView(getActivity());
                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(16, 0, 16, 0); // spacing between cards
                cardView.setLayoutParams(cardParams);
                cardView.setRadius(16f);
                cardView.setCardElevation(8f);

                // Create an ImageView inside the CardView
                ImageView imageView = new ImageView(getActivity());
                int height = imageContainer.getHeight();
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(height, height);
                imageView.setLayoutParams(imageParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // Load image with Glide
                Glide.with(this)
                        .load(imageUri)
                        .into(imageView);

                // Add ImageView to CardView
                cardView.addView(imageView);

                // Add CardView to the container
                imageContainer.addView(cardView);
            }
        }
    }
    private void loadCategories()
    {
        categoryService.getCategories(new CategoryService.CategoryServiceCallback() {
            @Override
            public void onCategoriesFetched(List<Category> categories) {
                categoryList = categories;

                ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categories
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(String error) {
                // Show error message
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadAttributesForCategory(int categoryId)
    {
        attributesContainer.removeAllViews();
        attributeInputs.clear();

        attributeService.getAttributesByCategory(categoryId, new AttributeService.AttributeCallback() {
            @Override
            public void onAttributesFetched(List<AttributeDefinition> attributes) {
                for (AttributeDefinition attribute : attributes)
                {
                    TextView label = new TextView(getContext());
                    label.setText(attribute.name);

                    EditText input = new EditText(getContext());
                    input.setHint("Enter " + attribute.name);

                    attributesContainer.addView(label);
                    attributesContainer.addView(input);
                    attributeInputs.put(attribute.id, input);
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void submitProduct()
    {
        ProductDTO product = new ProductDTO();
        product.name = nameInput.getText().toString();
        product.description = descriptionInput.getText().toString();
        product.price = Double.parseDouble(priceInput.getText().toString());
        product.city = cityInput.getText().toString();
        product.region = regionInput.getText().toString();
        product.createdByUserId = "80df53cc-c6c9-4b9f-a4b6-9a95732542dc";
        product.status = statusInput.getText().toString();
        //Category category = (Category) categorySpinner.getSelectedItem();
        //product.categoryId = category.id;
        Object selectedItem = categorySpinner.getSelectedItem();
        if (selectedItem instanceof Category) {
            Category selectedCategory = (Category) selectedItem;
            int categoryId = selectedCategory.id;
            product.categoryId = categoryId;
        } else {
            Toast.makeText(getContext(), "Please select a valid category", Toast.LENGTH_SHORT).show();
        }
        List<AttributeDTO> attributeDTOS = new ArrayList<>();
        for (Map.Entry<Integer, EditText> entry : attributeInputs.entrySet())
        {
                    int attrId = entry.getKey();
                    String value = entry.getValue().getText().toString().trim();
                    if (!value.isEmpty())
                    {
                        attributeDTOS.add(new AttributeDTO(attrId, value));
                    }
        }
        product.attributes = attributeDTOS;
        List<File> imageFiles = UriFileConverter.urisToFiles(requireContext(), selectedImageUris);
        productService.addProduct(product, imageFiles, new ProductService.ProductAddCallback() {
            @Override 
            public void onSuccess() {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
                    // You can also navigate or clear the form here
                });
            }

            @Override
            public void onFailure(String error) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Failed to add product: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}