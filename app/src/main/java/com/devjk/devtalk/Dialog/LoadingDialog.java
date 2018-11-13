package com.devjk.devtalk.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.devjk.devtalk.R;

public class LoadingDialog extends Dialog {

    private Context context;
    private ImageView imgv_logo;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imgv_logo = (ImageView) findViewById(R.id.LoadingDialog_ImageView_loadingIcon);
        imgv_logo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.loading));
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
