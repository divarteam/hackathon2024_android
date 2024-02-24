package ru.divarteam.franimal.presentation.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.LocalDate
import ru.divarteam.franimal.R
import ru.divarteam.franimal.databinding.FragmentProfileEditBinding
import ru.divarteam.franimal.util.attrColor
import java.io.IOException
import java.util.Date


@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var chosenDates = listOf<String>()

    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                profileViewModel.updateUserPhoto(requireContext(), uri,
                    doOnError = {
                        onError(it)
                    },
                    doOnSuccess = {
                        Glide.with(binding.avatar)
                            .load("https://api.hackathon2024.divarteam.ru/file_storage/$it")
                            .placeholder(
                                ContextCompat.getDrawable(
                                    binding.avatar.context,
                                    R.drawable.ic_launcher_background
                                )
                            )
                            .into(binding.avatar)

                        Toast.makeText(
                            context,
                            "Изображение загружено! Не забудьте сохранить изменения.",
                            Toast.LENGTH_LONG
                        ).show()
                    })
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

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
    ) = FragmentProfileEditBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.appbar.setStartButtonOnClickListener {
            findNavController().popBackStack()
        }

        profileViewModel.loading.observe(viewLifecycleOwner) {
            binding.loading.visibility =
                if (it)
                    View.VISIBLE
                else
                    View.GONE
        }

        binding.chooseBusyDates.setOnClickListener {
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Выберите даты")
                .build()
                .apply {
                    addOnPositiveButtonClickListener {
                        var start = LocalDate(Date(it.first))
                        val end = LocalDate(Date(it.second))
                        val totalDates = arrayListOf<LocalDate>()
                        while (!start.isAfter(end)) {
                            totalDates.add(start)
                            start = start.plusDays(1)
                        }
                        println(totalDates)
                        chosenDates = totalDates.map { it.toString("yyyy-MM-dd") }
                        binding.busyDatesText.setText(
                            chosenDates
                                .joinToString(", ")
                                .prependIndent("Текущие выбранные дни, в которые вас лучше не беспокоить: ")
                        )
                    }
                }
                .show(childFragmentManager, "choose_busy_dates")
        }

        profileViewModel.userResponse.observe(viewLifecycleOwner) {
            binding.age.editText?.setText("${it.age}")
            binding.fullname.editText?.setText(it.fullname)
            binding.city.editText?.setText(it.city)
            binding.phone.editText?.setText(it.phone)
            binding.vk.editText?.setText(it.vkLink)
            binding.telegram.editText?.setText(it.tgLink)
            binding.showEmail.isChecked = it.mailIsPublic == true
            binding.showPhoneNumber.isChecked = it.phoneIsPublic == true
            binding.showVkLink.isChecked = it.vkLinkIsPublic == true
            binding.showTgLink.isChecked = it.tgLinkIsPublic == true
            chosenDates = it.busyDates ?: listOf()

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

            binding.busyDatesText.setText(
                it.busyDates
                    ?.joinToString(", ")
                    ?.prependIndent("Текущие выбранные дни, в которые вас лучше не беспокоить: ")
            )
        }

        binding.save.setOnClickListener {
            profileViewModel.updateUser(
                age = binding.age.editText?.text.toString().toInt(),
                fullname = (binding.fullname.editText?.text
                    ?: profileViewModel.userResponse.value?.fullname).toString(),
                city = (binding.city.editText?.text
                    ?: profileViewModel.userResponse.value?.city).toString(),
                mailIsPublic = binding.showEmail.isChecked,
                phone = binding.phone.editText?.text.toString(),
                phoneIsPublic = binding.showPhoneNumber.isChecked,
                vkLink = binding.vk.editText?.text.toString(),
                vkLinkIsPublic = binding.showVkLink.isChecked,
                tgLink = binding.telegram.editText?.text.toString(),
                tgLinkIsPublic = binding.showTgLink.isChecked,
                busyDates = chosenDates,
                doOnError = {
                    onError(it)
                },
                doOnSuccess = {
                    Toast.makeText(
                        context,
                        "Изменения применены.",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }
            )
        }

        binding.updateAvatar.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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