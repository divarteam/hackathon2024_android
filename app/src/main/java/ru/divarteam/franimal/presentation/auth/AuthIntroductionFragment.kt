package ru.divarteam.franimal.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.FragmentAuthIntroductionBinding

@AndroidEntryPoint
class AuthIntroductionFragment : Fragment() {

    private lateinit var binding: FragmentAuthIntroductionBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAuthIntroductionBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginViaEmail.setOnClickListener {
            findNavController().navigate(
                AuthIntroductionFragmentDirections.actionAuthIntroductionFragmentToAuthInputEmailBottomSheet()
            )
        }

        binding.loginViaVk.setOnClickListener {
            findNavController().navigate(
                AuthIntroductionFragmentDirections.actionAuthIntroductionFragmentToAuthVkLoginBottomSheet()
            )
        }

        binding.loginViaTelegram.setOnClickListener {
            findNavController().navigate(
                AuthIntroductionFragmentDirections.actionAuthIntroductionFragmentToAuthTgLoginBottomSheet()
            )
        }
    }
}