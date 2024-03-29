package apps.everythingforward.com.wellnessdiary.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import apps.everythingforward.com.wellnessdiary.R;
import apps.everythingforward.com.wellnessdiary.Utility;

/**
 * Created by santh on 5/20/2017.
 */

public class TherapistsAdapter extends RecyclerView.Adapter<TherapistsAdapter.ViewHolder> {



    ArrayList<ParseObject> data;

    public TherapistsAdapter(List<ParseObject> objectList)
    {

        data = (ArrayList<ParseObject>)objectList;
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        RelativeLayout layout;
        LikeButton connect;



        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.therapistImage);
            name = (TextView)itemView.findViewById(R.id.therapistName);
            layout = (RelativeLayout)itemView.findViewById(R.id.relLayout);
            connect = (LikeButton)itemView.findViewById(R.id.connect_button);


        }
    }

    @Override
    public TherapistsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.therapistitem,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TherapistsAdapter.ViewHolder holder, int position) {

        final String name = data.get(position).getString(Utility.THERAPIST_NAME);
        final String description = data.get(position).getString(Utility.THERAPIST_DESCRIPTION);
        final String userName = data.get(position).getString(Utility.THERAPIST_USERNAME);


        holder.image.setImageDrawable(null);

        final ViewHolder holderFinal = holder;


        ParseFile file = data.get(position).getParseFile(Utility.THERAPIST_IMAGE);
        Log.e("TherapistsAdapter",file.getName());

        Picasso.with(holder.image.getContext())
                .load(file.getUrl())

                .placeholder(R.drawable.applogo100x100)
                .fit()
                .centerCrop()
                .into(holder.image);


/*        file.getUrl();

        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {


                Glide.with(holderFinal.image.getContext())


                        .load(data)

                        .apply(RequestOptions.fitCenterTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.applogo100x100))
                        .into(holderFinal.image);


            }
        });

        */



//        try {
//            byte[] data = file.getData();
//            Glide.with(holderFinal.image.getContext()).load(data).apply(RequestOptions.fitCenterTransform()).into(holderFinal.image);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }





        holderFinal.name.setText(name);

        final int positionVal = position;

        holderFinal.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialStyledDialog.Builder(holderFinal.layout.getContext())
                        .setTitle(name)
                        .setDescription(description)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setHeaderColor(R.color.colorPrimaryDark)
                        .withDialogAnimation(true)
                        .withIconAnimation(true)
                        .setScrollable(true)
                        .setCancelable(true)
                        //.setStyle(Style.HEADER_WITH_TITLE)
                        .show();
            }
        });


        ParseQuery<ParseObject> isLikedAlready = new ParseQuery<ParseObject>("Connections");
        isLikedAlready.whereEqualTo(Utility.CONNECTION_THERAPISTUSERNAME,userName);
        try {
            if(isLikedAlready.count()!=0)
            {
                holderFinal.connect.setLiked(true);

            }
            else
            {
                holderFinal.connect.setLiked(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holderFinal.connect.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                ParseObject object = new ParseObject("Connections");
                object.put(Utility.CONNECTION_USERUSERNAME, ParseUser.getCurrentUser().getEmail());
                object.put(Utility.CONNECTION_THERAPISTUSERNAME,userName);
                object.saveInBackground();

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Connections");
                query.whereEqualTo(Utility.CONNECTION_USERUSERNAME, ParseUser.getCurrentUser().getEmail());
                query.whereEqualTo(Utility.CONNECTION_THERAPISTUSERNAME,userName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for(ParseObject object : objects)
                        {
                            object.deleteInBackground();
                        }
                    }
                });




            }
        });

        holderFinal.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(holderFinal.connect.isLiked())
                {

                    new LovelyTextInputDialog(holderFinal.layout.getContext())
                            .setTopColorRes(R.color.material_deep_teal_50)
                            .setTitle("Message:")
                            .setMessage("Go ahead and ask your connection something!")

                            .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                                @Override
                                public void onTextInputConfirmed(String text) {


                                    Calendar calendar =Calendar.getInstance();
                                    String timeStamp=calendar.getTime().toString();



                                    ParseObject object = new ParseObject("Messages");

                                    object.put(Utility.MESSAGES_FROM,ParseUser.getCurrentUser().getEmail());
                                    object.put(Utility.MESSAGES_TO,userName);
                                    object.put(Utility.MESSAGE_CONTENT,text);
                                    object.put(Utility.MESSAGE_TIME,timeStamp);
                                    object.put(Utility.MESSAGE_SENDERNAME,ParseUser.getCurrentUser().getString("name"));
                                    object.saveInBackground();
                                    Toast.makeText(holderFinal.layout.getContext(), "Sent message!", Toast.LENGTH_SHORT).show();



                                }
                            })
                            .show();


                }

                return false;
            }
        });





    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
