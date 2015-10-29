package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;

import butterknife.Bind;
import me.relex.photodraweeview.PhotoDraweeView;

public class GalleryActivity extends BaseActivity {
    private final static String IMAGE_URL = "image_url";

    @Bind(R.id.zoom_photo)
    PhotoDraweeView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String imageUrl = getIntent().getStringExtra(IMAGE_URL);
        if (!TextUtils.isEmpty(imageUrl)) {
            PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
            builder.setUri(Uri.parse(imageUrl));
            builder.setOldController(photoView.getController());
            builder.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null || photoView == null) {
                        return;
                    }
                    photoView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoView.setController(builder.build());
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.gallery;
    }

    @Override
    protected CharSequence getTitleName() {
        return "图片浏览";
    }

    public static Intent getCallingIntent(Context context, String imageUrl) {
        Intent callingIntent = new Intent(context, GalleryActivity.class);
        callingIntent.putExtra(IMAGE_URL, imageUrl);
        return callingIntent;
    }
}
