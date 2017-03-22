package com.example.nish.minesweeper;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    LinearLayout mainLayout;
    LinearLayout squareLayout;
    LinearLayout rows[];
    static int n;
    static int numberOfMines ;
    MyButton buttons[][];
    final static int MINE = -1;
    static boolean gameOver;
    static int score;
    TextView textView;
    static boolean areMinesSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.activity_main);
        squareLayout = (LinearLayout) findViewById(R.id.squareLayout);
        textView = (TextView) findViewById(R.id.textView);

        Bundle bundle = getIntent().getExtras();
        n = bundle.getInt("n");
        numberOfMines=bundle.getInt("mines");

        setUpBoard();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newGame) {
            setUpBoard();
        }
        return true;
    }

    public void setUpBoard() {
        areMinesSet = false;
        score = 0;
        textView.setText("" + score);
        gameOver = false;

        rows = new LinearLayout[n];
        buttons = new MyButton[n][n];
        squareLayout.removeAllViews();

        for (int i = 0; i < n; i++) {
            rows[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            rows[i].setLayoutParams(params);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            squareLayout.addView(rows[i]);
            if (i < n - 1) {
                View v = new View(this);
                v.setBackgroundColor(Color.GRAY);
                LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                v.setLayoutParams(par);
                squareLayout.addView(v);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new MyButton(this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                buttons[i][j].setPadding(10, 10, 10, 10);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setRow(i);
                buttons[i][j].setCol(j);
                buttons[i][j].setText("");
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                buttons[i][j].setBackgroundColor(Color.rgb(0, 255, 255));
                rows[i].addView(buttons[i][j]);


                if (j < n - 1) {
                    View v = new View(this);
                    v.setBackgroundColor(Color.GRAY);
                    LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT);
                    v.setLayoutParams(par);
                    rows[i].addView(v);
                }
            }
        }
    }

    private void addMines(int row, int col) {

        Random random = new Random();
        for (int i = 0; i < numberOfMines; i++) {
            int mineRow = random.nextInt(n);
            int mineCol = random.nextInt(n);
            if (buttons[mineRow][mineCol].getCellValue() == MINE) {
                i--;
            } else if (mineRow == row && mineCol == col) {
                i--;
            } else {
                buttons[mineRow][mineCol].setCellValue(MINE);
                for (int k = Math.max(0, mineRow - 1); k <= Math.min(mineRow + 1, n - 1); k++) {
                    for (int j = Math.max(0, mineCol - 1); j <= Math.min(mineCol + 1, n - 1); j++) {
                        if (buttons[k][j].getCellValue() != MINE) {
                            int value = buttons[k][j].getCellValue();
                            buttons[k][j].setCellValue(value + 1);
                        }
                    }
                }
            }
        }
    }
    /*private void resetBoard() {
        gameOver = false;
        addMines(temp);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j].setRow(i);
                buttons[i][j].setCol(j);
                buttons[i][j].setCellValue(temp[i][j]);
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundColor(Color.rgb(0, 255, 255));
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
            }
        }
    }*/

    @Override
    public void onClick(View v) {
        MyButton b = (MyButton) v;
        if (!areMinesSet) {
            areMinesSet = true;
            addMines(b.getRow(), b.getCol());
        }
        if (gameOver) {
            return;
        }
        if (b.isRevealed()) {
            return;
        } else if (b.getCellValue() == MINE) {
            int greenColorValue = Color.parseColor("#00ff00");

            b.setRevealed();
            b.setBackgroundColor(greenColorValue);
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
            revealAll();
            gameOver = true;
        } else if (b.getCellValue() == 0) {
            int col = b.getCol();
            int row = b.getRow();
            revealNeighbours(row, col);
        } else {
            score++;
            b.setRevealed();
        }
        if (rightSolution(buttons)) {
            gameOver = true;
            Toast.makeText(this, "You won", Toast.LENGTH_SHORT).show();
            revealAll();
        }
        textView.setText("" + score);

    }

    public boolean rightSolution(MyButton buttons[][]) {
        boolean solution = true;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (buttons[i][j].getCellValue() == MINE) {
                    solution = solution && !buttons[i][j].isRevealed();
                } else {
                    solution = solution && buttons[i][j].isRevealed();
                }
            }
            if (!solution) {
                return false;
            }
        }

        return solution;
    }


    private void revealNeighbours(int row, int col) {

        for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, n - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, n - 1); j++) {
                if ((i != row || j != col) && !buttons[i][j].isRevealed()) {
                    if (buttons[i][j].getCellValue() == 0) {
                        buttons[i][j].setRevealed();
                        score++;
                        revealNeighbours(i, j);
                    } else {
                        buttons[i][j].setRevealed();
                        score++;
                    }
                } else if (i == row && j == col && !buttons[i][j].isRevealed()) {
                    buttons[i][j].setRevealed();
                    score++;
                }

            }
        }
    }

    private void revealAll() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j].setRevealed();
            }
        }

        SharedPreferences sp=getSharedPreferences("Minesweeper",MODE_PRIVATE);

        int beginnerHighScore=sp.getInt("beginnerHighScore",0);
        int intermediateHighScore=sp.getInt("intermediateHighScore",0);
        int expertHighScore=sp.getInt("expertHighScore",0);
        SharedPreferences.Editor editor=sp.edit();
        if(n==8){
            if(beginnerHighScore<score) {
                editor.putInt("beginnerHighScore", score);
                editor.commit();
            }
        }
        if (n == 10) {
            if(intermediateHighScore<score) {
                editor.putInt("intermediateHighScore", score);
                editor.commit();
            }
        }
        if(n==12){
            if(expertHighScore<score) {
                editor.putInt("expertHighScore", score);
                editor.commit();
            }
        }
    }


    @Override
    public boolean onLongClick(View v) {
        MyButton b = (MyButton) v;
        if (!b.isRevealed()) {
            b.setBackgroundResource(R.drawable.flag);
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
