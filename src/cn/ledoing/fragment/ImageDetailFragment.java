package cn.ledoing.fragment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.ledoing.activity.ImagePagerActivity;
import cn.ledoing.activity.R;
import cn.ledoing.imageloader.ImageLoader;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.photoview.PhotoView;
import cn.ledoing.view.photoview.PhotoViewAttacher;
import cn.ledoing.view.photoview.PhotoViewAttacher.OnPhotoTapListener;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private PhotoView mImageView;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (PhotoView) v.findViewById(R.id.image);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mAttacher = new PhotoViewAttacher(mImageView);
        LCUtils.startProgressDialog(getActivity());
        Glide.with(getActivity()).load(mImageUrl).placeholder(R.color.gray).error(R.color.gray).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                LCUtils.stopProgressDialog(getActivity());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                LCUtils.stopProgressDialog(getActivity());
                return false;
            }
        }).into(mImageView);
        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
