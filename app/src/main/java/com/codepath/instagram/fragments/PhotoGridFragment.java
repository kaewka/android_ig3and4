package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPhotosAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PhotoGridFragment extends Fragment {
    private static final String TAG = "PhotoGridFragment";

    private static final int NUM_COLUMNS = 3;

    private static final String ARG_USER_ID = "userId";
    private static final String ARG_TAG = "tag";

    private InstagramPhotosAdapter instagramPhotosAdapter;

    RecyclerView rvPhotoGrid;

    private String userId;
    private String tag;

    public static PhotoGridFragment newInstance(String userId, String tag) {
        PhotoGridFragment fragment = new PhotoGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putString(ARG_TAG, tag);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
            tag = getArguments().getString(ARG_TAG);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_grid, container, false);
        rvPhotoGrid = (RecyclerView) view.findViewById(R.id.rvPhotoGrid);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), NUM_COLUMNS);
        rvPhotoGrid.setLayoutManager(layoutManager);

        List<InstagramPost> posts = new ArrayList<>();
        instagramPhotosAdapter = new InstagramPhotosAdapter(posts);
        rvPhotoGrid.setAdapter(instagramPhotosAdapter);

        fetchPhotos();
        return view;
    }

    private void fetchPhotos() {
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                instagramPhotosAdapter.replaceAll(Utils.decodePostsFromJsonResponse(response));
            }
        };

        if (userId != null) {
            MainApplication.getRestClient().getUserRecentMedia(userId, responseHandler);
        } else if (tag != null) {
            MainApplication.getRestClient().getTagRecentMedia(tag, responseHandler);
        }
    }
}
