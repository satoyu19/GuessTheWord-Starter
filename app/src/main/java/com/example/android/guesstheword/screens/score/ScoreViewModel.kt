package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {

    var score = finalScore

    init {
        //確認用ログ
        Log.i("ScoreViewModel", "Final score is $finalScore")
    }

}