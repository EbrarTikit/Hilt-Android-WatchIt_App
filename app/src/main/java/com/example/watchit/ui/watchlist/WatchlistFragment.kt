package com.example.watchit.ui.watchlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchit.R
import com.example.watchit.common.UIState
import com.example.watchit.databinding.FragmentWatchlistBinding
import com.example.watchit.ui.movieList.adapter.ListAdapter
import com.example.watchit.ui.movieList.adapter.MovieClickListener
import com.example.watchit.ui.watchlist.viewmodel.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchlistFragment : Fragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListAdapter
    private val viewModel by viewModels<WatchlistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listWatchlist()
        setupRecyclerView()
        setupObservers()
    }

    private fun listWatchlist() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val sessionId = prefs.getString("SESSION_ID", "") ?: ""
        val accountId = prefs.getInt("ACCOUNT_ID", -1)

        if (sessionId.isNotEmpty() && accountId != -1) {
            viewModel.getWatchList(
                accountId = accountId,
                sessionId = sessionId
            )
        } else {
            Toast.makeText(requireContext(), "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchlist.collect{
                when(it){
                    is UIState.Loading -> {
                        binding.progress.visibility=View.VISIBLE
                        binding.rvWatchlist.visibility=View.GONE
                        binding.error.visibility=View.GONE
                    }
                    is UIState.Success -> {
                        binding.progress.visibility=View.GONE
                        binding.error.visibility=View.GONE
                        binding.rvWatchlist.visibility=View.VISIBLE
                        adapter.updateList(it.data.results)
                    }
                    is UIState.Failure -> {
                        binding.progress.visibility=View.GONE
                        binding.rvWatchlist.visibility=View.GONE
                        binding.error.visibility=View.VISIBLE
                        binding.error.text = "Error: ${it.error.message}"
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ListAdapter(emptyList(), object: MovieClickListener{
            override fun onMovieClicked(movieId: Int?) {
                movieId?.let {
                    val action = WatchlistFragmentDirections.actionWatchListFragmentToDetailFragment(it)
                    findNavController().navigate(action)
                }
            }

        })
        binding.rvWatchlist.adapter = adapter
        binding.rvWatchlist.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        listWatchlist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}