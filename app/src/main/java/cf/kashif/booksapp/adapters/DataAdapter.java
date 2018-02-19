package cf.kashif.booksapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cf.kashif.booksapp.R;
import cf.kashif.booksapp.pojo.Book;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    //This is a recyclerview adapter to bind data to recyclerview

    Context context;
    public static List<Book> booksList = new ArrayList<>();
    final String TAG = "MyLog " + this.getClass().getSimpleName();

    public DataAdapter(Context context, List<Book> booksList) {

        this.booksList = booksList;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return booksList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout rvBookRow;
        protected ImageView ivBookCover;
        protected TextView tvTitle, tvAuthor, tvPublisher;
        
        public MyViewHolder(View v) {
            super(v);
            rvBookRow = (RelativeLayout) v.findViewById(R.id.rvBookRow);
            ivBookCover = (ImageView) v.findViewById(R.id.ivBookCover);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle); 
            tvAuthor = (TextView) v.findViewById(R.id.tvAuthor); 
            tvPublisher = (TextView) v.findViewById(R.id.tvPublisher);
        }

    }//ViewHolder



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder paramHolder, final int position) {

        Glide.with(context)
                .load( booksList.get(position).volumeInfo.imageLinks.getThumbnail() )
                .centerCrop()
                .crossFade()
                .thumbnail(0.5f)
                .into( paramHolder.ivBookCover );

        paramHolder.tvTitle.setText( booksList.get(position).volumeInfo.getTitle() );
        paramHolder.tvAuthor.setText( booksList.get(position).volumeInfo.getAuthors().toString() );
        paramHolder.tvPublisher.setText( booksList.get(position).volumeInfo.getPublisher() );

        paramHolder.rvBookRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( booksList.get(position).volumeInfo.getPreviewLink() )));

            }
        });

    }



}


