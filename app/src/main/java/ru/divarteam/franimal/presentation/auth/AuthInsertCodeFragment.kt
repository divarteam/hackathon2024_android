package ru.divarteam.franimal.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.FragmentAuthInsertCodeBinding
import ru.divarteam.franimal.presentation.main.MainActivity
import ru.divarteam.franimal.util.makeCustomOTP
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class AuthInsertCodeFragment : Fragment() {

    private lateinit var binding: FragmentAuthInsertCodeBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private val args: AuthInsertCodeFragmentArgs by navArgs()

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
    ) = FragmentAuthInsertCodeBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.title.setText("Вы получите код на почту ${args.email}")

        listOf(
            binding.firstSymbol.editText,
            binding.secondSymbol.editText,
            binding.thirdSymbol.editText,
            binding.fourthSymbol.editText
        ).filter { it != null }.map { it!! }.makeCustomOTP()

        authViewModel.timerSecondsRemain.observe(viewLifecycleOwner) {
            if (it > 0)
                binding.sendAgain.apply {
                    setText("Не получили письмо? Повторная отправка будет доступна через $it секунд")
                    if (hasOnClickListeners())
                        setOnClickListener(null)
                }
            else
                binding.sendAgain.apply {
                    setText(
                        SpannableString("Нажмите, чтобы отправить письмо повторно")
                            .apply {
                                setSpan(UnderlineSpan(), 0, length, 0)
                            }
                    )
                    setOnClickListener {
                        authViewModel.sendCode(args.email, doOnSuccess = {
                            Toast.makeText(
                                context,
                                "Код успешно отправлен",
                                Toast.LENGTH_SHORT
                            ).show()
                        }, doOnError = {
                            onError(it)
                        })
                    }
                }
        }

        authViewModel.sendCode(args.email, doOnSuccess = {
            Toast.makeText(
                context,
                "Код успешно отправлен",
                Toast.LENGTH_SHORT
            ).show()
        }, doOnError = {
            onError(it)
        })

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.checkCode.setOnClickListener {
            authViewModel.checkCode(
                args.email,
                "${
                    binding.firstSymbol.editText?.text?.first().toString()
                }${
                    binding.secondSymbol.editText?.text?.first().toString()
                }${
                    binding.thirdSymbol.editText?.text?.first().toString()
                }${
                    binding.fourthSymbol.editText?.text?.first().toString()
                }",
                doOnSuccess = {
                    authViewModel.updateUserData(
                        newUserId = it?.intId ?: 0,
                        newUserToken = it?.currentToken ?: ""
                    )
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                },
                doOnError = {
                    onError(it)
                }
            )
        }
    }

    fun onError(errorText: String) {
        Toast.makeText(
            context,
            "Error: $errorText",
            Toast.LENGTH_SHORT
        ).show()
    }
}