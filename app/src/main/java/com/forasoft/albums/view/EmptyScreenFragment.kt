package com.forasoft.albums.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.forasoft.albums.R
import com.forasoft.albums.databinding.EmptyScreenFragmentBinding

const val TEXT_ARGUMENT = "emptySearchText"

class EmptyScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EmptyScreenFragmentBinding.inflate(inflater, container, false)
        binding.text = arguments?.getString(TEXT_ARGUMENT) ?: getString(R.string.search_prompt)
        return binding.textView.rootView
    }
}