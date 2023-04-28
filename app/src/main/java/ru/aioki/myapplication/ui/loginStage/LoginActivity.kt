package ru.aioki.myapplication.ui.loginStage

import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseActivity
import ru.aioki.myapplication.data.repository.LoginRepository
import ru.aioki.myapplication.databinding.ActivityLoginBinding
import ru.aioki.myapplication.ui.mainStage.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var loginRepository: LoginRepository

    override fun getFragmentContainer() = R.id.nav_host_fragment_content_login

    override fun configureActivity() {
        super.configureActivity()
        if (loginRepository.isAuth()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}