package com.forasoft.albums.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.forasoft.albums.databinding.EmptyScreenFragmentBinding

const val TEXT_ARGUMENT = "emptySearchText"

/**
 * Shows text to user. Primarily shows prompt and error texts
 */
class EmptyScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EmptyScreenFragmentBinding.inflate(inflater, container, false)
        arguments?.getString(TEXT_ARGUMENT)?.let { binding.text = it }
        return binding.textView.rootView
    }
}