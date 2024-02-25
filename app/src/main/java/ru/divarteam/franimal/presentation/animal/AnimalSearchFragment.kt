package ru.divarteam.franimal.presentation.animal

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
import ru.divarteam.franimal.databinding.FragmentAnimalSearchBinding

@AndroidEntryPoint
class AnimalSearchFragment : Fragment() {

    private lateinit var binding: FragmentAnimalSearchBinding
    private val animalSearchViewModel: AnimalSearchViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAnimalSearchBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animalSearchViewModel.setupQuerySubject {
            onError(it)
        }

        binding.search.editText?.doAfterTextChanged {
            animalSearchViewModel.operateQuery(it.toString())
        }

        animalSearchViewModel.usersList.observe(viewLifecycleOwner) {
            binding.usersRecycler.withModels {
                it.forEachIndexed { index, userResponse ->
                    userAnimals {
                        id(index)
                        userResponse(userResponse)
                        navigateToAnimal { }
                        navigateToOwner {
                            findNavController().navigate(
                                AnimalSearchFragmentDirections.actionAnimalSearchFragmentToProfileOtherFragment().apply {
                                    userId = userResponse.intId ?: -1
                                }
                            )
                        }
                    }
                }
            }
        }

        animalSearchViewModel.operateQuery(" ")
    }

    fun onError(errorText: String) {
        Toast.makeText(
            context,
            "Error: $errorText",
            Toast.LENGTH_SHORT
        ).show()
    }
}