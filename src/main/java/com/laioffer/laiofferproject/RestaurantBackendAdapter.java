package com.laioffer.laiofferproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by daisy on 11/29/2017.
 */

public class RestaurantBackendAdapter extends BaseAdapter {
    private Context context;
    private List<Restaurant> restaurantData;
    private DataService dataService;

    public RestaurantBackendAdapter(Context context, List<Restaurant> restaurantData) {
        this.context = context;
        this.restaurantData = restaurantData;
        dataService = new DataService();
    }

    @Override
    public int getCount() {
        return restaurantData.size();
    }

    @Override
    public Restaurant getItem(int position) {
        return restaurantData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.restaurant_backend_item,
                    parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.restaurantName = (TextView) rowView.findViewById(R.id.restaurant_backend_name);
            viewHolder.restaurantCategory = (TextView) rowView.findViewById(R.id.
                    restaurant_backend_category);
            viewHolder.restaurantStreet = (TextView) rowView.findViewById(R.id.restaurant_backend_street);
            viewHolder.restaurantCity = (TextView) rowView.findViewById(R.id.restaurant_backend_city);
            viewHolder.restaurantState = (TextView) rowView.findViewById(R.id.restaurant_backend_state);
            viewHolder.restaurantFavorate = (ImageView) rowView.findViewById(R.id.restaurant_backend_favorate);
            viewHolder.restaurantImage = (ImageView) rowView.findViewById(R.id.restaurant_backend_image);
            viewHolder.restaurantRatingBar = (RatingBar)rowView.findViewById(R.id.restaurant_backend_ratingbar);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        final Restaurant restaurant = restaurantData.get(position);

        holder.restaurantName.setText(restaurant.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("category");
        for (String category : restaurant.getCategories()) {
            builder.append(category + ", ");
        }

        holder.restaurantCategory.setText(builder.toString());
        String[] address = restaurant.getAddress().split(",");
        if (address.length >= 3) {
            holder.restaurantStreet.setText(address[0]);
            holder.restaurantCity.setText(address[1]);
            holder.restaurantState.setText(address[2]);
        }

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Clock  clock = new Clock();
                clock.start();
                Bitmap bitmap =  dataService.getBitmapFromURL(restaurant.getUrl());
                clock.stop();
                Log.e("Image loading time", clock.getCurrentInterval() + "");
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.restaurantImage.setImageBitmap(bitmap);
            }
        }.execute();

        holder.restaurantRatingBar.setRating((float)restaurant.getStars());

        holder.restaurantFavorate.setImageResource(
                restaurant.isFavorate() ?
                        R.mipmap.ic_favorite_black_24dp:
                        R.mipmap.ic_favorite_border_black_24dp
        );
        return rowView;
    }

    static class ViewHolder{
        public TextView restaurantName;
        public TextView restaurantCategory;
        public TextView restaurantStreet;
        public TextView restaurantCity;
        public TextView restaurantState;

        public ImageView restaurantImage;
        public ImageView restaurantFavorate;
        public RatingBar restaurantRatingBar;
    }


}
