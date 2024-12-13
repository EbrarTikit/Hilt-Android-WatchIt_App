package com.example.watchit.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.watchit.R
import com.example.watchit.common.UIState
import com.example.watchit.databinding.FragmentLoginBinding
import com.example.watchit.ui.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupObservers()
        setupTextWatchers()
    }

    private fun setupClickListeners(){
        binding.login.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                binding.loading.visibility = View.VISIBLE
                binding.login.text = ""  // Clear button text while loading
                viewModel.login(username, password)
            }
        }
    }

    private fun setupTextWatchers(){
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.login.isEnabled = binding.username.text!!.isNotEmpty() && binding.password.text!!.isNotEmpty()
            }
        }

        binding.username.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    private fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect(){ state ->
                when(state){
                    is UIState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.login.isEnabled = false
                    }
                    is UIState.Success ->{
                        binding.loading.visibility = View.GONE
                        binding.login.isEnabled = true
                        if (state.data.success){
                            findNavController().navigate(R.id.action_loginFragment_to_movieListFragment)
                        }

                    }
                    is UIState.Failure ->{
                        binding.loading.visibility = View.GONE
                        binding.login.isEnabled = true
                        Toast.makeText(context, "Login failed: ${state.error.message}", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun onDestryView(){
        super.onDestroyView()
        _binding = null
    }
}