package ru.aioki.myapplication.ui.loginStage.register

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.databinding.FragmentRegistrationBinding
import ru.aioki.myapplication.utils.Resource


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun getLayoutID(): Int = R.layout.fragment_registration

    override fun setUpViews() {
        super.setUpViews()
        binding.vm = viewModel
    }

    override fun observeView() {
        super.observeView()
        binding.btnLogin.setOnClickListener {
            viewModel.checkStep1()
        }
    }

    override fun observeData() {
        super.observeData()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.statusStep1.collect {
                binding.btnLogin.isEnabled = it.status != Resource.Status.LOADING
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        findNavController().navigate(RegisterFragmentDirections.actionSecondFragmentToRegisterFragment2())
                    }
                    Resource.Status.LOADING -> {

                    }
                    Resource.Status.ERROR, Resource.Status.NETWORK_ERROR -> {
                        logNotify(it.errorBody?.message)
                    }
                }
            }
        }

    }

}