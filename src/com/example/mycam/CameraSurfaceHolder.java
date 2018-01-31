package com.example.mycam;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhousong on 2016/9/18.
 * 相机界面SurfaceView的Holder类
 */
public class CameraSurfaceHolder {
    Context context;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    InformationView informationView;
    SurfaceViewCallback callback = new SurfaceViewCallback();
    

    /**
    * 设置相机界面SurfaceView的Holder
     * @param context 从相机所在的Activity传入的context
     * @param surfaceView Holder所绑定的响应的SurfaceView
    * */
    public void setCameraSurfaceHolder(Context context, SurfaceView surfaceView) {
        this.context = context;
        this.surfaceView = surfaceView;
        surfaceHolder = surfaceView.getHolder();
        callback.setContext(context);
        callback.setInformationView(this.informationView);
        surfaceHolder.addCallback(callback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setInformationView(InformationView view) {
        this.informationView = view;
    }

}


