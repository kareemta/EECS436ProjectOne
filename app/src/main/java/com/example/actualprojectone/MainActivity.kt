package com.example.actualprojectone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {


    // Get all of the different views needed
    // Image view
    private lateinit var diceImage: ImageView

    // button views
    private lateinit var rollBtn: Button
    private lateinit var submitBtn: Button
    // edit text view to submit your answer
    private lateinit var answerInput: EditText

    private lateinit var problemDisplay: TextView // displays the current problem
    private lateinit var pOneScore: TextView // the scores of player one and two
    private lateinit var pTwoScore: TextView
    private lateinit var currentPlayer: TextView // who's turn it is currently
    private lateinit var currJackpot: TextView // how much the current jackpot is

    // the scores of the two players
    private var playerOneScore = 0
    private var playerTwoScore = 0

    // who the current player is and what the jackpot value is
    private var currPlayer = 1
    private var jackPot = 5

    // if we are rolling for double points or not
    private var doublePoints = false

    // if we are going for the jackpot
    private var jackpotAttempt = false

    // what the answer is for the current problem
    private var currentAns: Int = 0
    private var lastRoll = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // pairing the variables with the ID from the xml file
        diceImage = findViewById(R.id.ivDie)
        rollBtn = findViewById(R.id.btRoll)
        submitBtn = findViewById(R.id.btSubmit)
        answerInput = findViewById(R.id.etAnswer)
        problemDisplay = findViewById(R.id.tvMathProblem)
        pOneScore = findViewById(R.id.tvPOneScore)
        pTwoScore = findViewById(R.id.tvPTwoScore)
        currentPlayer = findViewById(R.id.tvTurn)
        currJackpot = findViewById(R.id.tvCurrJackpot)

        // what happens if we click each button
        rollBtn.setOnClickListener {
            rollDie()
        }

        submitBtn.setOnClickListener {
            checkAnswer()
        }
    }

    private fun rollDie() {


        val roll = Random.nextInt(1, 7)
        lastRoll = roll
        val drawableResource = when (roll) {
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            else -> R.drawable.dice6
        }

        diceImage.setImageResource(drawableResource)

        when (roll) {
            1 -> addition()
            2 -> subtraction()
            3 -> multiply()
            4 -> rollAgainForDouble()
            5 -> loseTurn()
            6 -> tryForJackpot()

        }
    }

    private fun addition() {

        val numberOne = Random.nextInt(0, 100)
        val numberTwo = Random.nextInt(0, 100)
        currentAns = numberOne + numberTwo
        problemDisplay.text = "Solve: ${numberOne} + ${numberTwo}"

    }

    private fun subtraction() {

        val numberOne = Random.nextInt(0, 100)
        val numberTwo = Random.nextInt(0, 100)
        currentAns = numberOne - numberTwo
        problemDisplay.text = "Solve: ${numberOne} - ${numberTwo}"
    }

    private fun multiply() {
        val numberOne = Random.nextInt(0, 20)
        val numberTwo = Random.nextInt(0, 20)
        currentAns = numberOne * numberTwo

        problemDisplay.text = "Solve: ${numberOne} * ${numberTwo}"
    }

    private fun rollAgainForDouble() {
        doublePoints = true
        problemDisplay.text = "Roll again for double points!"
    }

    private fun loseTurn() {
        currPlayer = if (currPlayer == 1) 2 else 1
        updateCurrentPlayerDisplay()
        problemDisplay.text = "You Lost a Turn!"
    }

    private fun tryForJackpot() {
        jackpotAttempt = true
        val numberOne = Random.nextInt(0, 20)
        val numberTwo = Random.nextInt(0, 20)

        currentAns = numberOne * numberTwo
        problemDisplay.text = "Solve: ${numberOne} * ${numberTwo}"

    }

    private fun updateScore() {
        // Check if we are doing double points or trying for jackpot

        var pointsEarned = 0

        if (doublePoints) {
            pointsEarned = lastRoll * 2
        } else if (jackpotAttempt) {
            pointsEarned = jackPot
            jackPot = 5
            currJackpot.text = "$jackPot"
        } else {
            pointsEarned = lastRoll
        }

        if (currPlayer == 1) {
            playerOneScore += pointsEarned
            pOneScore.text = "${playerOneScore}"
        } else {
            playerTwoScore += pointsEarned
            pTwoScore.text = "${playerTwoScore}"
        }

        jackpotAttempt = false
        doublePoints = false
        checkForWinner()
    }

    private fun checkAnswer() {
        val userInput = answerInput.text.toString().toIntOrNull()

        if (userInput != null && userInput == currentAns) {
            updateScore()
            problemDisplay.text = "Correct!"
        } else {
            problemDisplay.text = "Incorrect!"
            jackPot += lastRoll
            currJackpot.text = "$jackPot"

        }
        currPlayer = if (currPlayer == 1) 2 else 1
        updateCurrentPlayerDisplay()
    }

    // function to update the player display
    private fun updateCurrentPlayerDisplay() {
        val playerText = if (currPlayer == 1) "1" else "2"
        currentPlayer.text = playerText
    }

    private fun checkForWinner() {
        if (playerOneScore >= 20) {
            endGame(winner = 1)
        } else if (playerTwoScore >= 20) {
            endGame(winner = 2)
        }
    }

    private fun endGame(winner: Int) {
        problemDisplay.text = "Player $winner is the winner!"

        diceImage.postDelayed({
            playerOneScore = 0
            playerTwoScore = 0
            currPlayer = 1
            jackPot = 5
            doublePoints = false
            currentAns = 0
            lastRoll = 0

            pOneScore.text = "0"
            pTwoScore.text = "0"
            currJackpot.text = "$jackPot"
            updateCurrentPlayerDisplay()
            problemDisplay.text = "Roll to start!"
        }, 2000)
    }



}