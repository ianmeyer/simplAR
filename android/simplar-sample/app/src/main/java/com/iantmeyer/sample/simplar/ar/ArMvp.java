package com.iantmeyer.sample.simplar.ar;

import android.support.annotation.NonNull;

import com.iantmeyer.simplar.render.ArRenderer;

import java.util.List;

interface ArMvp {
    interface Model {
    }

    interface View {
        void displayRenderers(@NonNull List<ArRenderer> arRendererList);
    }

    interface Presenter {
        void loadRenderers();
    }
}
