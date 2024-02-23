package ru.divarteam.franimal.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.BottomSheetInputEmailBinding

@AndroidEntryPoint
class AuthInputEmailBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetInputEmailBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = BottomSheetInputEmailBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.email.editText?.doAfterTextChanged {
            if (it != null)
                binding.email.error =
                    if (it.isNotEmpty() && it.contains("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$".toRegex())
                            .not()
                    ) "Неправильный email"
                    else ""
        }

        binding.sendCode.setOnClickListener {
            findNavController().navigate(
                AuthInputEmailBottomSheetDirections.actionAuthInputEmailBottomSheetToAuthInsertCodeFragment(
                    binding.email.editText?.text.toString()
                )
            )
        }
    }
}