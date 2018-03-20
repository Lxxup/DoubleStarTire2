package com.and.netease.utils;

import android.view.View;
import android.view.animation.TranslateAnimation;

public class MoveBg {
	/**
	 * ???????
	 * 
	 * @param v
	 *            ????????View
	 * @param startX
	 *            ???x????
	 * @param toX
	 *            ???x????
	 * @param startY
	 *            ???y????
	 * @param toY
	 *            ???y????
	 */
	public static void moveFrontBg(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(200);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
}
