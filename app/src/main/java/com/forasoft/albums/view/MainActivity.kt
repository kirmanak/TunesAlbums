package com.forasoft.albums.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.forasoft.albums.R

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val args = bundleOf(TEXT_ARGUMENT to getString(R.string.search_prompt))
        getNavController().setGraph(getNavController().graph, args)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as? SearchManager
        val searchView = menu?.findItem(R.id.app_bar_search)?.actionView as? SearchView

        if (searchManager != null && searchView != null)
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun onSearchRequest(text: String?) {
        Log.d(TAG, "onSearchRequest: $text")
        if (text == null || text.isBlank())
            return

        val bundle = bundleOf("searchRequest" to text)
        getNavController().navigate(R.id.albumsListFragment, bundle)
    }

    private fun handleIntent(intent: Intent?) {
        val action = intent?.action ?: return
        when (action) {
            Intent.ACTION_SEARCH -> onSearchRequest(intent.getStringExtra(SearchManager.QUERY))
        }
    }

    private fun getNavController() = findNavController(R.id.main_nav_host)
}
