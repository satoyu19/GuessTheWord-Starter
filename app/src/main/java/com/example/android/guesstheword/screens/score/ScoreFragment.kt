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

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )

        //arguments!!が使えるのと使えないのは何が違う？
        viewModelFactory = ScoreViewModelFactory(ScoreFragmentArgs.fromBundle(requireArguments()).score)
        //ViewModelProvider.get()メソッドを呼び出し、紐づくScore FragmentコンテクストとviewModelFactoryを渡してください。
        // これによってviewModelFactoryクラスに定義されたファクトリーメソッドを使ってScoreViewModelオブジェクトが生成されます。
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

//        ViewModelバインディングにより不要
//        viewModel.score.observe(viewLifecycleOwner, Observer {
//            binding.scoreText.text = it.toString()
//        })
        binding.lifecycleOwner = viewLifecycleOwner

        binding.scoreViewModel = viewModel

        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                //eventPlayAgainをfalseにする
                viewModel.onPlayAgainComplete()
            }
        })

//        binding.playAgainButton.setOnClickListener { viewModel.onPlayAgain() } //eventPlayAgainをtrueに変更、　ゲーム画面へ遷移する
        return binding.root
    }
}
