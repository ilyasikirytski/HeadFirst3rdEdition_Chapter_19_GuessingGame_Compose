package com.example.chapter_19_guessinggame_compose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val words = listOf("Android", "Activity", "Fragment")
    private val secretWord = words.random().uppercase()
    private var correctGuesses = ""
    private val secretWordDisplay = MutableLiveData<String>()
    val getSecretWordDisplay: LiveData<String> get() = secretWordDisplay

    private val incorrectGuesses = MutableLiveData<String>("")
    val getIncorrectGuesses: LiveData<String> get() = incorrectGuesses

    private val livesLeft = MutableLiveData<Int>(8)
    val getLivesLeft: LiveData<Int> get() = livesLeft

    private val gameOver = MutableLiveData<Boolean>(false)
    val getGameOver: LiveData<Boolean> get() = gameOver

    init {
        secretWordDisplay.value = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> "_"
    }

    fun makeGuess(guess: String) {
        if (secretWord.contains(guess)) {
            correctGuesses += guess
            secretWordDisplay.value = deriveSecretWordDisplay()
        } else {
            incorrectGuesses.value += guess
            livesLeft.value = livesLeft.value?.minus(1)
        }
        if (isWon() || isLost()) {
            gameOver.value = true
        }
    }

    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)

    private fun isLost() = (livesLeft.value ?: 0) <= 0

    fun wonLostMessage(): String {
        var message = ""
        if (isWon()) {
            message = "You Win!"
        } else if (isLost()) {
            message = "You Lost!"
        }
        message += "The word was $secretWord."
        return message
    }

    fun finishGame() {
        gameOver.value = true
    }
}