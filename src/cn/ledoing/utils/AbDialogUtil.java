/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ledoing.utils;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ledoing.activity.R;
import cn.ledoing.adapter.LCReasonAdapter;
import cn.ledoing.bean.CancelReasonList;
import cn.ledoing.bean.OfflineCourses;
import cn.ledoing.fragment.AbAlertDialogFragment;
import cn.ledoing.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import cn.ledoing.fragment.AbDialogFragment.AbDialogOnLoadListener;
import cn.ledoing.fragment.AbLoadDialogFragment;
import cn.ledoing.fragment.AbProgressDialogFragment;
import cn.ledoing.fragment.AbRefreshDialogFragment;
import cn.ledoing.fragment.AbSampleDialogFragment;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.model.LCCheckChangeCallBack;
import cn.ledoing.view.LCDialog;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbDialogUtil.java
 * 描述：Dialog工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-02 下午11:52:13
 */

public class AbDialogUtil {

    /**
     * dialog tag
     */
    private static String mDialogTag = "dialog";
    private static View dialogView;
    private static LCDialog dialog;
    private static boolean isSuccess;

    /**
     * 全屏显示一个对话框不影响下面的View的点击
     *
     * @param view
     * @return
     */
    public static AbSampleDialogFragment showTipsDialog(View view) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
        newFragment.setContentView(view);

        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // 作为全屏显示,使用“content”作为fragment容器的基本视图,这始终是Activity的基本视图  
        ft.add(android.R.id.content, newFragment, mDialogTag).addToBackStack(null).commit();

        return newFragment;
    }

    /**
     * 全屏显示一个对话框
     *
     * @param view
     * @return
     */
    public static AbSampleDialogFragment showFullScreenDialog(View view) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画 
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个自定义的对话框(有背景层)
     *
     * @param view
     */
    public static AbSampleDialogFragment showDialog(View view) {
        return showDialog(view, Gravity.CENTER);
    }

    /**
     * 描述：显示一个自定义的对话框(有背景层).
     *
     * @param view
     * @param gravity 位置
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, int gravity) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog, gravity);
        newFragment.setContentView(view);

        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);

        return newFragment;
    }

    /**
     * 显示一个自定义的对话框(有背景层)
     *
     * @param view
     * @param animEnter
     * @param animExit
     * @param animPopEnter
     * @param animPopExit
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, int animEnter, int animExit, int animPopEnter, int animPopExit) {
        return showDialog(view, animEnter, animExit, animPopEnter, animPopExit, Gravity.CENTER);
    }

    /**
     * 描述：显示一个自定义的对话框(有背景层).
     *
     * @param view
     * @param animEnter
     * @param animExit
     * @param animPopEnter
     * @param animPopExit
     * @param gravity      位置
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, int animEnter, int animExit, int animPopEnter, int animPopExit, int gravity) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog, gravity);
        newFragment.setContentView(view);
        //自定义转场动画
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个自定义的对话框(有背景层)
     *
     * @param view
     * @param onCancelListener
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, DialogInterface.OnCancelListener onCancelListener) {
        return showDialog(view, Gravity.CENTER, onCancelListener);
    }

    /**
     * 描述：显示一个自定义的对话框(有背景层).
     *
     * @param view
     * @param gravity          位置
     * @param onCancelListener 　取消事件
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, int gravity, DialogInterface.OnCancelListener onCancelListener) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog, gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画  
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.setOnCancelListener(onCancelListener);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示一个自定义的对话框(有背景层).
     *
     * @param view
     * @param animEnter
     * @param animExit
     * @param animPopEnter
     * @param animPopExit
     * @param onCancelListener
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, int animEnter, int animExit, int animPopEnter, int animPopExit, DialogInterface.OnCancelListener onCancelListener) {
        return showDialog(view, animEnter, animExit, animPopEnter, animPopExit, Gravity.CENTER, onCancelListener);
    }

    /**
     * 描述：显示一个自定义的对话框(有背景层).
     *
     * @param view
     * @param animEnter
     * @param animExit
     * @param animPopEnter
     * @param animPopExit
     * @param gravity
     * @param onCancelListener
     * @return
     */
    public static AbSampleDialogFragment showDialog(View view, int animEnter, int animExit, int animPopEnter, int animPopExit, int gravity, DialogInterface.OnCancelListener onCancelListener) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog, gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);
        newFragment.setOnCancelListener(onCancelListener);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个自定义的对话框(无背景层)
     *
     * @param view
     */
    public static AbSampleDialogFragment showPanel(View view) {
        return showPanel(view, Gravity.CENTER);
    }

    /**
     * 描述：显示一个自定义的对话框(无背景层).
     *
     * @param view
     * @param gravity
     * @return
     */
    public static AbSampleDialogFragment showPanel(View view, int gravity) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_Panel, gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个自定义的对话框(无背景层)
     *
     * @param view
     * @param onCancelListener
     * @return
     */
    public static AbSampleDialogFragment showPanel(View view, DialogInterface.OnCancelListener onCancelListener) {
        return showPanel(view, Gravity.CENTER, onCancelListener);
    }

    /**
     * 显示一个自定义的对话框(无背景层)
     *
     * @param view
     * @param onCancelListener
     * @return
     */
    public static AbSampleDialogFragment showPanel(View view, int gravity, DialogInterface.OnCancelListener onCancelListener) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_Panel, gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.setOnCancelListener(onCancelListener);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }


    /**
     * 描述：对话框dialog （图标，标题，String内容）.
     *
     * @param context
     * @param icon
     * @param title   对话框标题内容
     * @param view    对话框提示内容
     */
    public static AbAlertDialogFragment showAlertDialog(Context context, int icon, String title, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon, title, message, null, null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个一般的对话框（图标，标题，string内容，确认，取消）.
     *
     * @param context
     * @param icon
     * @param title           对话框标题内容
     * @param message         对话框提示内容
     * @param onClickListener 点击确认按钮的事件监听
     */
    public static AbAlertDialogFragment showAlertDialog(Context context, int icon, String title, String message, AbDialogOnClickListener onClickListener) {
        FragmentActivity activity = (FragmentActivity) context;
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon, title, message, null, onClickListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }


    /**
     * 显示一个一般的对话框（标题，String内容，确认，取消）.
     *
     * @param context
     * @param title           对话框标题内容
     * @param message         对话框提示内容
     * @param onClickListener 点击确认按钮的事件监听
     */
    public static AbAlertDialogFragment showAlertDialog(Context context, String title, String message, AbDialogOnClickListener onClickListener) {
        FragmentActivity activity = (FragmentActivity) context;
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0, title, message, null, onClickListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个一般的对话框（View内容）.
     *
     * @param view 对话框标题内容
     */
    public static AbAlertDialogFragment showAlertDialog(View view) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0, null, null, view, null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个一般的对话框（String内容）.
     *
     * @param context
     * @param title   对话框标题内容
     */
    public static AbAlertDialogFragment showAlertDialog(Context context, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0, null, message, null, null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：对话框dialog （图标，标题，View内容）.
     *
     * @param icon
     * @param title 对话框标题内容
     * @param view  对话框提示内容
     */
    public static AbAlertDialogFragment showAlertDialog(int icon, String title, View view) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon, title, null, view, null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个一般的对话框（图标，标题，View内容，确认，取消）.
     *
     * @param icon
     * @param title           对话框标题内容
     * @param view            对话框提示内容
     * @param onClickListener 点击确认按钮的事件监听
     */
    public static AbAlertDialogFragment showAlertDialog(int icon, String title, View view, AbDialogOnClickListener onClickListener) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon, title, null, view, onClickListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：对话框dialog （标题，View内容）.
     *
     * @param title 对话框标题内容
     * @param view  对话框提示内容
     */
    public static AbAlertDialogFragment showAlertDialog(String title, View view) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0, title, null, view, null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 显示一个一般的对话框（标题，View内容，确认，取消）.
     *
     * @param title           对话框标题内容
     * @param view            对话框提示内容
     * @param onClickListener 点击确认按钮的事件监听
     */
    public static AbAlertDialogFragment showAlertDialog(String title, View view, AbDialogOnClickListener onClickListener) {
        FragmentActivity activity = (FragmentActivity) view.getContext();
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0, title, null, view, onClickListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：对话框dialog （标题，String内容）.
     *
     * @param context
     * @param title   对话框标题内容
     * @param view    对话框提示内容
     */
    public static AbAlertDialogFragment showAlertDialog(Context context, String title, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0, title, message, null, null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }


    /**
     * 描述：显示进度框.
     *
     * @param context               the context
     * @param indeterminateDrawable 用默认请写0
     * @param message               the message
     */
    public static AbProgressDialogFragment showProgressDialog(Context context, int indeterminateDrawable, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbProgressDialogFragment newFragment = AbProgressDialogFragment.newInstance(indeterminateDrawable, message);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return null;
    }

    /**
     * 描述：显示加载框.
     *
     * @param context               the context
     * @param indeterminateDrawable
     * @param message               the message
     */
    public static AbLoadDialogFragment showLoadDialog(Context context, int indeterminateDrawable, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示加载框.
     *
     * @param context               the context
     * @param indeterminateDrawable
     * @param message               the message
     */
    public static AbLoadDialogFragment showLoadDialog(Context context, int indeterminateDrawable, String message, AbDialogOnLoadListener abDialogOnLoadListener) {
        FragmentActivity activity = (FragmentActivity) context;
        AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示加载框.
     *
     * @param context               the context
     * @param indeterminateDrawable
     * @param message               the message
     */
    public static AbLoadDialogFragment showLoadPanel(Context context, int indeterminateDrawable, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_Panel);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示加载框.
     *
     * @param context                   the context
     * @param indeterminateDrawable
     * @param message                   the message
     * @param abDialogOnRefreshListener
     */
    public static AbLoadDialogFragment showLoadPanel(Context context, int indeterminateDrawable, String message, AbDialogOnLoadListener abDialogOnLoadListener) {
        FragmentActivity activity = (FragmentActivity) context;
        AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_Panel);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示刷新框.
     *
     * @param context                   the context
     * @param indeterminateDrawable
     * @param message                   the message
     * @param abDialogOnRefreshListener
     */
    public static AbRefreshDialogFragment showRefreshDialog(Context context, int indeterminateDrawable, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        newFragment.setAbDialogOnLoadListener(null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示刷新框.
     *
     * @param context
     * @param indeterminateDrawable
     * @param message
     * @param abDialogOnRefreshListener
     * @return
     */
    public static AbRefreshDialogFragment showRefreshDialog(Context context, int indeterminateDrawable, String message, AbDialogOnLoadListener abDialogOnLoadListener) {
        FragmentActivity activity = (FragmentActivity) context;
        AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示刷新框.
     *
     * @param context               the context
     * @param indeterminateDrawable
     * @param message               the message
     */
    public static AbRefreshDialogFragment showRefreshPanel(Context context, int indeterminateDrawable, String message) {
        FragmentActivity activity = (FragmentActivity) context;
        AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_Panel);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        newFragment.setAbDialogOnLoadListener(null);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }

    /**
     * 描述：显示刷新框.
     *
     * @param context
     * @param indeterminateDrawable
     * @param message
     * @param abDialogOnRefreshListener
     * @return
     */
    public static AbRefreshDialogFragment showRefreshPanel(Context context, int indeterminateDrawable, String message, AbDialogOnLoadListener abDialogOnLoadListener) {
        FragmentActivity activity = (FragmentActivity) context;
        AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_Panel);
        newFragment.setIndeterminateDrawable(indeterminateDrawable);
        newFragment.setMessage(message);
        newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }


    /**
     * 描述：移除Fragment.
     *
     * @param context the context
     */
    public static void removeDialog(Context context) {
        try {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            // 指定一个系统转场动画
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            Fragment prev = activity.getFragmentManager().findFragmentByTag(mDialogTag);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            ft.commit();
        } catch (Exception e) {
            //可能有Activity已经被销毁的异常
            e.printStackTrace();
        }
    }

    /**
     * 描述：移除Fragment和View
     *
     * @param view
     */
    public static void removeDialog(View view) {
        removeDialog(view.getContext());
        AbViewUtil.removeSelfFromParent(view);
    }

    public static CharSequence temp;

    /**
     * 描述：
     *
     * @param
     */
    public static boolean showDialog(final Context context, final List<CancelReasonList> listReason, final OfflineCourses classList) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.lc_abolish_dialog,
                null);
        final LCDialog dialog = new LCDialog(context, R.style.MyDialog, dialogView);
        dialog.show();
        final Map<String, String> choose = new HashMap<String, String>();
        ImageView lc_dialog_cancel = (ImageView) dialogView.findViewById(R.id.lc_dialog_cancel);
        ListView Lc_dialog_reasonChose = (ListView) dialogView.findViewById(R.id.Lc_dialog_reasonChose);
        Lc_dialog_reasonChose.setOverScrollMode(View.OVER_SCROLL_NEVER);
        View dialogV = LayoutInflater.from(context).inflate(R.layout.lc_layout_footview,
                null);
        EditText lc_dialog_reasonOther = (EditText) dialogV.findViewById(R.id.lc_dialog_reasonOther);
        final Button lc_dialog_confirmCancel = (Button) dialogV.findViewById(R.id.lc_dialog_confirmCancel);
        Lc_dialog_reasonChose.addFooterView(dialogV);
        LCReasonAdapter reasonAdapter=new LCReasonAdapter(context,listReason,choose);
        Lc_dialog_reasonChose.setAdapter(reasonAdapter);
        reasonAdapter.setCheckChangeListener(new LCCheckChangeCallBack() {
            @Override
            public void onChange(Map<String, String> choose) {
                if (choose.size() > 0) {
                    lc_dialog_confirmCancel.setBackgroundResource(R.drawable.blue_btn_selector);
                    lc_dialog_confirmCancel.setTextColor(context.getResources().getColor(R.color.white));
                    lc_dialog_confirmCancel.setEnabled(true);
                } else {
                    lc_dialog_confirmCancel.setBackgroundResource(R.drawable.no_select);
                    lc_dialog_confirmCancel.setTextColor(context.getResources().getColor(R.color.title_theme_text1));
                    lc_dialog_confirmCancel.setEnabled(false);
                }
            }
        });
        lc_dialog_reasonOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 0) {
                    choose.put("five", temp.toString());
                } else {
                    choose.remove("five");
                }
                if (choose.size() > 0) {
                    lc_dialog_confirmCancel.setBackgroundResource(R.drawable.blue_btn_selector);
                    lc_dialog_confirmCancel.setTextColor(context.getResources().getColor(R.color.white));
                    lc_dialog_confirmCancel.setEnabled(true);
                } else {
                    lc_dialog_confirmCancel.setBackgroundResource(R.drawable.no_select);
                    lc_dialog_confirmCancel.setTextColor(context.getResources().getColor(R.color.title_theme_text1));
                    lc_dialog_confirmCancel.setEnabled(false);
                }
            }
        });
        lc_dialog_confirmCancel.setEnabled(false);
        lc_dialog_confirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer reason = new StringBuffer();
                reason.append("{\"reason\":[");
                for (int i = 0; i < listReason.size(); i++) {
                    if (!TextUtils.isEmpty(choose.get(i + ""))) {
                        reason.append("\"" + choose.get(i + "") + "\"" + ",");
                    }
                }
                reason.append("]").append("\"otherReason\":").append("\"" + choose.get("five") + "\"").append("}");

                AbRequestParams params = new AbRequestParams();
                params.put("reservation_id", classList.getId());
                params.put("reason", reason.toString());
                AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(context);
                mAbHttpUtil.setTimeout(5000);
                mAbHttpUtil.postUrl(LCConstant.RE_CANCEL, params,
                        new AbStringHttpResponseListener() {

                            // 获取数据成功会调用这里
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                L.e("-------" + statusCode + "---------" + content + "---------------");
                                String praise = JSONUtils.getInstatce().loadConvert(content);
                                L.e(praise);
                                try {
                                    JSONObject jo = new JSONObject(praise);
                                    String errorCode = jo.optString("errorCode", "");
                                    String errorMessage = jo.optString("errorMessage", "");
                                    if ("0".equals(errorCode)) {
                                        isSuccess = true;
                                    } else {
                                        LCUtils.ReLogin(errorCode, context, errorMessage);
                                    }
                                } catch (JSONException e) {
//                            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
                                }


                            }

                            // 开始执行前
                            @Override
                            public void onStart() {
                                LCUtils.startProgressDialog(context);
                            }

                            // 失败，调用
                            @Override
                            public void onFailure(int statusCode, String content,
                                                  Throwable error) {
                                L.e("" + statusCode + "::" + content);
                            }

                            // 完成后调用，失败，成功
                            @Override
                            public void onFinish() {

                                LCUtils.stopProgressDialog(context);
                            }


                        });

                dialog.cancel();
            }
        });
        lc_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return isSuccess;
    }

    public static LCDialog showDialogTwoButton(Context context,View.OnClickListener cancel, View.OnClickListener affim,
                                        String msg, String textcancel, String textaffim) {
        dialogView = LayoutInflater.from(context).inflate(R.layout.lc_dialog,
                null);
        dialog = new LCDialog(context, R.style.MyDialog, dialogView);
        dialog.show();
        TextView tv = (TextView) dialogView.findViewById(R.id.jxb_dialog_title);
        tv.setText(msg);
        Button dialog_button_cancel = (Button) dialogView
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setText(textcancel);
        Button dialog_button_affim = (Button) dialogView
                .findViewById(R.id.dialog_button_affim);

        dialog_button_affim.setText(textaffim);

        if (affim == null) {
            dialog_button_affim.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        } else {
            dialog_button_affim.setOnClickListener(affim);
        }

        if (cancel == null) {
            dialog_button_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        } else {
            dialog_button_cancel.setOnClickListener(cancel);

        }
        return dialog;
    }
}
