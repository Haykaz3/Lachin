public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, priceTextView, originalPriceTextView, discountTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            originalPriceTextView = itemView.findViewById(R.id.originalPriceTextView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
        }
    }

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.imageView.setImageResource(product.getImageResId());
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText("$" + product.getPrice());

        if (product.getOriginalPrice() != null) {
            holder.originalPriceTextView.setText("$" + product.getOriginalPrice());
            holder.originalPriceTextView.setVisibility(View.VISIBLE);
        } else {
            holder.originalPriceTextView.setVisibility(View.GONE);
        }

        if (product.getDiscountPercent() != null) {
            holder.discountTextView.setText(product.getDiscountPercent() + "%");
            holder.discountTextView.setVisibility(View.VISIBLE);
        } else {
            holder.discountTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
