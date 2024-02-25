package ru.divarteam.franimal.presentation.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.FragmentCardSearchBinding
import ru.divarteam.franimal.presentation.animal.userAnimals

@AndroidEntryPoint
class CardSearchFragment : Fragment() {

    private lateinit var binding: FragmentCardSearchBinding
    private val cardSearchViewModel: CardSearchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCardSearchBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cardSearchViewModel.setupQuerySubject {
            onError(it)
        }

        binding.search.editText?.doAfterTextChanged {
            cardSearchViewModel.operateQuery(it.toString())
        }

        cardSearchViewModel.cardsList.observe(viewLifecycleOwner) {
            binding.cardsRecycler.withModels {
                it.forEachIndexed { index, cardResponse ->
                    card {
                        id(index)
                        cardResponse(cardResponse)
                        navigateToOwner {
                            findNavController().navigate(
                                CardSearchFragmentDirections.actionCardSearchFragmentToProfileOtherFragment().apply {
                                    userId = cardResponse.userIntId ?: -1
                                }
                            )
                        }
                    }
                }
            }
        }

        cardSearchViewModel.operateQuery(" ")
    }

    fun onError(errorText: String) {
        Toast.makeText(
            context,
            "Error: $errorText",
            Toast.LENGTH_SHORT
        ).show()
    }
}