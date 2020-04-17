package com.bvm.myapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class Second extends AppCompatActivity {
    public static final long COUNTDOWN_IN_MILLIS = 30000;
    private static TextView Question_text;
    private  static TextView Question_Count;
    private static TextView score;
    private static RadioButton rb1;
    private static RadioButton rb2;
    private static RadioButton rb3;
    private static RadioGroup rbGroup;
    private static String Username;
    private long backPressedTime;
    DatabaseReference myref;
    List<Integer> indexArray = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
    private static Button btn;
    private  String answerCorrect;
    private int questionTotal = 10;
    private int var = 0;
    private int Score = 0;
    private boolean answered;
    private static TextView textViewCountDown;
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    String[] questions = new String[]{"Question 1", "Question 2",
            "Question 3", "Question 4", "Question 5", "Question 6",
            "Question 7", "Question 8", "Question 9", "Question 10"};
    String[] Questions = new String[10];
    String[] option1 = new String[10];
    String[] option2 = new String[10];
    String[] option3 = new String[10];
    Integer[] correctanswer = new Integer[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Question_text = findViewById(R.id.text_view_question);
        Question_Count = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdonw);
        score = findViewById(R.id.text_view_score);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        btn = findViewById(R.id.button);
        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        Intent intent = getIntent();
        Username = intent.getStringExtra("Username");
        Collections.shuffle(indexArray);
        myref = FirebaseDatabase.getInstance().getReference();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=0;i<10;i++) {
                    Questions[i] = dataSnapshot.child(questions[i]).child("Que").getValue().toString();
                    option1[i] = dataSnapshot.child(questions[i]).child("option 1").getValue().toString();
                    option2[i] = dataSnapshot.child(questions[i]).child("option 2").getValue().toString();
                    option3[i] = dataSnapshot.child(questions[i]).child("option 3").getValue().toString();
                    answerCorrect = dataSnapshot.child(questions[i]).child("ans").getValue().toString();
                    correctanswer[i] = AnswerFunction(answerCorrect);


                }
                Question_text.setText(Questions[indexArray.get(var)]);
                rb1.setText(option1[indexArray.get(var)]);
                rb2.setText(option2[indexArray.get(var)]);
                rb3.setText(option3[indexArray.get(var)]);
                btn.setText("Confirm");
                Question_Count.setText("Question" + (var + 1) + " / " +questionTotal);
                score.setText("Score: " + Score);




            }

            private int AnswerFunction( String correctAnswer) {
                int Answer = 0;
                switch (correctAnswer) {
                    case "1":
                        Answer = 1;
                        break;
                    case "2":
                        Answer = 2;
                        break;
                    case "3":
                        Answer = 3;
                        break;
                }
                return Answer;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        checkAnswer();
                    }else{
                        Toast.makeText(Second.this,"Please select one answer",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    var++;
                    showNextQuestion();
                }
            }

            private void showNextQuestion() {
                answered = false;
                rb1.setTextColor(textColorDefaultRb);
                rb2.setTextColor(textColorDefaultRb);
                rb3.setTextColor(textColorDefaultRb);
                rbGroup.clearCheck();

                if(var < questionTotal){
                    Question_text.setText(Questions[indexArray.get(var)]);
                    rb1.setText(option1[indexArray.get(var)]);
                    rb2.setText(option2[indexArray.get(var)]);
                    rb3.setText(option3[indexArray.get(var)]);
                    btn.setText("Confirm");
                    //startCountDown();
                    Question_Count.setText("Question" + (var + 1) + " / " +questionTotal);
                    score.setText("Score: " + Score);
                    timeLeftInMillis = COUNTDOWN_IN_MILLIS;
                }else{
                    finishQuiz();
                }
            }





            private void finishQuiz() {
                Toast.makeText(Second.this,"Your score has been submitted",Toast.LENGTH_SHORT ).show();
                Scoree(Score);
                Intent intent = new Intent(Second.this,MainActivity.class);
                startActivity(intent);
            }



            private void checkAnswer() {
                answered = true;



                RadioButton rbSelected =findViewById(rbGroup.getCheckedRadioButtonId());
                int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
                if(answerNr == correctanswer[indexArray.get(var)] ){
                    Score++;
                    score.setText("Score: " + Score );
                }
                showSolution();
            }


            private void showSolution() {
                rb1.setTextColor(Color.RED);
                rb2.setTextColor(Color.RED);
                rb3.setTextColor(Color.RED);
                switch (correctanswer[indexArray.get(var)]){
                    case 1:
                        rb1.setTextColor(Color.GREEN);
                        Question_text.setText("Answer 1 is correct");
                        break;
                    case 2:
                        rb2.setTextColor(Color.GREEN);
                        Question_text.setText("Answer 2 is correct");
                        break;
                    case 3:
                        rb3.setTextColor(Color.GREEN);
                        Question_text.setText("Answer 3 is correct");
                        break;
                }
                if (var < questionTotal) {
                    btn.setText("NEXT");
                }else{
                    btn.setText("FINISH");
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    private void finishQuiz() {
        Scoree(Score);
        Toast.makeText(Second.this,"Your result has been submitted",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Second.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void Scoree(final int highScore){
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myref.child("Login Info").child(Username).child("score").setValue(highScore);
                myref.child("Login Info").child(Username).child("flag").setValue("done");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
