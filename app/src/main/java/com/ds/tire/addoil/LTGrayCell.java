/**
 * 
 */
package com.ds.tire.addoil;

import android.graphics.Color;
import android.graphics.Rect;

public class LTGrayCell extends Cell
{
    public LTGrayCell(int dayOfMon, Rect rect, float s)
    {
        super(dayOfMon, rect, s);
        mPaint.setColor(Color.LTGRAY);
    }
    
    public LTGrayCell(int dayOfMon, Rect rect, float s, boolean highlight)
    {
        super(dayOfMon, rect, s, highlight);
        mPaint.setColor(Color.LTGRAY);
    }
}
