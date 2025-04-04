package com.example.codelingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private String topicTitle;
    private Map<String, QuizData> quizDataMap = new HashMap<>();
    private int currentQuestionIndex = 0;
    private QuizData currentQuizData;
    private ProgressBar progressBar;
    private boolean questionAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get data from intent
        topicTitle = getIntent().getStringExtra("TOPIC_TITLE");

        // Set title in header
        TextView headerTitle = findViewById(R.id.header_title);
        headerTitle.setText(topicTitle);

        // Set section title
        TextView sectionTitle = findViewById(R.id.section_title);
        sectionTitle.setText("SECTION 1, UNIT 1");

        // Initialize progress bar
        progressBar = findViewById(R.id.progressBar);

        // Initialize quiz data
        initializeQuizData();

        // Get quiz data for current topic
        currentQuizData = quizDataMap.get(topicTitle);
        if (currentQuizData == null) {
            // Fallback to default quiz if the topic doesn't have specific questions
            currentQuizData = quizDataMap.get("default");
        }

        // Update progress bar
        progressBar.setMax(currentQuizData.questions.length);
        progressBar.setProgress(currentQuestionIndex + 1);

        // Display first question
        displayCurrentQuestion();

        // Setup answer options
        setupAnswerOptions();
    }

    private void initializeQuizData() {
        // Quiz data for Print Statement
        QuizData printQuiz = new QuizData();
        printQuiz.questions = new String[]{
                "What statement is used to display text in Java?",
                "What will System.out.println(\"Hello\"); do?"
        };
        printQuiz.options = new String[][]{
                {"System.out.println()", "console.log()", "print()"},
                {"Display \"Hello\" on the screen", "Create a variable named Hello", "Cause an error"}
        };
        printQuiz.correctAnswers = new int[]{0, 0};
        quizDataMap.put("Print Statement", printQuiz);

        // Quiz data for Variables
        QuizData variablesQuiz = new QuizData();
        variablesQuiz.questions = new String[]{
                "What is the correct way to declare an integer variable in Java?",
                "Which of these is a valid variable name in Java?"
        };
        variablesQuiz.options = new String[][]{
                {"int x = 5;", "x = 5;", "integer x = 5;"},
                {"my_variable", "2variable", "class"}
        };
        variablesQuiz.correctAnswers = new int[]{0, 0};
        quizDataMap.put("Variables", variablesQuiz);

        // Default quiz for other topics
        QuizData defaultQuiz = new QuizData();
        defaultQuiz.questions = new String[]{
                "Question 1",
                "Question 2"
        };
        defaultQuiz.options = new String[][]{
                {"Answer 1", "Answer B", "Answer C"},
                {"Option X", "Option Y", "Option Z"}
        };
        defaultQuiz.correctAnswers = new int[]{0, 1};
        quizDataMap.put("default", defaultQuiz);
    }

    private void displayCurrentQuestion() {
        // Set question text
        TextView questionText = findViewById(R.id.question_text);
        questionText.setText(currentQuizData.questions[currentQuestionIndex]);

        // Set answer texts
        TextView answerText1 = findViewById(R.id.answer_text_1);
        TextView answerText2 = findViewById(R.id.answer_text_2);
        TextView answerText3 = findViewById(R.id.answer_text_3);

        answerText1.setText(currentQuizData.options[currentQuestionIndex][0]);
        answerText2.setText(currentQuizData.options[currentQuestionIndex][1]);
        answerText3.setText(currentQuizData.options[currentQuestionIndex][2]);

        // Reset card backgrounds
        resetCardBackgrounds();

        // Reset question answered flag
        questionAnswered = false;
    }

    private void resetCardBackgrounds() {
        CardView answerCard1 = findViewById(R.id.answer_card_1);
        CardView answerCard2 = findViewById(R.id.answer_card_2);
        CardView answerCard3 = findViewById(R.id.answer_card_3);

        answerCard1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
        answerCard2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
        answerCard3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
    }

    private void setupAnswerOptions() {
        CardView answerCard1 = findViewById(R.id.answer_card_1);
        CardView answerCard2 = findViewById(R.id.answer_card_2);
        CardView answerCard3 = findViewById(R.id.answer_card_3);

        // Set click listeners
        answerCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionAnswered) {
                    checkAnswer(0);
                }
            }
        });

        answerCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionAnswered) {
                    checkAnswer(1);
                }
            }
        });

        answerCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!questionAnswered) {
                    checkAnswer(2);
                }
            }
        });
    }

    private void checkAnswer(int selectedAnswerIndex) {
        CardView answerCard1 = findViewById(R.id.answer_card_1);
        CardView answerCard2 = findViewById(R.id.answer_card_2);
        CardView answerCard3 = findViewById(R.id.answer_card_3);
        CardView[] answerCards = {answerCard1, answerCard2, answerCard3};

        // Mark question as answered to prevent multiple selections
        questionAnswered = true;

        // Check if answer is correct
        int correctIndex = currentQuizData.correctAnswers[currentQuestionIndex];
        boolean isCorrect = (selectedAnswerIndex == correctIndex);

        // Highlight selected answer
        answerCards[selectedAnswerIndex].setCardBackgroundColor(
                ContextCompat.getColor(this, isCorrect ? R.color.green : R.color.red));

        // If incorrect, highlight the correct answer
        if (!isCorrect) {
            answerCards[correctIndex].setCardBackgroundColor(
                    ContextCompat.getColor(this, R.color.green));
        }

        // Show feedback
        String feedback = isCorrect ? "Correct!" : "Try again!";
        Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show();

        // Move to next question after a delay
        answerCards[0].postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToNextQuestion();
            }
        }, 1500);
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;

        // Check if we've reached the end of the quiz
        if (currentQuestionIndex < currentQuizData.questions.length) {
            // Update progress
            progressBar.setProgress(currentQuestionIndex + 1);

            // Show next question
            displayCurrentQuestion();
        } else {
            // Quiz complete, return to main menu
            Toast.makeText(this, "Quiz completed! Well done!", Toast.LENGTH_LONG).show();

            // Delay before returning to main menu
            new View(this).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }, 2000);
        }
    }

    // Inner class to hold quiz data
    private static class QuizData {
        String[] questions;
        String[][] options;
        int[] correctAnswers;
    }
}