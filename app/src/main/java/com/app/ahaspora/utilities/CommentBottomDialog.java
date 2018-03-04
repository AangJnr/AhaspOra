package com.app.ahaspora.utilities;

/**
 * Created by aangjnr on 12/02/2018.
 */
import android.Manifest;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.ahaspora.R;
import com.app.ahaspora.activities.DetailedEventActivity;
import com.app.ahaspora.fragments.BaseFragment;
import com.app.ahaspora.models.Recommendation;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class
CommentBottomDialog extends BottomSheetDialogFragment {

    View view;
    public RelativeLayout imageLayout;
    public ImageView selectedImage;
    public ImageView removeImage;
    public Uri imageUri;
    public EditText commentEditText;
    public TextView postButton;
    public CircleImageView usersImage;
    public ProgressBar progressBar;


    public static CommentBottomDialog getInstance() {


        return new CommentBottomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.write_something_layout, container, false);





         commentEditText = view.findViewById(R.id.comment_edittext);
        postButton = view.findViewById(R.id.post);
        usersImage = view.findViewById(R.id.user_image);
        progressBar = view.findViewById(R.id.progress_bar);


        selectedImage = view.findViewById(R.id.select_image);
        imageLayout = view.findViewById(R.id.imageRl);
        selectedImage = view.findViewById(R.id.selectedImage);
        removeImage = view.findViewById(R.id.removeImage);

        return view;
    }




}