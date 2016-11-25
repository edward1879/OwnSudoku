package com.example.android.ownsudoku;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TextView for the cells
    TextView[][] cell;
    //for showing the selected number
    TextView tv_selectedNum;
    //Buttons for the number pad
    Button[] numberPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();
        getTextFromAllTV();

    }

    private void setUpUI(){
        bindTextViews();
        bindNumberButtons();
    }

    private void getTextFromAllTV() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (cell != null){
                    final TextView curCell = cell[i][j];
                    curCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String selectedNum = (String) tv_selectedNum.getText();
                            curCell.setText(selectedNum);
//                            curCell.setBackgroundColor(Color.GRAY);
//                            Toast.makeText(MainActivity.this, curCell.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void bindNumberButtons(){
        int numberOfButtons = 4;
        numberPad = new Button[numberOfButtons];
        for (int i = 0;i<numberOfButtons;i++){
            String buttonName = "btn_num_"+(i+1);
            int id = getStringIdentifier(this,buttonName);
            logCat("Button id: "+id);
            numberPad[i] = (Button) findViewById(id);
        }
        setOnClickForNumberButtons();
    }

    private void setOnClickForNumberButtons(){
        if (numberPad!=null){
            for (int i = 0;i<numberPad.length;i++){
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
                logCat(row + "," + col);
                String textViewName = "cell" + (row+1) + (col+1);
                String textViewId = "R.id." + textViewName;
                int id = getStringIdentifier(this, textViewName);
                logCat("id "+ row+col+": "+id);
                if (cell != null) {
                    cell[row][col] = (TextView) findViewById(id);
                    cell[row][col].setText("");
                    logCat("success: "+row+","+col);
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
