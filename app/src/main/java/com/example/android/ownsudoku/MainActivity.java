package com.example.android.ownsudoku;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //TextView for the cells
    TextView[][] cell;
    //for showing the selected number
    TextView tv_selectedNum;
    //Buttons for the number pad
    Button[] numberPad;
    Button btn_del;

    Button btn_refresh;
    public static final String[] easyQuestion = {"1040040000200203", "1030000140000204",};
    public static final String[] hardQuestion = {"2003004002001000", "0030000140000240", "0400103000030020",
            "0403002001004000", "1000030200200004", "0040010310000200", "3004040000100002"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();


    }

    private void setUpUI() {
        bindTextViews();
        bindNumberButtons();
        setUpQuestions(getRandomQuestion(hardQuestion));
        setTextViewOnClick();
    }

    private String getRandomQuestion(String[] difficultyOfQuestion) {
        Random random = new Random();
        //50 is the maximum and the 1 is our minimum
        int n = random.nextInt(50) + 1;
        n %= difficultyOfQuestion.length;
        logCat("random number: " + n);
        return difficultyOfQuestion[n];
    }


    //the question should be from top to the bottom, from left to right
    private void setUpQuestions(String question) {
        //get each char of the given question using a for loop
        //the question must be of length 16 because this is a 4x4 sudoku
        if (question.length() != 16) {
            Log.d("MainActivity", "The question must be of the length 16.");
            return;
        }

        if (cell != null) {
            for (int i = 0; i < question.length(); i++) {
                int curNum = Integer.parseInt(String.valueOf(question.charAt(i)));
                //set up the question
                //set the clickable of the textViews having default numbers false to prevent changing its content
                final int row = i / 4;
                final int col = i % 4;
                if (curNum != 0) {
                    TextView curCell = cell[row][col];
                    logCat(row + "," + col);
                    curCell.setText(curNum + "");
                    curCell.setTypeface(null, Typeface.BOLD);
                    curCell.setOnClickListener(null);
                } else {
                    //set onClickListener to all the blank cells
                    final TextView curCell = cell[row][col];
                    curCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String selectedNum = (String) tv_selectedNum.getText();
                            if (selectedNum.equals("Del"))
                                selectedNum = "";
                            curCell.setText(selectedNum);
                            curCell.setTextColor(Color.GRAY);
                            boolean isRowValid = isRowValid(row, col);
                            boolean isColValid = isColValid(row, col);
                            boolean isLocalValid = isLocalValid(row, col);
                            //the player wins
                            if (isTheLastCell() && isColValid && isRowValid)
                                Toast.makeText(MainActivity.this, "Congratz! All correct!", Toast.LENGTH_LONG).show();

//                            curCell.setBackgroundColor(Color.GRAY);
//                            Toast.makeText(MainActivity.this, curCell.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        } else {
            Log.w("MainActivity", "Please bind the textViews first.");
        }
    }

    private void setTextViewOnClick() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

            }
        }
    }

    //to check if there is only one blank cell
    private boolean isTheLastCell() {
        int blankCellCounter = 0;
        boolean isOneCellLeft = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cell[i][j].getText().equals(""))
                    blankCellCounter++;
            }
        }
        if (blankCellCounter == 0) {
            isOneCellLeft = true;

        }
        logCat("Number of blank cell: " + blankCellCounter);
        return isOneCellLeft;
    }

    private boolean isLocalValid(int curRow, int curCol) {
        boolean isLocalValid = true;
        TextView cellJustEdited = cell[curRow][curCol];

        boolean isUpperPart = curRow < 2;
        boolean isLeftPart = curCol < 2;
        int quadrant = 1;
        //quadrant1 = upperLeft, quadrant2 = upperRight, quadrant3 = bottomLeft, quadrant4 = bottomRight
        if (isUpperPart && isLeftPart) {
            quadrant = 1;
            logCat("upperLeft");
        } else if (isUpperPart && !isLeftPart) {
            quadrant = 2;
            logCat("upperRight");
        } else if (!isUpperPart && isLeftPart) {
            quadrant = 3;
            logCat("bottomLeft");
        } else {
            quadrant = 4;
            logCat("bottomRight");
        }

        int rowStart = 0, colStart = 0, rowBoundary = 0, colBoundary = 0;
        switch (quadrant) {
            case 1:
                rowStart = 0;
                colStart = 0;
                rowBoundary = 2;
                colBoundary = 2;
                break;
            case 2:
                rowStart = 0;
                colStart = 2;
                rowBoundary = 2;
                colBoundary = 4;
                break;
            case 3:
                rowStart = 2;
                colStart = 0;
                rowBoundary = 4;
                colBoundary = 2;
                break;
            case 4:
                rowStart = 2;
                colStart = 2;
                rowBoundary = 4;
                colBoundary = 4;
                break;
        }
        for (int i = rowStart; i < rowBoundary; i++) {
            for (int j = colStart; j < colBoundary; j++) {
                //skip checking itself with itself
                if (i == curRow && j == curCol) {
//                logCat("itself: " + curRow + "," + j);
                    continue;
                } else {
                    TextView cellToBeCheck = cell[i][j];
                    //skip checking the blank cells
                    if (cellToBeCheck.equals("")) {
                        logCat("blank cell: " + i + "," + j);
                        continue;
                    } else if (cellJustEdited.getText().equals(cellToBeCheck.getText())) {
                        isLocalValid = false;
                        cellJustEdited.setTextColor(Color.RED);
                        cellToBeCheck.setTextColor(Color.RED);

                    } else {
                        cellToBeCheck.setTextColor(Color.BLACK);
                    }
                }
            }
        }
        return isLocalValid;
    }


    private boolean isRowValid(int curRow, int curCol) {
        boolean isRowValid = true;
        TextView cellJustEdited = cell[curRow][curCol];
        for (int j = 0; j < 4; j++) {
            //skip checking itself with itself
            if (j == curCol) {
                logCat("itself: " + curRow + "," + j);
                continue;
            } else {
                TextView cellToBeCheck = cell[curRow][j];
                //skip checking the blank cells
                if (cellToBeCheck.equals("")) {
                    logCat("blank cell: " + curRow + "," + j);
                    continue;
                } else if (cellJustEdited.getText().equals(cellToBeCheck.getText())) {
                    isRowValid = false;
                    cellJustEdited.setTextColor(Color.RED);
                    cellToBeCheck.setTextColor(Color.RED);

                } else {
                    cellToBeCheck.setTextColor(Color.BLACK);
                }
            }
        }

        return isRowValid;
    }

    private boolean isColValid(int curRow, int curCol) {
        boolean isColValid = true;
        TextView cellJustEditted = cell[curRow][curCol];

        for (int i = 0; i < 4; i++) {
            //skip checking itself with itself
            if (i == curRow) {
                logCat("itself: " + i + "," + curCol);
                continue;
            } else {
                TextView cellToBeCheck = cell[i][curCol];
                //skip checking the blank cells
                if (cellToBeCheck.equals("")) {
                    logCat("blank cell: " + i + "," + curCol);
                    continue;
                } else if (cellJustEditted.getText().equals(cellToBeCheck.getText())) {
                    isColValid = false;
                    cellJustEditted.setTextColor(Color.RED);
                    cellToBeCheck.setTextColor(Color.RED);

                } else {
                    cellToBeCheck.setTextColor(Color.BLACK);
                }
            }
        }

        return isColValid;
    }

    private void bindNumberButtons() {
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = getIntent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                finish();
//                startActivity(intent);
                setUpUI();


            }
        });
        btn_del = (Button) findViewById(R.id.btn_del);
        int numberOfButtons = 4;
        numberPad = new Button[numberOfButtons];
        for (int i = 0; i < numberOfButtons; i++) {
            String buttonName = "btn_num_" + (i + 1);
            int id = getStringIdentifier(this, buttonName);
//            logCat("Button id: " + id);
            numberPad[i] = (Button) findViewById(id);
        }
        setOnClickForNumberButtons();
    }

    private void setOnClickForNumberButtons() {
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_selectedNum.setText("Del");
            }
        });
        if (numberPad != null) {
            for (int i = 0; i < numberPad.length; i++) {
                final Button curButton = numberPad[i];
                curButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_selectedNum.setText(curButton.getText());
                    }
                });
            }
        }
    }


    private void bindTextViews() {
        //bind the textViews
        tv_selectedNum = (TextView) findViewById(R.id.tv_selectedNum);
        cell = new TextView[4][4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
//                logCat(row + "," + col);
                String textViewName = "cell" + (row + 1) + (col + 1);
                String textViewId = "R.id." + textViewName;
                int id = getStringIdentifier(this, textViewName);
//                logCat("id " + row + col + ": " + id);
                if (cell != null) {
                    cell[row][col] = (TextView) findViewById(id);
                    cell[row][col].setText("");
                    cell[row][col].setTextColor(Color.BLACK);

//                    logCat("success: " + row + "," + col);
                }
            }
        }
    }

    /**
     * Returns Identifier of String into it's ID as defined in R.java file.
     *
     * @param pContext
     * @param pString  defnied in Strings.xml resource name e.g: action_item_help
     * @return
     */
    private static int getStringIdentifier(Context pContext, String pString) {
        return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
    }

    private void logCat(String message) {
        Log.d("MainActivity", message);
    }
}
