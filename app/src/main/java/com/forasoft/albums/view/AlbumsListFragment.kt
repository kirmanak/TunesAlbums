package com.forasoft.albums.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.forasoft.albums.R
import com.forasoft.albums.TunesApp
import com.forasoft.albums.model.RequestResult
import com.forasoft.albums.model.State
import com.forasoft.albums.viewmodel.Album
import com.forasoft.albums.viewmodel.TunesViewModel
import kotlinx.android.synthetic.main.albums_list_fragment.*
import javax.inject.Inject

/**
 * Albums list screen
 */
class AlbumsListFragment : Fragment() {

    companion object {
        private val TAG = AlbumsListFragment::class.java.simpleName
    }

    private val albumAdapter by lazy {
        AlbumAdapter {
            val direction =
                AlbumsListFragmentDirections.actionAlbumsListFragmentToSongsListFragment(it)
            findNavController().navigate(direction)
        }
    }
    @Inject
    lateinit var viewModel: TunesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: injecting fragment")

        (activity?.application as? TunesApp)?.appComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.albums_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albums_list.layoutManager = LinearLayoutManager(albums_list.context)
        albums_list.adapter = albumAdapter

        arguments?.getString("searchRequest")?.let { term ->
            viewModel.search(term).observe(this, Observer<RequestResult<List<Album>>> { result ->
                when (val state = result?.state) {
                    State.SUCCESS -> {
                        if (result.result != null && result.result.isNotEmpty()) {
                            albumAdapter.updateData(result.result)
                            progress_bar.visibility = View.GONE
                        } else {
                            showText(R.string.empty_result)
                        }
                    }
                    State.LOADING -> progress_bar.visibility = View.VISIBLE
                    State.FAILURE -> showText(R.string.network_failure)
                    else -> Log.w(TAG, "Unknown state: $state")
                }
            })
        }
    }

    private fun showText(@StringRes textId: Int) {
        val destination =
            AlbumsListFragmentDirections.actionAlbumsListFragmentToEmptyScreenFragment(
                getString(textId)
            )
        findNavController().navigate(destination)
    }
}
