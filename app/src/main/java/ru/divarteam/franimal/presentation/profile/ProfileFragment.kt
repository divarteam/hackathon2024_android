package ru.divarteam.franimal.presentation.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.R
import ru.divarteam.franimal.databinding.FragmentProfileBinding
import ru.divarteam.franimal.presentation.animal.animal
import ru.divarteam.franimal.presentation.auth.AuthActivity
import ru.divarteam.franimal.presentation.card.card
import ru.divarteam.franimal.presentation.notes.note
import ru.divarteam.franimal.util.attrColor

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProfileBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.myPetsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.myCardsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.myNotesRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.sayThankYou.visibility = View.GONE

        profileViewModel.userResponse.observe(viewLifecycleOwner) {
            binding.loading.visibility =
                if (it.intId == null)
                    View.VISIBLE
                else
                    View.GONE

            binding.fullname.text = it.fullname ?: "Нет ФИО"
            binding.coins.setText((it.points ?: 0).toString().prependIndent("Очков: "))
            binding.age.text =
                (it.age ?: "Неизвестен").toString().prependIndent("Возраст: ")

            Glide.with(binding.avatar)
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
                .error(
                    Glide.with(binding.avatar)
                        .load("https://i.imgur.com/Kr3SNSO.png")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.avatar)
            binding.mail.text = (it.mail ?: "Нет почты").prependIndent("Почта: ")
            binding.city.text = (it.city ?: "Не указан").prependIndent("Город: ")
            binding.phone.text = (it.phone ?: "Пусто").prependIndent("Телефон: ")

            binding.rankText.text = (it.rank ?: "Нет ранга").prependIndent("Ранг: ")

            val currentPoints = it.points ?: 0
            val neededPoints = it.neededPoints ?: 0
            val sumPoints = currentPoints + neededPoints

            binding.rankProgress.progress =
                if (neededPoints == 0)
                    100
                else
                    currentPoints / sumPoints * 100

            binding.rankRemain.text =
                if (neededPoints == 0)
                    "Максимальный ранг!"
                else
                    neededPoints.toString().prependIndent("Очков до повышения: ")

            binding.mail.visibility = if (it.mailIsPublic == true)
                View.VISIBLE
            else
                View.GONE

            binding.phone.visibility = if (it.phoneIsPublic == true)
                View.VISIBLE
            else
                View.GONE

            binding.avatar.strokeColor = ColorStateList.valueOf(
                context?.attrColor(
                    if (it.isBusy == true)
                        androidx.appcompat.R.attr.colorPrimary
                    else
                        com.google.android.material.R.attr.colorSurfaceContainerHigh
                ) ?: 0x000
            )

            binding.userIsBusy.setText("Ваш профиль в состоянии занятости.")
            binding.userIsBusy.visibility =
                if (it.isBusy == true)
                    View.VISIBLE
                else
                    View.GONE

            binding.myPetsTitle.setText(
                if (it.animals?.isNotEmpty() == true)
                    "Мои питомцы"
                else
                    "Мои питомцы (пусто)"
            )

            binding.myCardsTitle.setText(
                if (it.cards?.isNotEmpty() == true)
                    "Мои запросы"
                else
                    "Мои запросы (пусто)"
            )

            binding.myNotesTitle.setText(
                if (it.notes?.isNotEmpty() == true)
                    "Мои публикации"
                else
                    "Мои публикации (пусто)"
            )

            binding.myPetsRecycler.withModels {
                it.animals?.asReversed()?.forEachIndexed { index, it ->
                    animal {
                        id(index)
                        animalResponse(it)
                        navigateToAnimal { }
                    }
                }
            }

            binding.myCardsRecycler.withModels {
                it.cards?.asReversed()?.forEachIndexed { index, it ->
                    card {
                        id(index + 1)
                        cardResponse(it)
                        isInProfile(true)
                        navigateToOwner { }
                    }
                }
            }

            binding.myNotesRecycler.withModels {
                it.notes?.asReversed()?.forEachIndexed { index, noteResponse ->
                    note {
                        id(index)
                        noteResponse(noteResponse)
                        userResponse(it)
                        isInProfile(true)
                        navigateToNote {
                            findNavController().navigate(
                                ProfileFragmentDirections.actionProfileFragmentToNoteFragment(
                                    it.intId ?: -1
                                )
                            )
                        }
                        likeNote {
                            if (it.intId != null)
                                profileViewModel.likeNoteById(-1, it.intId, doOnError = {
                                    onError(it)
                                }, doOnBanned = {
                                    onBanned()
                                })
                        }
                    }
                }
            }
        }

        binding.editProfile.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment()
            )
        }

        binding.addMyNote.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToProfileNoteCreateBottomSheet()
            )
        }

        profileViewModel.loadMe(doOnError = {
            onError(it)
        }, doOnBanned = {
            onBanned()
        })

        binding.exit.setOnClickListener {
            onBanned()
        }
    }

    fun onBanned() {
        profileViewModel.exit()
        startActivity(Intent(context, AuthActivity::class.java))
        activity?.finish()
    }

    fun onError(errorText: String) {
        Toast.makeText(
            context,
            "Error: $errorText",
            Toast.LENGTH_SHORT
        ).show()
    }
}