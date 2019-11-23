package com.forasoft.albums.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.forasoft.albums.R
import com.forasoft.albums.TunesApp
import com.forasoft.albums.databinding.SongsListFragmentBinding
import com.forasoft.albums.model.State
import com.forasoft.albums.viewmodel.Album
import com.forasoft.albums.viewmodel.TunesViewModel
import kotlinx.android.synthetic.main.songs_list_fragment.*
import javax.inject.Inject

const val ALBUM_ARGUMENT = "album"

/**
 * Songs list screen
 */
class SongsListFragment : Fragment() {
    companion object {
        private val TAG = SongsListFragment::class.java.simpleName
    }

    private val adapter by lazy { SongAdapter() }
    @Inject
    lateinit var viewModel: TunesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as? TunesApp)?.appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SongsListFragmentBinding.inflate(inflater, container, false)
        arguments?.getParcelable<Album>(ALBUM_ARGUMENT)?.let {
            binding.album = it
            binding.executePendingBindings()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songs_list.layoutManager = LinearLayoutManager(songs_list.context)
        songs_list.adapter = adapter

        arguments?.getParcelable<Album>(ALBUM_ARGUMENT)?.let { album ->
            viewModel.loadTracks(album).observe(this, Observer {
                when (it.state) {
                    State.LOADING -> progress_bar.visibility = View.VISIBLE
                    State.FAILURE -> {
                        val direction =
                            SongsListFragmentDirections.actionSongsListFragmentToEmptyScreenFragment(
                                getString(R.string.network_failure)
                            )
                        findNavController().navigate(direction)
                    }
                    State.SUCCESS -> {
                        it.result?.let { songs -> adapter.setData(songs) }
                        progress_bar.visibility = View.GONE
                    }
                }
            })
        }
    }
}