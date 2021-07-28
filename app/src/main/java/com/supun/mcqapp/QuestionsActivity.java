package com.supun.mcqapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArrayMap;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.lang.Integer;

import static com.supun.mcqapp.SetsActivity.category_id;
import static com.supun.mcqapp.SetsActivity.setsIDs;
import static com.supun.mcqapp.MainActivity.selected_cat_index;
import static com.supun.mcqapp.MainActivity.catList;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, qCount, timer;
    private Button option1, option2, option3, option4;
    private List<Question> questionList;
    private int quesNum;
    private CountDownTimer countDown;
    private int score;
    private FirebaseFirestore firestore;
    private int setNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        question = findViewById(R.id.question);
        qCount = findViewById(R.id.qcount);
        timer = findViewById(R.id.timer);

        option1 = findViewById(R.id.optin_a_btn);
        option2 = findViewById(R.id.optin_b_btn);
        option3 = findViewById(R.id.optin_c_btn);
        option4 = findViewById(R.id.optin_d_btn);

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        questionList = new ArrayList<>();

        setNo = getIntent().getIntExtra("SETNO",1);

        //add below line
        firestore = FirebaseFirestore.getInstance();

        getQuestionsList();

        score = 0;
    }


    private void getQuestionsList()
    {
        questionList.clear();

        //questionList = new ArrayList<>();

        firestore.collection("QUIZ").document("CAT" + String.valueOf(category_id))
                .collection("SET" + String.valueOf(setNo))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot questions = task.getResult();
                    for (QueryDocumentSnapshot doc : questions) {
                        questionList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                Integer.valueOf(doc.getString("ANSWER"))
                        ));
                    } setQuestion();
                }
                else { Toast.makeText(QuestionsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();}
            }
        }) ;
    }

    private void setQuestion()
    {
        timer.setText(String.valueOf(40));

        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());

        qCount.setText(String.valueOf(1)+"/"+ String.valueOf(questionList.size()));

        startTimer();

        quesNum = 0;
    }

    private void startTimer()
    {
        countDown = new CountDownTimer(40000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                changeQuestion();

            }
        };
        countDown.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int selectedOption = 0;

        switch (v.getId())
        {
            case R.id.optin_a_btn:
                selectedOption = 1;
                break;
            case R.id.optin_b_btn:
                selectedOption = 2;
                break;

            case R.id.optin_c_btn:
                selectedOption = 3;
                break;

            case R.id.optin_d_btn:
                selectedOption = 4;
                break;
            default:
        }

        countDown.cancel();
        checkAnswer(selectedOption, v);

    }

    //Remove tint list
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view)
    {
        if(selectedOption == questionList.get(quesNum).getCorrectAns())
        {
            //Right ans
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            score++;
        }
        else
        {
            //Wrong ans
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            switch (questionList.get(quesNum).getCorrectAns())
            {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;

                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;

                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;

                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;
            }
        }
        changeQuestion();
    }
    private void changeQuestion()
    {
        if( quesNum < questionList.size()- 1)
        {
            quesNum++;

            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);

            qCount.setText(String.valueOf(quesNum+1)+"/" + String.valueOf(questionList.size()));

            timer.setText(String.valueOf(40));
            startTimer();

        }
        else
        {
            //score activity
            Intent intent = new Intent(QuestionsActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score) + "/" + String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionsActivity.this.finish();
        }
    }
    private void playAnim(final View view, final int value, int viewNum)
    {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(1000)
                .setStartDelay(300).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if (value == 0)
                        {
                            switch (viewNum)
                            {
                                case 0:
                                    ((TextView)view).setText(questionList.get(quesNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(quesNum).getOptionA());
                                    break;

                                case 2:
                                    ((Button)view).setText(questionList.get(quesNum).getOptionB());
                                    break;

                                case 3:
                                    ((Button)view).setText(questionList.get(quesNum).getOptionC());
                                    break;

                                case 4:
                                    ((Button)view).setText(questionList.get(quesNum).getOptionD());
                                    break;
                            }

                            playAnim(view,1,viewNum);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        countDown.cancel();
    }
}