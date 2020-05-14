/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Какие типы данных оносяца к примитивным ?",
                    answers = listOf("int, double, boolean, float, long", "string, boolean, float", "float, double", "all those")),
            Question(text = "Какой из модификаторов доступа недаёт доступ к полям класса ?",
                    answers = listOf("private, final", "private", "public", "void")),
            Question(text = "Каким символом вызывается метод ?",
                    answers = listOf(" . ", " , ", " / ", " ; ")),
            Question(text = "Что возвращает тип данных boolean ?",
                    answers = listOf("true/false", "0", "null", "error")),
            Question(text = "Какой из типов данных точнее float или double ?",
                    answers = listOf("double", "float", "они одинаковы", "long")),
            Question(text = "Какой метод используется для вывода информации в консоль ?",
                    answers = listOf("print()", "print{}", ".print()", "printing{}")),
            Question(text = "Чем отличается метод от класса ?",
                    answers = listOf("У метода нет полей", "У класса нет методов", "У методов нет классов", "У классов есть действия")),
            Question(text = "Каким образом можно сложить две строки \"Я люблю\" , \" KOTLIN \" ?",
                    answers = listOf("s1 = \"Я любюлю... s2 = \" KOTLIN \" s1 + s2 ", "Я люблю котлин", "s1 = Я люблю Айфон", "s2 = \"Я люблю KOTLIN\" ")),
            Question(text = "Что нужно сделать для того чтобы прибавить к \"10\" 43 ?",
                    answers = listOf("Преобразовать строку в число", "Преобразовать число в строку", "И то, и то", "Сложить строку и число")),
            Question(text = "Какой оператор отвечает за логическое \" ИЛИ \"?",
                    answers = listOf(" || ", " && ", " &| ", " |/ "))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater, R.layout.fragment_game, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.game = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController().navigate(GameFragmentDirections.navigateGameToGameWon(numQuestions,questionIndex))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController().navigate(GameFragmentDirections.navigateGameToGameOver())
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}
