package com.panacoding.gridtimeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class JGridDraw extends View {

    private int rowsCount = 19;
    private int columnsCount = 7;
    private int col_multiplier = 4;
    private String stroke_color = "#edf2f4";

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    float cell_height;
    float cell_width;
    private int canvasWidth;
    private int canvasHeight;

    OnCellSizeChange onCellSizeChange;

    public interface OnCellSizeChange{
        public void onCellSizeChange(float cell_width,float cell_height);
    }

    public void setOnCellSizeChange(OnCellSizeChange onCellSizeChange) {
        this.onCellSizeChange = onCellSizeChange;
    }

    public void setStroke_color(String stroke_color) {
        this.stroke_color = stroke_color;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }

    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public void setCol_multiplier(int col_multiplier) {
        this.col_multiplier = col_multiplier;
    }

    public JGridDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.parseColor(stroke_color));
        paint.setStrokeWidth(JUtils.convertDpToPixel(1,context));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth = w;
        canvasHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = canvasHeight;
        int width = canvasWidth;

        paint.setColor(Color.parseColor(stroke_color));

        float cal_columnsCount = columnsCount * col_multiplier;

        for (int i = 0; i < rowsCount; ++i) {
            canvas.drawLine(0, height / rowsCount * (i + 1), width, height / rowsCount * (i + 1), paint);
        }


        for (int i = 0; i < cal_columnsCount; ++i) {
            canvas.drawLine(width / cal_columnsCount * (i + 1), 0, (width / cal_columnsCount * (i + 1)), height, paint);
        }

        cell_height = height / rowsCount;
        cell_width = width / columnsCount;

        if(onCellSizeChange!=null){
            onCellSizeChange.onCellSizeChange(cell_width,cell_height);
        }

        super.onDraw(canvas);
    }

}
