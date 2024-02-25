package ru.divarteam.franimal.presentation.animal

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.EpoxyRecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import ru.divarteam.franimal.R
import ru.divarteam.franimal.data.network.response.AnimalResponse
import ru.divarteam.franimal.data.network.response.UserResponse
import ru.divarteam.franimal.util.KotlinHolder
import ru.divarteam.franimal.util.attrColor

@EpoxyModelClass
abstract class UserAnimalsEpoxyModel: EpoxyModelWithHolder<UserAnimalsEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var userResponse: UserResponse

    @EpoxyAttribute
    lateinit var navigateToAnimal: (AnimalResponse) -> Unit

    @EpoxyAttribute
    lateinit var navigateToOwner: (UserResponse) -> Unit

    override fun getDefaultLayout() = R.layout.item_search_user

    override fun bind(holder: Holder) {
        Glide.with(holder.avatar)
            .load("https://api.hackathon2024.divarteam.ru/file_storage/${userResponse.photoFilename}")
            .placeholder(
                ContextCompat.getDrawable(
                    holder.avatar.context,
                    R.drawable.ic_launcher_background
                )?.apply {
                    setTint(
                        holder.avatar.context.attrColor(com.google.android.material.R.attr.colorSurfaceContainerHighest)
                    )
                }
            )
            .error(
                Glide.with(holder.avatar)
                    .load("https://i.imgur.com/Kr3SNSO.png")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(holder.avatar)

        holder.avatar.setOnClickListener { navigateToOwner(userResponse) }
        holder.fullname.setOnClickListener { navigateToOwner(userResponse) }

        holder.fullname.setText(userResponse.fullname)
        holder.petsRecycler.withModels {
            userResponse.animals?.forEachIndexed { index, animalResponse ->
                animal {
                    id(index)
                    animalResponse(animalResponse)
                    navigateToAnimal(navigateToAnimal)
                }
            }
        }
    }

    inner class Holder: KotlinHolder() {
        val root: MaterialCardView by bind(R.id.user_root)
        val avatar: ShapeableImageView by bind(R.id.avatar)
        val fullname: TextView by bind(R.id.fullname)
        val petsRecycler: EpoxyRecyclerView by bind(R.id.pets_recycler)
    }
}