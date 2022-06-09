package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ScoreViewModelFactory(private val finalScore: Int): ViewModelProvider.Factory {

    //指定されたの新しいclassインスタンスを作成します。
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //このオブジェクトによって表されるクラスまたはインターフェースが、
        // 指定されたパラメーターによって表されるクラスまたはインターフェースと同じであるか、
        // スーパークラスまたはスーパーインターフェースであるかを判別します。
        if(modelClass.isAssignableFrom(ScoreViewModel::class.java)){
            return ScoreViewModel(finalScore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}