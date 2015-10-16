package org.estgroup.phphub.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.estgroup.phphub.R;

public class AlertDialog extends Dialog implements
        View.OnClickListener {
    float widthProportion;
    float heightProportion;

    private Window window = null;

    public DialogClickListener listener;

    public interface  DialogClickListener {
        public void onClick(View view);
    }

    public AlertDialog(Context context) {
        super(context);
    }

    public AlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void popupDialog(int layoutResID, float wp, float hp, DialogClickListener listener) {
        setContentView(layoutResID);
        this.widthProportion = wp;
        this.heightProportion = hp;
        this.listener = listener;

        windowDeploy();

        setCanceledOnTouchOutside(true);

        show();
    }

    public void windowDeploy() {
        window = getWindow();

        window.setWindowAnimations(R.style.dialogWindow);
        window.setBackgroundDrawableResource(R.drawable.bg_dialog); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = 0; //x小于0左移，大于0右移
        wl.y = 0; //y小于0上移，大于0下移
//            wl.alpha = 0.6f; //设置透明度
//            wl.gravity = Gravity.BOTTOM; //设置重力

        window.setAttributes(wl);

        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();

        WindowManager.LayoutParams params = window.getAttributes();

        params.width = (int) (d.getWidth() * this.widthProportion);
        params.height = (int) (d.getHeight() * this.heightProportion);

        window.setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }

    public void setListener(DialogClickListener listener) {
        this.listener = listener;
    }
}
