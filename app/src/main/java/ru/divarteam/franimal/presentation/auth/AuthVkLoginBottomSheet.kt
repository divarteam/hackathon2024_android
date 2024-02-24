package ru.divarteam.franimal.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.divarteam.franimal.databinding.BottomSheetVkLoginBinding
import ru.divarteam.franimal.presentation.main.MainActivity


class AuthVkLoginBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetVkLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = BottomSheetVkLoginBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vk.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/franimal"))
            startActivity(browserIntent)
        }

        binding.sendCode.setOnClickListener {
            authViewModel.authViaVk(
                binding.code.editText?.text.toString(),
                doOnError = {

                }, doOnSuccess = {
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                }
            )
        }
    }
}