/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ds.tire.addoil;

import android.R.bool;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.util.Log;

/**
 * ���ڿؼ���Ԫ��
 * 
 * @Description: ���ڿؼ���Ԫ��
 * 
 * @FileName: Cell.java
 * 
 * @Package com.exina.android.calendar
 * 
 * @Author Hanyonglu
 * 
 * @Date 2012-3-26 ����11:37:05
 * 
 * @Version V1.0
 */
public class Cell
{
    protected Rect      mBound       = null;
    private final float STROKE_WIDTH = 0.5f;
    private boolean     highlighted  = false;
    protected int       mDayOfMonth  = 1;                                                          // from
                                                                                                    // 1
                                                                                                    // to
                                                                                                    // 31
    public Paint        mPaint       = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    int                 dx, dy;
    
    public Cell(int dayOfMon, Rect rect, float textSize, boolean bold, boolean highlight)
    {
        highlighted = highlight;
        mDayOfMonth = dayOfMon;
        mBound = rect;
        mPaint.setTextSize(textSize/* 26f */);
        mPaint.setColor(Color.BLACK);
        if (bold)
            mPaint.setFakeBoldText(true);
        dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
        dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
    }
    
    public Cell(int dayOfMon, Rect rect, float textSize)
    {
        this(dayOfMon, rect, textSize, false, false);
    }
    
    public Cell(int dayOfMon, Rect rect, float textSize, boolean highlight)
    {
        this(dayOfMon, rect, textSize, false, highlight);
    }
    
    public boolean isHighlighted()
    {
        return highlighted;
    }
    
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Style.FILL);
        paint.setAlpha(128);
        paint.setColor(Color.WHITE);
        // mBound.offset(2, 2);
        canvas.drawRect(new RectF(mBound.left, mBound.top, mBound.right, mBound.bottom), paint);
        if (highlighted)
        {
            paint.setColor(Color.GREEN);
            paint.setAlpha(128);
            canvas.drawRect(new RectF(mBound.left, mBound.top, mBound.right, mBound.bottom), paint);
        }
        
        mPaint.setTextAlign(Align.CENTER);
        canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX(), mBound.centerY() + mPaint.getTextSize() / 2, mPaint);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(mBound, paint);
        
    }
    
    public int getDayOfMonth()
    {
        return mDayOfMonth;
    }
    
    public boolean hitTest(int x, int y)
    {
        return mBound.contains(x, y);
    }
    
    public Rect getBound()
    {
        return mBound;
    }
    
    public String toString()
    {
        return String.valueOf(mDayOfMonth) + "(" + mBound.toString() + ")";
    }
    
}
