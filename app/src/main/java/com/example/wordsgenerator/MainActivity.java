package com.example.wordsgenerator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*  ENGLISH WORDS GENERATOR v1.0
[Streams]           - Read data from file (word and translateWordsList in different files);
[List]              - ArrayList contain all data from read files;
[OnTouchListener]   - OnTouchListener when touched on screen word change (ConstraintLayout have variable and method myLayout.setOnTouchListener);
[Bundle]            - Bundle restore data when screen change orientation;
[Menu]              - Items in Menu witch have switch-case to define click of particular ItemMenu;
[Switch]            - Switch in Menu witch has setOnCheckedChangeListener;
[AlertDialog]       - AlertDialog in Menu is dialog with buttons 'Yes' or 'No' (are you sure to close the project? Yes : No).
 */

public final class MainActivity extends AppCompatActivity {
    private ArrayList<String> wordsList;
    private ArrayList<String> translateWordsList;
    private TextView word;
    private TextView translateWord;
    private ConstraintLayout myLayout = null;
    private int wordRandomNumber = 9735;

    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = findViewById(R.id.myLayout);
        word = findViewById(R.id.word);
        translateWord = findViewById(R.id.translate_word);

        try {
            wordsList = readWords();
            translateWordsList = readTranslateWords();
        } catch (IOException e) {
            e.printStackTrace();
        }

        word.setText(wordsList.get(wordRandomNumber));
        translateWord.setText(translateWordsList.get(wordRandomNumber));

        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        // check if we want to handle touch events, return true
                        // else don't handle further touch events, return false
                        return true;
                    // ... handle other cases

                    case MotionEvent.ACTION_UP:
                        wordRandomNumber = (int) (Math.random() * ((wordsList.size() - 1) + 1)) + 1;
                        word.setText(wordsList.get(wordRandomNumber));
                        translateWord.setText(translateWordsList.get(wordRandomNumber));
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        // finish handling touch events
                        // note that these methods won't be called if 'false' was returned
                        // from any previous events related to the gesture
                        break;
                }
                return true;
            }
        });

//        for(int i = 0; i < wordsList.size(); i++){
//            if(wordsList.get(i).equals("hello")){
//                Toast.makeText(getApplicationContext(), "id: "+i, Toast.LENGTH_LONG).show();
//            }
//        }
    }

    // [START CREATE DICTIONARIES OF WORDS]
    public final ArrayList<String> readWords() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.words)));
        while (bufferedReader.ready()) {
            list.add(bufferedReader.readLine());
        }
        bufferedReader.close();
        return list;
    }

    public final ArrayList<String> readTranslateWords() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.words_translate)));
        while (bufferedReader.ready()) {
            list.add(bufferedReader.readLine());
        }
        bufferedReader.close();
        return list;
    }
    // [END CREATE DICTIONARIES OF WORDS]


    // [START BUNDLE RESTORE DATA]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("WORD_NUMBER", wordRandomNumber);

        //outState.putString("WORD", word.getText().toString());
        //outState.putString("TRANSLATE_WORD", translateWord.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.wordRandomNumber = savedInstanceState.getInt("WORD_NUMBER");
        word.setText(wordsList.get(wordRandomNumber));
        translateWord.setText(translateWordsList.get(wordRandomNumber));
    }
    // [END BUNDLE RESTORE DATA]


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        aSwitch = menu.findItem(R.id.switchOnOffItem).getActionView().findViewById(R.id.switchOnOff);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    translateWord.setVisibility(View.INVISIBLE);
                } else {
                    translateWord.setVisibility(View.VISIBLE);
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Toast.makeText(getApplicationContext(), "ADD", Toast.LENGTH_SHORT).show();
                break;
            //[START EXIT FORM APP]
            case R.id.exit_project:
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setMessage("Вы хотите закрыть приложение?")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setTitle("Закрытие приложения");
                alertDialog.show();
                break;
            //[END EXIT FORM APP]
            case R.id.menu_on_off:
                if (translateWord.getVisibility() == View.VISIBLE) {
                    translateWord.setVisibility(View.INVISIBLE);
                    item.setTitle("Turn on translate");
                } else {
                    translateWord.setVisibility(View.VISIBLE);
                    item.setTitle("Turn off translate");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}