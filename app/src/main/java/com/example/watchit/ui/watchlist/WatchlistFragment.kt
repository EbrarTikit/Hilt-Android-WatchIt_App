package com.example.watchit.ui.watchlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchit.common.UIState
import com.example.watchit.databinding.FragmentWatchlistBinding
import com.example.watchit.ui.movieList.adapter.PopularMovieAdapter
import com.example.watchit.ui.watchlist.viewmodel.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WatchlistViewModel>()
    private val adapter = PopularMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        setupObservers()
        listWatchlist()
    }

    private fun setupRecyclerView() {
        binding.rvWatchlist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WatchlistFragment.adapter
        }
    }

    private fun setupClickListeners() {
        adapter.setOnMovieClickListener { movieId ->
            findNavController().navigate(
                WatchlistFragmentDirections.actionWatchListFragmentToDetailFragment(movieId)
            )
        }
    }

    private fun listWatchlist() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val sessionId = prefs.getString("SESSION_ID", "") ?: ""
        val accountId = prefs.getInt("ACCOUNT_ID", -1)

        if (sessionId.isNotEmpty() && accountId != -1) {
            viewModel.getWatchList(accountId, sessionId)
        } else {
            Toast.makeText(requireContext(), "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchlistMovies.collect { state ->
                when (state) {
                    is UIState.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.error.visibility = View.GONE
                        if (state.data.results.isEmpty()) {
                            binding.error.visibility = View.VISIBLE
                            binding.error.text = "Watchlist'iniz boş"
                            binding.rvWatchlist.visibility = View.GONE
                        } else {
                            binding.rvWatchlist.visibility = View.VISIBLE
                            adapter.updateList(state.data.results)
                        }
                    }
                    is UIState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.rvWatchlist.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is UIState.Failure -> {
                        binding.progress.visibility = View.GONE
                        binding.rvWatchlist.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
                        binding.error.text = state.error.message
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}