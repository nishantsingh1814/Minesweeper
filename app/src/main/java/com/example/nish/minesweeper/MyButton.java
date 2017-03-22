package com.example.nish.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

/**
 * Created by Nishant on 1/30/2017.
 */

public class MyButton extends Button {
    private int row;
    private int col;
    private int cellValue;
    private boolean isRevealed=false;
    public MyButton(Context context) {
        super(context);
    }

    public int getRow() {
        return row;
    }

    public void setRevealed() {
        isRevealed=true;
        if (isRevealed) {
            int mineValue =this.getCellValue();
            if (mineValue == 0){
                setText("");
                int greenColorValue = Color.parseColor("#00ff00");
                setBackgroundColor(greenColorValue);
            } else if (mineValue == MainActivity.MINE) {
                setBackgroundColor(Color.rgb(0, 255, 255));
                setBackgroundResource(R.drawable.minec);
            } else {
                setBackgroundColor(Color.rgb(0, 255, 255));
                setText(""+mineValue);
            }
        }
    }

    public int getCol() {
        return col;

    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCellValue() {
        return cellValue;
    }

    public void setCellValue(int cellValue) {
        this.cellValue = cellValue;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public boolean isRevealed() {
        return isRevealed;
    }
}
