package ru.divarteam.franimal.presentation.animal

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import ru.divarteam.franimal.R
import ru.divarteam.franimal.data.network.response.AnimalResponse
import ru.divarteam.franimal.util.KotlinHolder
import ru.divarteam.franimal.util.attrColor

@EpoxyModelClass
abstract class AnimalEpoxyModel : EpoxyModelWithHolder<AnimalEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var animalResponse: AnimalResponse

    @EpoxyAttribute
    lateinit var navigateToAnimal: (AnimalResponse) -> Unit

    override fun getDefaultLayout() = R.layout.item_profile_animal

    override fun bind(holder: Holder) {
        Glide.with(holder.photo)
            .load("https://api.hackathon2024.divarteam.ru/file_storage/${animalResponse.photoFilename}")
            .placeholder(
                ContextCompat.getDrawable(
                    holder.photo.context,
                    R.drawable.ic_launcher_background
                )?.apply {
                    setTint(
                        holder.photo.context.attrColor(com.google.android.material.R.attr.colorSurfaceContainerHighest)
                    )
                }
            )
            .error(
                Glide.with(holder.photo)
                    .load("https://i.imgur.com/Kr3SNSO.png")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(holder.photo)

        holder.nickname.setText(animalResponse.nickname)
        holder.type.setText(animalResponse.type?.prependIndent("Это: "))
        holder.subtype.setText(animalResponse.subtype?.prependIndent("Порода: "))
        holder.bloodType.setText(animalResponse.bloodGroup?.prependIndent("Группа крови: "))
        holder.root.setOnClickListener {
            navigateToAnimal(animalResponse)
        }
    }

    inner class Holder : KotlinHolder() {
        val root: MaterialCardView by bind(R.id.animal_root)
        val photo: ShapeableImageView by bind(R.id.animal_photo)
        val nickname: TextView by bind(R.id.nickname)
        val type: TextView by bind(R.id.type)
        val subtype: TextView by bind(R.id.subtype)
        val bloodType: TextView by bind(R.id.blood_type)
    }
}