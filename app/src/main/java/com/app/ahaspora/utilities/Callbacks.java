package com.app.ahaspora.utilities;

import com.app.ahaspora.models.Post;

/**
 * Created by aangjnr on 05/08/2017.
 */

public class Callbacks {


    public interface AddressUpdateListener {

        void onAddressUpdated(String id, int position);

    }


    public interface UpdateFeedAdapterListener {

        void onFeedItemAdded(Post post);

    }


    public interface PopFragmentsBackStackListener {

        void onPopFragmentsBackStack();

    }


    public interface UpdateBottomSheetUiListener {

        void onPendingOrderAdded();

    }


    public interface UpdateMiniUiListener {

        void onCylinderUpdated();

    }
}
