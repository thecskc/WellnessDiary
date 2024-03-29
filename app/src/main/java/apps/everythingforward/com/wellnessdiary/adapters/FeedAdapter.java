package apps.everythingforward.com.wellnessdiary.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import apps.everythingforward.com.wellnessdiary.FeedItemActivity;
import apps.everythingforward.com.wellnessdiary.R;
import apps.everythingforward.com.wellnessdiary.Utility;

/**
 * Created by santh on 5/19/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private ArrayList<ParseObject> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView articleImage;
        TextView articleTitle,articleDescription;
        CardView feedCard;
        public ViewHolder(View itemView) {
            super(itemView);
            articleImage= (ImageView) itemView.findViewById(R.id.articleIV);
            articleTitle=(TextView)itemView.findViewById(R.id.titleTV);
            articleDescription=(TextView)itemView.findViewById(R.id.descriptionTV);
            feedCard = (CardView)itemView.findViewById(R.id.feedcard);



        }
    }

    public FeedAdapter(List<ParseObject> objects)
    {
        data= (ArrayList<ParseObject>) objects;

    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedcards,parent,false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(FeedAdapter.ViewHolder holder, final int position) {

        String imageURL = data.get(position).getString(Utility.FEED_ARTICLEIMAGE);
        Picasso.with(holder.articleImage.getContext()).load(imageURL).fit().centerCrop()
                .into(holder.articleImage);

        holder.articleTitle.setText(data.get(position).getString(Utility.FEED_ARTICLETITLE));

        holder.articleDescription.setText(data.get(position).getString(Utility.FEED_ARTICLEDESCRIPTION));

        holder.feedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FeedItemActivity.class);
                Number ID = data.get(position).getNumber(Utility.FEED_ITEMID);

        //        Toast.makeText(view.getContext(), String.valueOf(ID.intValue()), Toast.LENGTH_SHORT).show();
                intent.putExtra(Intent.EXTRA_TEXT,ID.intValue());
                view.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
