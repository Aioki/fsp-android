package ru.aioki.myapplication.ui.loginStage.register

import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.databinding.FragmentRegister2Binding
import ru.aioki.myapplication.ui.mainStage.MainActivity
import ru.aioki.myapplication.utils.Resource

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class RegisterFragment2 : BaseFragment<FragmentRegister2Binding>() {

    override fun getLayoutID(): Int = R.layout.fragment_register2

    private val viewModel: RegisterViewModel by activityViewModels()


    override fun setUpViews() {
        super.setUpViews()
        binding.vm = viewModel

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            R.id.item,
            listOf("Спортсмен", "Представитель региональной федерации", "Партнер")
        )
        (binding.tfType.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    }

    override fun observeView() {
        super.observeView()
        binding.btnLogin.setOnClickListener {
            viewModel.register()
        }
    }

    override fun observeData() {
        super.observeData()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.statusStep2.collect {
                binding.btnLogin.isEnabled = it.status != Resource.Status.LOADING
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
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