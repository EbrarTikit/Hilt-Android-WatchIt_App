package com.example.watchit.ui.trends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchit.R
import com.example.watchit.common.UIState
import com.example.watchit.databinding.FragmentTrendsBinding
import com.example.watchit.ui.movieList.adapter.ListAdapter
import com.example.watchit.ui.movieList.adapter.MovieClickListener
import com.example.watchit.ui.movieList.adapter.PopularMovieAdapter
import com.example.watchit.ui.movieList.viewmodel.MainViewModel
import com.example.watchit.ui.trends.viewmodel.TrendsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrendsFragment : Fragment() {

    //binding, viewModel, adapter
    private var _binding: FragmentTrendsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TrendsViewModel>()
    private val adapter = PopularMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        binding.rvTrend.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TrendsFragment.adapter
        }
    }

    private fun setupClickListeners() {
        adapter.setOnMovieClickListener { movieId ->
            findNavController().navigate(
                TrendsFragmentDirections.actionTrendsFragmentToDetailFragment(movieId)
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.trendMovie.collect { state ->
                when(state) {
                    is UIState.Success -> {
                        adapter.updateList(state.data.results)
                        binding.rvTrend.visibility = View.VISIBLE
                        binding.progress.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is UIState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.rvTrend.visibility = View.GONE
                        binding.error.visibility = View.GONE
                    }
                    is UIState.Failure -> {
                        binding.error.visibility = View.VISIBLE
                        binding.rvTrend.visibility = View.GONE
                        binding.progress.visibility = View.GONE
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