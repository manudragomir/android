package com.stud.ubbcluj.manu.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stud.ubbcluj.manu.R
import com.stud.ubbcluj.manu.auth.data.AuthRepository
import com.stud.ubbcluj.manu.utils.MyResult
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setupLoginForm()
        btn_go_offline.setOnClickListener{
            AuthRepository.isOffline = true
            findNavController().navigate(R.id.PlantListFragment)
        }
    }

    private fun setupLoginForm() {
        viewModel.loginResult.observe(viewLifecycleOwner, { loginResult ->
            Log.v(TAG, "s-o schimbat tokenul");
            loading.visibility = View.GONE
            if (loginResult is MyResult.Success<*>) {
                findNavController().navigate(R.id.PlantListFragment)
            } else if (loginResult is MyResult.Error) {
                error_text.text = "Login error ${loginResult.exception.message}"
                error_text.visibility = View.VISIBLE
            }
        })
        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            error_text.visibility = View.GONE
            viewModel.login(username.text.toString(), password.text.toString())
        }
    }
}