package com.example.mycam;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

/**
 * Created by zhousong on 2016/9/18.
 * ����࣬����ĵ���*/

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class FrontCamera {
    static final String TAG = "CameraCCCC";
    Camera mCamera;
    int mCurrentCamIndex = 0;
    boolean previewing;

    public void setCamera(Camera camera)
    {
        this.mCamera = camera;
    }

    public int getCurrentCamIndex()
    {
        return this.mCurrentCamIndex;
    }

    public boolean getPreviewing()
    {
        return this.previewing;
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.i(TAG, "�Ѿ���ȡ��bitmap:" + bitmap.toString());
            previewing = false;
            //��Ҫ�����ִ������
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filePath = ImageUtil.getSaveImgePath();
                    File file = new File(filePath);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file, true);
                        fos.write(data);
                        ImageUtil.saveImage(file, data, filePath);
                        fos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();*/
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();//���¿���Ԥ�� ����Ȼ���ܼ�������
            previewing = true;
        }


    };

    //��ʼ�����
    public Camera initCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        previewing = true;

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            //������򿪵���ǰ������ͷ,���޸Ĵ򿪺���ORǰ��
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    /**
     * ֹͣ���
     * @param mCamera ��Ҫֹͣ���������
    * */
    public void StopCamera(Camera mCamera) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        previewing = false;
    }

    /**
     * ��ת��Ļ���Զ����䣨��ֻ�õ����ģ�Ҳ�ɲ�Ҫ��
     * �Ѿ���manifests���ô�Activityֻ��������
     * @param activity �����ʾ�ڵ�Activity
     * @param cameraId �����ID
     * @param camera �������
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera)
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
		System.out.println("rotation ============== " + rotation);

        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else
        {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        result = 0;
        camera.setDisplayOrientation(result);
    }

}
