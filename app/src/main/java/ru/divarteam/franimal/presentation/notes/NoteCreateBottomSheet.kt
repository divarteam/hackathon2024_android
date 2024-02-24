package ru.divarteam.franimal.presentation.notes

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.BottomSheetCreatePostBinding

@AndroidEntryPoint
class NoteCreateBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetCreatePostBinding
    private val notesViewModel: NotesViewModel by activityViewModels()

    var photoUri: Uri? = null
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            photoUri = uri
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                Glide.with(binding.picturePreview)
                    .load(uri)
                    .into(binding.picturePreview)
                binding.picturePreview.visibility = View.VISIBLE
            } else {
                binding.picturePreview.visibility = View.GONE
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = BottomSheetCreatePostBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.addPicture.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.sendNote.setOnClickListener {
            notesViewModel.createNote(
                binding.text.editText?.text.toString(),
                requireContext(),
                photoUri,
                doOnError = {
                    onError(it)
                }, doOnSuccess = {
                    notesViewModel.loadNotesList {
                        onError(it)
                    }
                    dismiss()
                }
            )
        }
    }

    fun onError(errorText: String) {
        Toast.makeText(
            activity,
            "Error: $errorText",
            Toast.LENGTH_SHORT
        ).show()
    }


}