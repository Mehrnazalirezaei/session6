package ir.roojano.sematec.session6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.RecyclerViewHolder> {

    List<Search> moviesList;
    String whichActivity;

    public RecyclerAdapter(List<Search> moviesList, String whichActivity) {
        this.moviesList = moviesList;
        this.whichActivity = whichActivity;
    }

    public void clear() {
        int size = moviesList.size();
        moviesList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.txtTitle.setText(moviesList.get(position).getTitle());
        holder.txtType.setText(moviesList.get(position).getType());
        holder.txtYear.setText(moviesList.get(position).getYear());
        Glide.with(holder.itemView)
                .load(moviesList.get(position).getPoster())
                .centerCrop()
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtType;
        TextView txtYear;
        ImageView imgPoster;
        ImageView imgAddFav;
        ImageView imgAddedFav;
        ImageView imgDeleteFav;

        public RecyclerViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtType = itemView.findViewById(R.id.txtType);
            txtYear = itemView.findViewById(R.id.txtYear);
            imgPoster =itemView.findViewById(R.id.imgPoster);
            imgAddFav = itemView.findViewById(R.id.imgAddFav);
            imgAddedFav = itemView.findViewById(R.id.imgAddedFav);
            imgDeleteFav = itemView.findViewById(R.id.imgDeleteFav);

            if (whichActivity.matches("FavoriteMovies"))
            {
                imgAddFav.setVisibility(View.GONE);
                imgAddedFav.setVisibility(View.GONE);
                imgDeleteFav.setVisibility(View.VISIBLE);
            }

            imgAddFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteHelper helper = new SQLiteHelper(itemView.getContext(), "IMDB_Movies", null, 1);
                    helper.insertMovie(txtTitle.getText().toString(), txtType.getText().toString(), txtYear.getText().toString(), moviesList.get(getAdapterPosition()).getPoster(), moviesList.get(getAdapterPosition()).getImdbID());
                    helper.close();
                    Toast.makeText(itemView.getContext(), txtTitle.getText().toString() + " Save!", Toast.LENGTH_SHORT).show();
                    imgAddFav.setVisibility(View.GONE);
                    imgAddedFav.setVisibility(View.VISIBLE);
                }
            });

            imgAddedFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteHelper helper = new SQLiteHelper(itemView.getContext(), "IMDB_Movies", null, 1);
                    helper.deleteMovie(moviesList.get(getAdapterPosition()).getImdbID());
                    helper.close();
                    Toast.makeText(itemView.getContext(), moviesList.get(getAdapterPosition()).getTitle() + " Deleted from offline!", Toast.LENGTH_SHORT).show();
                    imgAddedFav.setVisibility(View.GONE);
                    imgAddFav.setVisibility(View.VISIBLE);
                }
            });

            imgDeleteFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteHelper helper = new SQLiteHelper(itemView.getContext(), "IMDB_Movies", null, 1);
                    helper.deleteMovie(moviesList.get(getAdapterPosition()).getImdbID());
                    helper.close();
                    Toast.makeText(itemView.getContext(), moviesList.get(getAdapterPosition()).getTitle() + " Deleted from offline!", Toast.LENGTH_SHORT).show();
                    moviesList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }
}