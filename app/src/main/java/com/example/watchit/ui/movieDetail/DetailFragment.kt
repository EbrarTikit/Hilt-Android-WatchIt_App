package com.example.watchit.ui.movieDetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.watchit.R
import com.example.watchit.common.UIState
import com.example.watchit.common.loadImage
import com.example.watchit.databinding.FragmentDetailBinding
import com.example.watchit.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieDetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // İlk yükleme
        viewModel.getMovieDetail(movieId = args.movieId)
        
        binding.btnAddWatchlist.apply {
            isEnabled = true
            setOnClickListener {
                Log.d("Watchlist", "Button clicked") // Debug için
                val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val sessionId = prefs.getString("SESSION_ID", "") ?: ""
                val accountId = prefs.getInt("ACCOUNT_ID", -1)

                if (sessionId.isNotEmpty() && accountId != -1) {
                    viewModel.addToWatchList(
                        accountId = accountId,
                        mediaId = args.movieId,
                        isMovie = true,
                        sessionId = sessionId
                    )
                } else {
                    Toast.makeText(requireContext(), "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        observeEvents()
    }

    override fun onResume() {
        super.onResume()
        binding.btnAddWatchlist.apply {
            isEnabled = true
            alpha = 1f
            visibility = View.VISIBLE
            bringToFront()
            setOnClickListener {
                Log.d("Watchlist", "Button clicked") // Debug için
                val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val sessionId = prefs.getString("SESSION_ID", "") ?: ""
                val accountId = prefs.getInt("ACCOUNT_ID", -1)

                if (sessionId.isNotEmpty() && accountId != -1) {
                    viewModel.addToWatchList(
                        accountId = accountId,
                        mediaId = args.movieId,
                        isMovie = true,
                        sessionId = sessionId
                    )
                } else {
                    Toast.makeText(requireContext(), "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progress.isVisible = isLoading
            binding.error.isVisible = false
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                binding.error.text = errorMessage
                binding.error.isVisible = true
                binding.progress.isVisible = false
            }
        }

        viewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            binding.error.isVisible = false
            binding.progress.isVisible = false
            
            Log.d("MovieDetail", "Received movie: $movie")
            binding.tvMovName.text = movie.title
            binding.tvImbd.text = movie.voteAverage.toString()
            binding.tvGenres.text = movie.genres.take(3).joinToString(" ")
            binding.tvOverview.text = movie.overview
            binding.imgPoster.loadImage(movie.posterPath)
            binding.imgBackdrop.loadImage(movie.backdropPath)

            (requireActivity() as MainActivity).supportActionBar?.title = movie.title
        }

        viewModel.genresText.observe(viewLifecycleOwner) { genresText ->
            binding.tvGenres.text = genresText
        }

        /*viewModel.releaseYearText.observe(viewLifecycleOwner) { releaseYearText ->
            binding.tvYear.text = releaseYearText
        }*/

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.watchlistState.collect { state ->
                when (state) {
                    is UIState.Success -> {
                        binding.btnAddWatchlist.isEnabled = true
                        Toast.makeText(requireContext(), "Film watchlist'e eklendi", Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Failure -> {
                        binding.btnAddWatchlist.isEnabled = true
                        Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
                    }
                    is UIState.Loading -> {
                        binding.btnAddWatchlist.isEnabled = false
                    }
                    else -> Unit
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}