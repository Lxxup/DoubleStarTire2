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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.ds.tire.R;

public class CalendarView extends ImageView
{
    private int                 CELL_WIDTH           = 58;
    private int                 CELL_HEIGH           = 53;
    private int                 CELL_MARGIN_TOP      = 0;
    private int                 CELL_MARGIN_LEFT     = 39;
    private float               CELL_TEXT_SIZE;
    private List<Integer>       highlightedDays;
    public final int            CURRENT_MOUNT        = 0;
    public final int            NEXT_MOUNT           = 1;
    public final int            PREVIOUS_MOUNT       = -1;
    private final String        TAG                  = "TAG";
    private Calendar            mRightNow            = null;
    // private Drawable mWeekTitle = null;
    private Cell                mToday               = null;
    private Cell[][]            mCells               = new Cell[6][7];
    private OnCellTouchListener mOnCellTouchListener = null;
    MonthDisplayHelper          mHelper;
    Drawable                    mDecoration          = null;
    public Drawable             mDecoraClick         = null;
    private Context             context;
    private int                 mWeekHeight;
    
    public void addHightLightDay(List<Integer> days)
    {
        if (highlightedDays == null)
        {
            highlightedDays = new ArrayList<Integer>();
        }
        highlightedDays.clear();
        highlightedDays.addAll(days);
        initCells();
        invalidate();
    }
    
    public interface OnCellTouchListener
    {
        public void onTouch(Cell cell);
    }
    
    public CalendarView(Context context)
    {
        this(context, null);
    }
    
    public CalendarView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public CalendarView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        mDecoration = context.getResources().getDrawable(R.drawable.typeb_calendar_today);
        mDecoraClick = context.getResources().getDrawable(R.drawable.typeb_calendar_today);
        initCalendarView();
    }
    
    private void initCalendarView()
    {
        mRightNow = Calendar.getInstance();
        // prepare static vars
        Resources res = getResources();
        
        // WEEK_TOP_MARGIN = (int) res.getDimension(R.dimen.week_top_margin);
        // WEEK_LEFT_MARGIN = (int) res.getDimension(R.dimen.week_left_margin);
        
        CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
        CELL_HEIGH = (int) res.getDimension(R.dimen.cell_heigh);
        CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
        // CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);
        CELL_MARGIN_LEFT = 0;
        CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
        
        mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR), mRightNow.get(Calendar.MONTH), mRightNow.getFirstDayOfWeek());
        
    }
    
    private void initCells()
    {
        class _calendar
        {
            public int day;
            public int whichMonth; // -1 Ϊ���� 1Ϊ���� 0Ϊ����
                                   
            public _calendar(int d, int b)
            {
                day = d;
                whichMonth = b;
            }
            
            public _calendar(int d)
            { // �ϸ��� Ĭ��Ϊ
                this(d, PREVIOUS_MOUNT);
            }
        }
        ;
        _calendar tmp[][] = new _calendar[6][7];
        
        for (int i = 0; i < tmp.length; i++)
        {
            int n[] = mHelper.getDigitsForRow(i);
            for (int d = 0; d < n.length; d++)
            {
                if (mHelper.isWithinCurrentMonth(i, d))
                    tmp[i][d] = new _calendar(n[d], CURRENT_MOUNT);
                else if (i == 0)
                {
                    tmp[i][d] = new _calendar(n[d]);
                }
                else
                {
                    tmp[i][d] = new _calendar(n[d], NEXT_MOUNT);
                }
                
            }
        }
        
        Calendar today = Calendar.getInstance();
        int thisDay = 0;
        mToday = null;
        if (mHelper.getYear() == today.get(Calendar.YEAR) && mHelper.getMonth() == today.get(Calendar.MONTH))
        {
            thisDay = today.get(Calendar.DAY_OF_MONTH);
        }
        // build cells
        Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH + CELL_MARGIN_LEFT, CELL_HEIGH + CELL_MARGIN_TOP);
        for (int week = 0; week < mCells.length; week++)
        {
            for (int dayOfWeek = 0; dayOfWeek < mCells[week].length; dayOfWeek++)
            {
                if (tmp[week][dayOfWeek].whichMonth == CURRENT_MOUNT)
                { // ����
                    // ��ʼ����cell
                    int day = tmp[week][dayOfWeek].day;
                    if (dayOfWeek == 0 || dayOfWeek == 6)
                        if (highlightedDays != null && highlightedDays.size() > 0 && highlightedDays.contains(day))
                        {
                            mCells[week][dayOfWeek] = new RedCell(tmp[week][dayOfWeek].day, new Rect(Bound), CELL_TEXT_SIZE, true);
                        }
                        else
                        {
                            mCells[week][dayOfWeek] = new RedCell(tmp[week][dayOfWeek].day, new Rect(Bound), CELL_TEXT_SIZE);
                        }
                    
                    else if (highlightedDays != null && highlightedDays.size() > 0 && highlightedDays.contains(day))
                    {
                        mCells[week][dayOfWeek] = new Cell(tmp[week][dayOfWeek].day, new Rect(Bound), CELL_TEXT_SIZE, true);
                    }
                    else
                    {
                        mCells[week][dayOfWeek] = new Cell(tmp[week][dayOfWeek].day, new Rect(Bound), CELL_TEXT_SIZE);
                    }
                    
                }
                else if (tmp[week][dayOfWeek].whichMonth == PREVIOUS_MOUNT)
                {
                    mCells[week][dayOfWeek] = new GrayCell(tmp[week][dayOfWeek].day, new Rect(Bound), CELL_TEXT_SIZE);
                }
                else
                { // ����ΪLTGray
                    mCells[week][dayOfWeek] = new LTGrayCell(tmp[week][dayOfWeek].day, new Rect(Bound), CELL_TEXT_SIZE);
                }
                
                Bound.offset(CELL_WIDTH, 0); // move to next column
                // get today
                if (tmp[week][dayOfWeek].day == thisDay && tmp[week][dayOfWeek].whichMonth == 0)
                {
                    mToday = mCells[week][dayOfWeek];
                    mDecoration.setBounds(mToday.getBound());
                }
            }
            Bound.offset(0, CELL_HEIGH); // move to next row and first column
            Bound.left = CELL_MARGIN_LEFT;
            Bound.right = CELL_MARGIN_LEFT + CELL_WIDTH;
        }
    }
    
    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        int width = right - left;
        // Rect re = getDrawable().getBounds();
        // WEEK_LEFT_MARGIN = CELL_MARGIN_LEFT = (right - left - re.width()) /
        // 2;
        CELL_MARGIN_LEFT = 0;
        CELL_MARGIN_TOP = mWeekHeight;
        /*
         * mWeekTitle.setBounds(WEEK_LEFT_MARGIN, WEEK_TOP_MARGIN,
         * WEEK_LEFT_MARGIN + mWeekTitle.getMinimumWidth(), WEEK_TOP_MARGIN +
         * mWeekTitle.getMinimumHeight());
         */
        CELL_WIDTH = width / 7;
        CELL_HEIGH = 4 * CELL_WIDTH / 5;
        CELL_MARGIN_TOP = mWeekHeight = CELL_WIDTH / 2;
        LayoutParams params = getLayoutParams();
        params.height = CELL_HEIGH * 6 + mWeekHeight;
        setLayoutParams(params);
        initCells();
        super.onLayout(changed, left, top, right, bottom);
    }
    
    public void setTimeInMillis(long milliseconds)
    {
        mRightNow.setTimeInMillis(milliseconds);
        initCells();
        this.invalidate();
    }
    
    public int getYear()
    {
        return mHelper.getYear();
    }
    
    public int getMonth()
    {
        return mHelper.getMonth();
    }
    
    public void nextMonth()
    {
        mHelper.nextMonth();
        initCells();
        invalidate();
    }
    
    public void previousMonth()
    {
        mHelper.previousMonth();
        initCells();
        invalidate();
    }
    
    public boolean firstDay(int day)
    {
        return day == 1;
    }
    
    public boolean lastDay(int day)
    {
        return mHelper.getNumberOfDaysInMonth() == day;
    }
    
    public void goToday()
    {
        Calendar cal = Calendar.getInstance();
        mHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
        initCells();
        invalidate();
    }
    
    public Calendar getDate()
    {
        return mRightNow;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mOnCellTouchListener != null)
        {
            for (Cell[] week : mCells)
            {
                for (Cell day : week)
                {
                    if (day.hitTest((int) event.getX(), (int) event.getY()))
                    {
                        mOnCellTouchListener.onTouch(day);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }
    
    public void setOnCellTouchListener(OnCellTouchListener p)
    {
        mOnCellTouchListener = p;
    }
    
    String[] week = new String[] { "Sun", "Mon", "Tue", "Web", "Thu", "Fri", "Sat" };
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        // draw background
        super.onDraw(canvas);
        Rect rect = canvas.getClipBounds();
        // mWeekTitle.draw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setARGB(255, 216, 196, 155);
        canvas.drawRect(new RectF(CELL_MARGIN_LEFT, rect.top, rect.right - 2, rect.top + mWeekHeight), paint);
        paint.setColor(Color.DKGRAY);
        paint.setFakeBoldText(true);
        paint.setTextSize(CELL_TEXT_SIZE - 5);
        paint.setTextAlign(Align.CENTER);
        float x = rect.left + CELL_WIDTH / 2;
        for (int i = 0; i < week.length; i++)
        {
            canvas.drawText(week[i], x, rect.top + (paint.getTextSize() + mWeekHeight) / 2, paint);
            x += CELL_WIDTH;
        }
        
        // draw cells
        for (Cell[] week : mCells)
        {
            for (Cell day : week)
            {
                day.draw(canvas);
            }
        }
        
        // draw today
        if (mDecoration != null && mToday != null)
        {
            mDecoration.draw(canvas);
        }
        if (mDecoraClick.getBounds() != null)
        {
            mDecoraClick.draw(canvas);
            mDecoraClick = context.getResources().getDrawable(R.drawable.typeb_calendar_today);
            // mDecoraClick.setBounds(null);
        }
    }
    
}