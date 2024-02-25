package ru.divarteam.franimal.presentation.notes

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
import ru.divarteam.franimal.databinding.FragmentNotesBinding
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private val notesViewModel: NotesViewModel by activityViewModels()

    @Inject
    lateinit var preferenceRepository: PreferenceRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNotesBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notesViewModel.notesList.observe(viewLifecycleOwner) {
            binding.notesRecycler.withModels {
                it.forEachIndexed { index, noteResponse ->
                    note {
                        id(index)
                        noteResponse(noteResponse)
                        userResponse(noteResponse.user)
                        navigateToProfile {
                            findNavController().navigate(
                                NotesFragmentDirections.actionNotesFragmentToProfileOtherFragment()
                                    .setUserId(noteResponse.userIntId ?: -1)
                            )
                        }
                        navigateToNote {
                            findNavController().navigate(
                                NotesFragmentDirections.actionNotesFragmentToNoteFragment(
                                    it.intId ?: -1
                                )
                            )
                        }
                        likeNote {
                            if (it.intId != null)
                                notesViewModel.likeNoteById(it.intId) {
                                    onError(it)
                                }
                        }
                    }
                }
            }
        }

        notesViewModel.loadNotesList {
            onError(it)
        }

        binding.addNote.setOnClickListener {
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToNoteCreateBottomSheet()
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