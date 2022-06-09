/*
 * Copyright (C) 2019 Google Inc.
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

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    //ViewModel変数
    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        //LiveDataオブジェクトのスコープを決め、game_fragment.xmlレイアウト内のビューを自動で更新できるようにします。
        binding.lifecycleOwner = viewLifecycleOwner

            //ViewModel初期化
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        //ViewModelとレイアウトファイルの紐付け
        binding.gameViewModel = viewModel

        //ViewModelバインディングにより不要
//        binding.correctButton.setOnClickListener { onCorrect() }
//        binding.skipButton.setOnClickListener { onSkip() }
//        binding.endGameButton.setOnClickListener { onEndGame() }
//        viewModel.word.observe(viewLifecycleOwner, Observer {
//            binding.wordText.text = it
//        })

        //Observerオブジェクトの取り付け,データに変更があった場合にイベントを受け取理、処理を実行する。
        viewModel.score.observe(viewLifecycleOwner, Observer {
            binding.scoreText.text = it.toString()
            Log.i("GuessTheWord", "画面を回転すると非アクティブ状態からアクティブ状態に変化し、更新を受ける")
        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer {
            if (it) gameFinished()  //eventGameFinishを監視、trueになったらゲームを終了
        })

        return binding.root
    }

    //得点をフラグメントに引き渡し
    private fun gameFinished(){
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
//        SafeArgsの依存関係書いてないのになぜ使える?クラスパスしかないがそれだけで行ける？
        val action = GameFragmentDirections.actionGameToScore()
        //viewModel.score.valueがnullであれば0を返す
        action.score = viewModel.score.value?:0
        NavHostFragment.findNavController(this).navigate(action)
    }
//      ViewModelバインディングにより不要
//    private fun onEndGame(){
//        gameFinished()
//    }

//    private fun onSkip() {
//        viewModel.onSkip()
//    }
//
//    private fun onCorrect() {
//        viewModel.onCorrect()
//    }

    //Viewに関する処理であり、計算等のロジックがないため、UI controllerに書くのが適している
//    private fun updateWordText() {
//        binding.wordText.text = viewModel.word.value
//    }
//
//    private fun updateScoreText() {
//        binding.scoreText.text = viewModel.score.value.toString()
//    }
}
