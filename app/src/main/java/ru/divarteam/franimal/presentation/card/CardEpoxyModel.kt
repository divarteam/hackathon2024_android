package ru.divarteam.franimal.presentation.card

import android.graphics.drawable.Drawable
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
import ru.divarteam.franimal.data.network.response.CardResponse
import ru.divarteam.franimal.util.KotlinHolder
import ru.divarteam.franimal.util.attrColor

@EpoxyModelClass
abstract class CardEpoxyModel : EpoxyModelWithHolder<CardEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var cardResponse: CardResponse

    @EpoxyAttribute
    lateinit var navigateToOwner: (userId: Int) -> Unit

    @EpoxyAttribute
    @JvmField
    var isInProfile: Boolean = false

    override fun getDefaultLayout() =
        if (isInProfile)
            R.layout.item_profile_card
        else
            R.layout.item_card

    override fun bind(holder: Holder) {
        holder.mainInfo.setText("${cardResponse.animalType}, ${cardResponse.animalBloodGroup}, ${cardResponse.animalBloodAmount} мл")
        holder.expirationDate.setText(cardResponse.expirationDate?.prependIndent("До: "))
        holder.address.setText(cardResponse.address?.prependIndent("Адрес: "))
        holder.reason.setText(cardResponse.reason)
        if (cardResponse.userIntId != null) {
            holder.root.setOnClickListener { navigateToOwner(cardResponse.userIntId ?: -1) }
            holder.fullname.setOnClickListener { navigateToOwner(cardResponse.userIntId ?: -1) }
        }
        Glide.with(holder.avatar)
            .load("https://api.hackathon2024.divarteam.ru/file_storage/${cardResponse.user?.photoFilename}")
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

        holder.fullname.setText(cardResponse.user?.fullname)
    }

    inner class Holder : KotlinHolder() {
        val root: MaterialCardView by bind(R.id.card_root)
        val mainInfo: TextView by bind(R.id.main_info)
        val expirationDate: TextView by bind(R.id.expiration_date)
        val address: TextView by bind(R.id.address)
        val reason: TextView by bind(R.id.reason)
        val avatar: ShapeableImageView by bind(R.id.avatar)
        val fullname: TextView by bind(R.id.fullname)
    }
}