/**
 * 
 */
package com.ds.tire.addoil;

import android.graphics.Color;
import android.graphics.Rect;

public class GrayCell extends Cell
{
    
    public GrayCell(int dayOfMon, Rect rect, float s)
    {
        super(dayOfMon, rect, s);
        mPaint.setColor(Color.GRAY);
    }
    
    public GrayCell(int dayOfMon, Rect rect, float s, boolean highlight)
    {
        super(dayOfMon, rect, s, highlight);
        mPaint.setColor(Color.GRAY);
    }
}