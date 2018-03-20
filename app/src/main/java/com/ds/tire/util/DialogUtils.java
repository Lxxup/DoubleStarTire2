package com.ds.tire.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

public class DialogUtils
{
    public static void alert(Context context, Drawable icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText, DialogInterface.OnClickListener negativeListener, String neutralText, DialogInterface.OnClickListener neutralListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog = dialog.setNeutralButton(neutralText, neutralListener);
        dialog.create().show();
    }
    
    public static void alert(Boolean flag, Context context, int icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText, DialogInterface.OnClickListener negativeListener, String neutralText, DialogInterface.OnClickListener neutralListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog = dialog.setNeutralButton(neutralText, neutralListener);
        dialog = dialog.setCancelable(flag);
        dialog.create().show();
    }
    
    public static void alert(Context context, Drawable icon, int title, int message, int positiveText, DialogInterface.OnClickListener positiveListener, int negativeText, DialogInterface.OnClickListener negativeListener, int neutralText, DialogInterface.OnClickListener neutralListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog = dialog.setNeutralButton(neutralText, neutralListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, int title, int message, int positiveText, DialogInterface.OnClickListener positiveListener, int negativeText, DialogInterface.OnClickListener negativeListener, int neutralText, DialogInterface.OnClickListener neutralListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog = dialog.setNeutralButton(neutralText, neutralListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, Drawable icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, String negativeText, DialogInterface.OnClickListener negativeListener, boolean flag)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog = dialog.setCancelable(flag);
        dialog.create().show();
    }
    
    public static void alert(Context context, Drawable icon, int title, int message, int positiveText, DialogInterface.OnClickListener positiveListener, int negativeText, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, int title, int message, int positiveText, DialogInterface.OnClickListener positiveListener, int negativeText, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, Drawable icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, Drawable icon, int title, int message, int positiveText, DialogInterface.OnClickListener positiveListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, int title, String message, int positiveText, DialogInterface.OnClickListener positiveListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, int title, int message, int positiveText, DialogInterface.OnClickListener positiveListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog.create().show();
    }
    
    public static void alert(Context context, int icon, int title, String message, int positiveText, DialogInterface.OnClickListener positiveListener, int negativeText, DialogInterface.OnClickListener negativeListener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog = dialog.setIcon(icon);
        dialog = dialog.setTitle(title);
        dialog = dialog.setMessage(message);
        dialog = dialog.setPositiveButton(positiveText, positiveListener);
        dialog = dialog.setNegativeButton(negativeText, negativeListener);
        dialog.create().show();
        
    }
}
