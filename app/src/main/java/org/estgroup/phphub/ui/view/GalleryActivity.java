package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;

public class GalleryActivity extends BaseActivity {
    private final static String IMAGE_URL = "image_url";

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
