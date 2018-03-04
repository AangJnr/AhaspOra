package com.app.ahaspora;

/**
 * Created by aangjnr on 12/02/2018.
 */
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.ahaspora.adapters.RecommendationsAdapter;
import com.app.ahaspora.fragments.BaseFragment;
import com.app.ahaspora.models.Recommendation;
import com.app.ahaspora.utilities.Constants;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayowa.adegeye on 28/06/2016.
 */
public class BottomDialog extends BottomSheetDialogFragment {


    static Recommendation recommendation;


    View view;
    public static BottomDialog getInstance(Object o, String type) {

        if(type.equalsIgnoreCase(Constants.TYPE_RECOMMENDATION))
        recommendation = (Recommendation) o;


        return new BottomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        view = inflater.inflate(R.layout.custom_bottom_dialog, container, false);
        TextView title = view.findViewById(R.id.title);
        TextView contactName = view.findViewById(R.id.contact);
        TextView body = view.findViewById(R.id.body);

        TextView category = view.findViewById(R.id.genre);

        String categoryName = recommendation.getCategory().getName();
        category.setText(categoryName);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(1000);
        drawable.setColor(BaseFragment.getColor(categoryName));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            category.setBackground(drawable);
        }else {
            category.setBackgroundDrawable(drawable);
        }

       // category.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), BaseFragment.getDrawable(categoryName)), null, null, null );




        CircleImageView circleImageView = view.findViewById(R.id.profile_photo);

        title.setText(recommendation.getTitle());
        contactName.setText(recommendation.getContact().getName());


        body.setText(recommendation.getBody());

            if(recommendation.getImage() != null && !recommendation.getImage().equalsIgnoreCase("null")
                    && !recommendation.getImage().equalsIgnoreCase(""))
                Picasso.with(getActivity())
                        .load(recommendation.getImage())
                        .resize(100, 100)
                        .centerCrop()
                        .into(circleImageView);




        return view;
    }
}