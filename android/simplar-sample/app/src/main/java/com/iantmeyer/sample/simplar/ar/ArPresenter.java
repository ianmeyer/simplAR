package com.iantmeyer.sample.simplar.ar;

import android.content.Context;

import com.iantmeyer.sample.simplar.renderers.CompassRenderer;
import com.iantmeyer.sample.simplar.renderers.CubeRenderer;
import com.iantmeyer.sample.simplar.renderers.FloorRenderer;
import com.iantmeyer.sample.simplar.renderers.HorizonLabelRenderer;
import com.iantmeyer.sample.simplar.renderers.HorizonRenderer;
import com.iantmeyer.simplar.render.ArRenderer;

import java.util.ArrayList;
import java.util.List;

class ArPresenter implements ArMvp.Presenter {

    private final Context mContext;

    private final ArMvp.View mArView;

    ArPresenter(Context context, ArMvp.View arView) {
        mContext = context.getApplicationContext();
        mArView = arView;
    }

    @Override
    public void loadRenderers() {

        List<ArRenderer> rendererList = new ArrayList<>();

        // HorizonRenderer + CompassRenderer
        rendererList.add(new HorizonRenderer(mContext));
        rendererList.add(new CompassRenderer(mContext));

        float PI = (float) (Math.PI);
        rendererList.add(new HorizonLabelRenderer(mContext, "NORTH", 0f, 0f));
        rendererList.add(new HorizonLabelRenderer(mContext, "EAST", PI / 2, 0f));
        rendererList.add(new HorizonLabelRenderer(mContext, "SOUTH", PI, 0f));
        rendererList.add(new HorizonLabelRenderer(mContext, "WEST", 3 * PI / 2, 0f));

        rendererList.add((new HorizonLabelRenderer(mContext, "NORTHEAST", PI / 4, 0f)));
        rendererList.add((new HorizonLabelRenderer(mContext, "SOUTHEAST", 3 * PI / 4, 0f)));
        rendererList.add((new HorizonLabelRenderer(mContext, "SOUTHWEST", -PI / 2, 0f)));
        rendererList.add((new HorizonLabelRenderer(mContext, "NORTHWEST", -3 * PI / 4, 0f)));

        // CubeRenderer on Ground
        rendererList.add(new CubeRenderer(mContext));

        // FloorRenderer
        rendererList.add(new FloorRenderer(mContext));

        mArView.displayRenderers(rendererList);
    }
}