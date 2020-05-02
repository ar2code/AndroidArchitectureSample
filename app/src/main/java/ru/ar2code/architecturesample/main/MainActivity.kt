package ru.ar2code.architecturesample.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.ar2code.architecturesample.R
import ru.ar2code.business_layer_abstract.models.LoginParameters
import ru.ar2code.common.*

class MainActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindLoginView()
        bindProgressBar()

        observeAuthorization()
        observeRefreshView()
    }

    private fun bindProgressBar() {
        progressBar.bindVisibleWithCommandIsExecuting(this, vm.loginCommand)
    }

    private fun bindLoginView() {
        loginEdit.bindAfterTextChangedWithCommand(vm.loginValidityCommand)
        passwordEdit.bindAfterTextChangedWithCommand(vm.passwordValidityCommand)

        loginButton.bindCommand(this, vm.loginCommand) {
            LoginParameters(loginEdit.text.toString(), passwordEdit.text.toString())
        }
    }

    private fun observeAuthorization() {
        vm.authorizationSuccessLive.observe(this, Observer {
            showAuthorizeSuccessMsg(it?.data)
        })
        vm.authorizationErrorLive.observe(this, Observer {
            showAuthorizeErrorMsg()
        })
    }

    private fun observeRefreshView() {
        vm.refreshLoginViewLive.observe(this, Observer {
            hideAuthorizeErrorMsg()
        })
    }

    private fun showAuthorizeErrorMsg() {
        loginErrorMsg.isInvisible = false
    }

    private fun hideAuthorizeErrorMsg() {
        loginErrorMsg.isInvisible = true
    }

    private fun showAuthorizeSuccessMsg(name : String?) {
        val msg = getString( R.string.success_login, name)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
