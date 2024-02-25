package ru.divarteam.franimal.presentation.notes

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import ru.divarteam.franimal.R
import ru.divarteam.franimal.data.network.response.CommentResponse
import ru.divarteam.franimal.data.network.response.UserResponse
import ru.divarteam.franimal.util.KotlinHolder
import ru.divarteam.franimal.util.attrColor
import ru.divarteam.franimal.util.date
import ru.divarteam.franimal.util.russianString

@EpoxyModelClass
abstract class CommentEpoxyModel : EpoxyModelWithHolder<CommentEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var commentResponse: CommentResponse

    @EpoxyAttribute
    lateinit var removeComment: (CommentResponse) -> Unit

    @EpoxyAttribute
    lateinit var navigateToOwner: (CommentResponse) -> Unit

    override fun getDefaultLayout() = R.layout.item_comment

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        holder.text.setText(commentResponse.text)
        holder.text.isSelected = true
        holder.fullname.setText(commentResponse.user?.fullname)
        holder.dateTime.setText(commentResponse.creationDate?.date?.russianString)

        holder.avatar.setOnClickListener { navigateToOwner(commentResponse) }
        holder.fullname.setOnClickListener { navigateToOwner(commentResponse) }
        holder.dateTime.setOnClickListener { navigateToOwner(commentResponse) }

        Glide.with(holder.avatar)
            .load("https://api.hackathon2024.divarteam.ru/file_storage/${commentResponse.user?.photoFilename}")
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
    }

    inner class Holder : KotlinHolder() {
        val root: MaterialCardView by bind(R.id.note_root)
        val avatar: ShapeableImageView by bind(R.id.avatar)
        val fullname: TextView by bind(R.id.fullname)
        val text: TextView by bind(R.id.text)
        val dateTime: TextView by bind(R.id.datetime)
    }
}