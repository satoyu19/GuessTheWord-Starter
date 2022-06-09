package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

//ViewModelを実装
class GameViewModel: ViewModel() {

    companion object{
        //game over time
        private const val DONE = 0L;
        //interval
        private const val ONE_SECOND = 1000L
//        total time
        private const val COUNTDOWN_TIME = 60000L
    }

    //countdown Time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
    get() = _currentTime
    //LiveDateの操作
    val currentTimeString = Transformations.map(currentTime){
        DateUtils.formatElapsedTime(it)
    }
    private val timer: CountDownTimer

    // The current word
    //監視可能な値、バッキングプロパティを追加(バッキングプロパティを使うとオブジェクトそのものでなくゲッターから何かを返すことができます。)
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
    get() = _word

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
    get() = _score

    //フラグメントの情報を伝達するための監視されるオブジェクト
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
    get() = _eventGameFinish

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        //確認用ログ
        Log.i("GameViewModel", "GameViewModel created!!")
//        ViewModelが生成されたときに単語リストをリセットする
        resetList()
        nextWord()
        //setValue()メソッドを使用
        _word.value = ""
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            //インターバル毎に呼び出される
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }
            //タイマーが終了した時に呼び出される
            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }

        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
            //確認用ログ
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }

    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        if (!wordList.isEmpty()) {
            //Select and remove a word from the list
            _word.value = wordList.removeAt(0)
        } else{ //wordListが空だったら
            resetList() //ここ合ってる？
        }
    }

    fun onCorrect() {
//        score++
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinish(){
        _eventGameFinish.value = true
    }

    fun onSkip() {
//        score--
        _score.value = (score.value)?.minus(1)
        nextWord()
    }



}