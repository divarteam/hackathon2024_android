package ru.divarteam.franimal.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.divarteam.franimal.databinding.BottomSheetTgLoginBinding
import ru.divarteam.franimal.databinding.BottomSheetVkLoginBinding
import ru.divarteam.franimal.presentation.main.MainActivity

class AuthTgLoginBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetTgLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = BottomSheetTgLoginBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tg.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/franimalbot"))
            startActivity(browserIntent)
        }

        binding.sendCode.setOnClickListener {
            authViewModel.authViaTg(
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