package com.example.watchit.ui.movieDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.watchit.R
import com.example.watchit.common.loadImage
import com.example.watchit.databinding.FragmentDetailBinding
import com.example.watchit.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.getMovieDetail(movieId = args.movieId)
        observeEvents()

        return binding.root
    }

    private fun observeEvents() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.error.text=it
            binding.error.isVisible=true
        }

        viewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            Log.d("ImageLoading", "Movie: $movie")
            Log.d("ImageLoading", "Poster path: ${movie.posterPath}")
            Log.d("ImageLoading", "Backdrop path: ${movie.backdropPath}")
            binding.tvMovName.text = movie.title
            binding.tvImbd.text = movie.voteAverage.toString()
            binding.tvGenres.text = movie.genres.take(3).joinToString(" ")
            binding.tvOverview.text=movie.overview
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}