package com.example.watchit.ui.movieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.watchit.R
import com.example.watchit.common.UIState

import com.example.watchit.databinding.FragmentMovieListBinding
import com.example.watchit.ui.movieList.adapter.ListAdapter
import com.example.watchit.ui.movieList.adapter.MovieClickListener
import com.example.watchit.ui.movieList.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: ListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater,container,false)

        setUpObservers()
        setUpRecyclerV()

        viewModel.fetchMovies(page = 1)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setUpRecyclerV() {
        binding.rv.layoutManager = LinearLayoutManager(context)
        adapter = ListAdapter(emptyList(), object : MovieClickListener {
            override fun onMovieClicked(movieId: Int?) {
                movieId?.let {
                    val action = MovieListFragmentDirections.actionMovieListFragmentToDetailFragment(it)
                    findNavController().navigate(action)
                }
            }
        })
        binding.rv.adapter = adapter
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            viewModel.mainItem.collect { state ->
                when (state) {
                    is UIState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.error.visibility = View.GONE
                        binding.rv.visibility = View.GONE
                    }
                    is UIState.Success -> {
                        binding.progress.visibility = View.GONE
                        binding.error.visibility = View.GONE
                        binding.rv.visibility = View.VISIBLE
                        adapter.updateList(state.data.results)
                    }
                    is UIState.Failure -> {
                        binding.progress.visibility = View.GONE
                        binding.error.visibility = View.VISIBLE
                        binding.rv.visibility = View.GONE
                        binding.error.text = "Error:${state.error.message}"
                    }

                    else -> {}
                }
            }
        }
    }

}