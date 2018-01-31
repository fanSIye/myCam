package com.example.mycam;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhousong on 2016/9/18.
 * �������SurfaceView��Holder��
 */
public class CameraSurfaceHolder {
    Context context;
    SurfaceHolder surfaceHolder;
    SurfaceView surfaceView;
    InformationView informationView;
    SurfaceViewCallback callback = new SurfaceViewCallback();
    

    /**
    * �����������SurfaceView��Holder
     * @param context ��������ڵ�Activity�����context
     * @param surfaceView Holder���󶨵���Ӧ��SurfaceView
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


