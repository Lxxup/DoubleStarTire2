/**
 * 
 */
package com.ds.tire.addoil;

import android.graphics.Rect;

public class RedCell extends Cell
{
    public RedCell(int dayOfMon, Rect rect, float s)
    {
        super(dayOfMon, rect, s);
        mPaint.setColor(0xdddd0000);
    }
    
    public RedCell(int dayOfMon, Rect rect, float s, boolean highlight)
    {
        super(dayOfMon, rect, s, highlight);
        mPaint.setColor(0xdddd0000);
    }
}
