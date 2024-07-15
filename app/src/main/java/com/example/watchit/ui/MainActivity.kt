package com.example.watchit.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watchit.R
import com.example.watchit.common.UIState
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.Result
import com.example.watchit.data.network.ApiInterface
import com.example.watchit.data.repository.AppRepositoryImpl
import com.example.watchit.databinding.ActivityMainBinding
import com.example.watchit.domain.repository.AppRepository
import com.example.watchit.ui.adapter.ListAdapter
import com.example.watchit.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_components_ViewModelComponent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpRecyclerV()
        setUpObservers()

        viewModel.fetchMovies(1)
    }

    private fun setUpRecyclerV() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = ListAdapter(emptyList())
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
                        binding.error.text = "Error: ${state.error.message}"
                    }
                }
            }
        }
    }

}

