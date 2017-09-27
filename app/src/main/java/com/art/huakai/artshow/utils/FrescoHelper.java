package com.art.huakai.artshow.utils;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by yezi on 16-6-8.
 */
public class FrescoHelper {

    public static void init(Context context) {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(context, config);
    }

    public static void release() {
        Fresco.shutDown();
    }


    public static void pause() {
        Fresco.getImagePipeline().pause();
    }

    public static void resume() {
        Fresco.getImagePipeline().resume();
    }

}
