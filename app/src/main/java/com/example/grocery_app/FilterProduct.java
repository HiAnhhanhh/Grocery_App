//package com.example.grocery_app;
//
//import android.widget.Filter;
//
//import java.util.ArrayList;
//
//public class FilterProduct extends Filter {
//
//    private ProductAdapter adapter;
//    private ArrayList<ProductModels> filterList;
//
//    public FilterProduct(ProductAdapter adapter, ArrayList<ProductModels> filterList) {
//        this.adapter = adapter;
//        this.filterList = filterList;
//    }
//
//    @Override
//    protected FilterResults performFiltering(CharSequence constraint) {
//        FilterResults filterResults = new FilterResults();
//        if(constraint != null && constraint.length() > 0){
//            constraint = constraint.toString().toUpperCase();
//            ArrayList<ProductModels> filteredModels = new ArrayList<>();
//            for( int i=0; i<filterList.size();i++){
//                if(filterList.get(i).getTitle().toUpperCase().contains(constraint) || filterList.get(i).getCategory().toUpperCase().contains(constraint) ){
//                    filteredModels.add(filterList.get(i));
//                }
//            }
//            filterResults.count= filteredModels.size();
//            filterResults.values= filteredModels;
//        }else{
//            filterResults.count= filterList.size();
//            filterResults.values= filterList;
//        }
//        return null;
//    }
//
//    @Override
//    protected void publishResults(CharSequence constraint, FilterResults results) {
//        adapter.productModelsArrayList = (ArrayList<ProductModels>) results.values;
//        adapter.notifyDataSetChanged();
//    }
//}
