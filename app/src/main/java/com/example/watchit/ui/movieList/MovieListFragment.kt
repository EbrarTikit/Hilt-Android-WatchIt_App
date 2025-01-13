package com.example.watchit.ui.movieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.watchit.R
import com.example.watchit.common.UIState

import com.example.watchit.databinding.FragmentMovieListBinding
import com.example.watchit.ui.movieList.adapter.ListAdapter
import com.example.watchit.ui.movieList.adapter.MovieCardAdapter
import com.example.watchit.ui.movieList.adapter.MovieClickListener
import com.example.watchit.ui.movieList.viewmodel.MainViewModel
import com.example.watchit.ui.trends.TrendsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: MovieCardAdapter
    private lateinit var adapter2: ListAdapter

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
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = MovieCardAdapter().apply {
            setOnMovieClickListener { movieId ->
                val action = MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(movieId)
                findNavController().navigate(action)
            }
        }

        binding.rv.apply {
            this.adapter = this@MovieListFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        adapter2 = ListAdapter(emptyList(),object : MovieClickListener{
            override fun onMovieClicked(movieId: Int?) {
                movieId?.let{
                    val action = MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(movieId)
                    findNavController().navigate(action)
                }
            }
        })
        binding.rvUpcoming.adapter = adapter2
        binding.rvUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mainItem.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.rv.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is UIState.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                        binding.error.visibility = View.GONE
                        adapter.updateList(state.data.results)
                    }
                    is UIState.Failure -> {
                        binding.progress.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
                        binding.rv.visibility = View.GONE
                        binding.error.text = "Error: ${state.error.message}"
                    }

                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.upcoming.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.rvUpcoming.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is UIState.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.rvUpcoming.visibility = View.VISIBLE
                        binding.error.visibility = View.GONE
                        adapter2.updateList(state.data.results)
                    }
                    is UIState.Failure -> {
                        binding.progress.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
                        binding.rvUpcoming.visibility = View.GONE
                        binding.error.text = "Error: ${state.error.message}"
                    }

                    else -> {}
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}