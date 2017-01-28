package com.iantmeyer.sample.simplar.ar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iantmeyer.sample.simplar.R;
import com.iantmeyer.simplar.ArView;
import com.iantmeyer.simplar.camera.CameraPermissionCallback;
import com.iantmeyer.simplar.render.ArRenderer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArFragment extends Fragment implements ArMvp.View {

    private static final String TAG = "ARFragment";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private ArMvp.Presenter mPresenter;

    @BindView(R.id.ar_view)
    protected ArView mArView;

    public ArFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ar, container, false);
        ButterKnife.bind(this, view);

        mPresenter = new ArPresenter(getContext(), this);
        mPresenter.loadRenderers();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        mArView.startCamera(mCameraCallback);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        mArView.stopCamera();
    }

    @OnClick(R.id.ar_view)
    protected void onArViewClick() {
        mArView.setNorth();
        Toast.makeText(getContext(), "North Set!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayRenderers(@NonNull List<ArRenderer> arRendererList) {
        mArView.addRendererList(arRendererList);
    }

    private CameraPermissionCallback mCameraCallback = new CameraPermissionCallback() {
        @Override
        public void requestCameraPermission() {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(getActivity())
                        // TODO
                        .setMessage("Request Camera Permission")
                        .setPositiveButton(
                                android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(
                                                new String[]{Manifest.permission.CAMERA},
                                                REQUEST_CAMERA_PERMISSION);
                                    }
                                }
                        )
                        .setNegativeButton(
                                android.R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO IM
                                        Toast.makeText(getContext(), "Camera Permission Required", Toast.LENGTH_LONG).show();
                                    }
                                }
                        )
                        .create();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        }
    };
}