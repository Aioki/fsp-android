package ru.aioki.myapplication.ui.loginStage.auth

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.databinding.FragmentLoginBinding
import ru.aioki.myapplication.ui.mainStage.MainActivity
import ru.aioki.myapplication.utils.Resource.Status.*
import ru.aioki.myapplication.utils.navigateSafely

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun getLayoutID() = R.layout.fragment_login

    override fun setUpViews() {
        super.setUpViews()
        binding.vm = viewModel
    }

    override fun observeView() {
        super.observeView()
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }
        binding.btnRegistration.setOnClickListener {
            findNavController().navigateSafely(LoginFragmentDirections.actionFirstFragmentToSecondFragment())
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.status.observe(viewLifecycleOwner) {
            binding.btnLogin.isEnabled = it.status != LOADING
            when (it.status) {
                SUCCESS -> {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
                LOADING -> {

                }
                ERROR, NETWORK_ERROR -> {
                    logNotify(it.errorBody?.message)
                }
            }
        }
    }

}