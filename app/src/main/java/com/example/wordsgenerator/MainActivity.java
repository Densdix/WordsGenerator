package com.example.wordsgenerator;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class MainActivity extends AppCompatActivity {
    private ArrayList<String> wordsList;
    private ArrayList<String> translateWordsList;
    private TextView word;
    private TextView translateWord;
    private ConstraintLayout myLayout = null;
    private int wordRandomNumber = 9735;

//    public void _$_clearFindViewByIdCache() {
//        if (this.findViewCache != null) {
//            this.findViewCache.clear();
//        }
//    }
//
//    public View _$_findCachedViewById(int i) {
//        if (this.findViewCache == null) {
//            this.findViewCache = new HashMap();
//        }
//        View view = (View) this.findViewCache.get(Integer.valueOf(i));
//        if (view != null) {
//            return view;
//        }
//        View findViewById = findViewById(i);
//        this.findViewCache.put(Integer.valueOf(i), findViewById);
//        return findViewById;
//    }

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
}
