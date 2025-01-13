package com.example.watchit.ui.movieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchit.common.UIState
import com.example.watchit.databinding.FragmentMovieListBinding
import com.example.watchit.ui.movieList.adapter.MovieCardAdapter
import com.example.watchit.ui.movieList.adapter.PopularMovieAdapter
import com.example.watchit.ui.movieList.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    
    private val mainAdapter = MovieCardAdapter()
    private val upcomingAdapter = PopularMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupClickListeners()
        observeData()
    }

    private fun setupRecyclerViews() {
        binding.trendingRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mainAdapter
        }

        binding.popularRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingAdapter
        }
    }

    private fun setupClickListeners() {
        mainAdapter.setOnMovieClickListener{
            findNavController().navigate(
                MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(it)
            )
        }

        upcomingAdapter.setOnMovieClickListener { movieId ->
            findNavController().navigate(
                MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(movieId)
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mainItem.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        binding.trendingProgress.visibility = View.VISIBLE
                        binding.trendingError.visibility = View.GONE
                    }
                    is UIState.Success -> {
                        binding.trendingProgress.visibility = View.GONE
                        binding.trendingError.visibility = View.GONE
                        mainAdapter.updateList(state.data.results)
                    }
                    is UIState.Failure -> {
                        binding.trendingProgress.visibility = View.GONE
                        binding.trendingError.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.upcoming.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        binding.popularProgress.visibility = View.VISIBLE
                        binding.popularError.visibility = View.GONE
                    }
                    is UIState.Success -> {
                        binding.popularProgress.visibility = View.GONE
                        binding.popularError.visibility = View.GONE
                        upcomingAdapter.updateList(state.data.results)
                    }
                    is UIState.Failure -> {
                        binding.popularProgress.visibility = View.GONE
                        binding.popularError.visibility = View.VISIBLE
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