package ru.divarteam.franimal.presentation.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.R
import ru.divarteam.franimal.databinding.FragmentNoteBinding
import ru.divarteam.franimal.util.attrColor
import ru.divarteam.franimal.util.date
import ru.divarteam.franimal.util.russianString

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private val args: NoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentNoteBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.commentsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        noteViewModel.loading.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it)
                View.VISIBLE
            else
                View.GONE
        }

        noteViewModel.updateNote(args.noteIntId, doOnError = {
            onError(it)
        }, doOnSuccess = {

        })

        binding.appbar.setStartButtonOnClickListener {
            findNavController().popBackStack()
        }

        binding.send.setOnClickListener {
            noteViewModel.createComment(
                noteIntId = noteViewModel.noteResponse.value?.intId ?: -1,
                commentText = binding.commentText.editText?.text.toString(),
                doOnError = {
                    onError(it)
                },
                doOnSuccess = {
                    binding.commentText.editText?.setText("")
                }
            )
        }

        noteViewModel.noteResponse.observe(viewLifecycleOwner) {
            if (it == null)
                return@observe

            binding.avatar.setOnClickListener { _ ->
                findNavController().navigate(
                    NoteFragmentDirections.actionNoteFragmentToProfileOtherFragment().apply {
                        userId = it.intId ?: -1
                    }
                )
            }

            binding.datetime.setText(it.creationDate?.date?.russianString)
            binding.fullname.setText(it.user?.fullname)
            binding.text.setText(it.text)
            binding.like.setText(it.likeAmount.toString())
            binding.comment.setText(it.comments?.size.toString())

            binding.likeCard.setOnClickListener { _ ->
                if (it.intId != null)
                    noteViewModel.likeNoteById(it.intId) {
                        onError(it)
                    }
            }

            binding.picture.visibility =
                if (it.photoFilename != null)
                    View.VISIBLE
                else
                    View.GONE

            Glide.with(binding.picture)
                .load("https://api.hackathon2024.divarteam.ru/file_storage/${it.photoFilename}")
                .placeholder(
                    ContextCompat.getDrawable(
                        binding.avatar.context,
                        R.drawable.ic_launcher_background
                    )?.apply {
                        setTint(
                            binding.avatar.context.attrColor(com.google.android.material.R.attr.colorSurfaceContainerHighest)
                        )
                    }
                )
                .into(binding.picture)

            Glide.with(binding.avatar)
                .load("https://api.hackathon2024.divarteam.ru/file_storage/${it.user?.photoFilename}")
                .placeholder(
                    ContextCompat.getDrawable(
                        binding.avatar.context,
                        R.drawable.ic_launcher_background
                    )?.apply {
                        setTint(
                            binding.avatar.context.attrColor(com.google.android.material.R.attr.colorSurfaceContainerHighest)
                        )
                    }
                )
                .error(
                    Glide.with(binding.avatar)
                        .load("https://i.imgur.com/Kr3SNSO.png")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)

            binding.commentsRecycler.withModels {
                it.comments?.forEachIndexed { index, commentResponse ->
                    comment {
                        id(index)
                        commentResponse(commentResponse)
                        removeComment {
                            noteViewModel.removeComment(
                                commentResponse.noteIntId ?: -1,
                                it.intId ?: -1,
                                doOnError = {
                                    onError(it)
                                }, doOnSuccess = {

                                }
                            )
                        }
                    }
                }
            }
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