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

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.databinding.FragmentGameWonBinding


class GameWonFragment : Fragment() {

    // ACTIVATING SAFE ARGS PLUGIN FOR THAT FRAGMENT. NEED TO BE DONE IF WE NEED TO USE SOME PASSED ARGUMENTS.
    val args: GameWonFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_game_won, container, false)
        binding.nextMatchButton.setOnClickListener { view ->
            view.findNavController().navigate(GameWonFragmentDirections.navigateGameWonToGame())
        }
        // SHOWING HOW MUCH IS CORRECT AND HM WAS QUESTIONS
        Toast.makeText(context,
                "Correct answers is : ${args.numCorrect}, Number of questions is : ${args.numQuestions}",
                Toast.LENGTH_LONG).show()
        setHasOptionsMenu(true)
        return binding.root
    }

    // WINNER OPTION MENU
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu,menu)
        if (null == getShareIntent().resolveActivity(activity!!.packageManager)) {
            menu.findItem(R.id.share).isVisible = false
        }
    }
    // SHARE FUNCTION
    private fun getShareIntent(): Intent {
        return ShareCompat.IntentBuilder.from(activity)
                .setText(getString(R.string.share_success_text, args.numCorrect, args.numQuestions))
                .setType("text/plain")
                .intent
    }
    // START SHARE ACTIVITY
    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    // ON SHARE OPTION CLICK FUNCTION
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
