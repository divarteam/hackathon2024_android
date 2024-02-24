package ru.divarteam.franimal.presentation.notes

import android.annotation.SuppressLint
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.View
import android.widget.ImageView
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
import ru.divarteam.franimal.data.network.response.NoteResponse
import ru.divarteam.franimal.data.network.response.ProductResponse
import ru.divarteam.franimal.data.network.response.UserResponse
import ru.divarteam.franimal.util.KotlinHolder
import ru.divarteam.franimal.util.attrColor
import ru.divarteam.franimal.util.date
import ru.divarteam.franimal.util.russianString

@EpoxyModelClass
abstract class NoteEpoxyModel : EpoxyModelWithHolder<NoteEpoxyModel.Holder>() {

    @EpoxyAttribute
    lateinit var noteResponse: NoteResponse

    @EpoxyAttribute
    lateinit var userResponse: UserResponse

    @EpoxyAttribute
    lateinit var likeNote: (NoteResponse) -> Unit

    @EpoxyAttribute
    lateinit var navigateToNote: (NoteResponse) -> Unit

    @EpoxyAttribute
    lateinit var navigateToProfile: (UserResponse) -> Unit

    @EpoxyAttribute
    @JvmField
    var isInProfile: Boolean = false

    override fun getDefaultLayout() =
        if (isInProfile)
            R.layout.item_profile_note
        else
            R.layout.item_note

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        holder.text.setText(noteResponse.text.let {
            if (isInProfile)
                it?.replace("\n", "  ")
            else
                it
        })
        holder.text.isSelected = true
        holder.fullname.setText(userResponse.fullname)
        holder.like.setText(noteResponse.likeAmount.toString())

        if (!isInProfile)
            holder.avatar.setOnClickListener {
                navigateToProfile(userResponse)
            }

        holder.likeCard.setOnClickListener {
            likeNote(noteResponse)
        }

        holder.root.setOnClickListener {
            navigateToNote(noteResponse)
        }

        holder.commentCard.setOnClickListener {
            navigateToNote(noteResponse)
        }

        holder.comment.setText((noteResponse.comments?.size ?: 0).toString())

        holder.like.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                holder.like.context,
                if (noteResponse.likedByMe == true)
                    R.drawable.ic_liked
                else
                    R.drawable.ic_not_liked
            ), null, null, null
        )

        holder.dateTime.setText(noteResponse.creationDate?.date?.russianString)

        holder.picture.visibility =
            if (isInProfile || noteResponse.photoFilename == null)
                View.GONE
            else
                View.VISIBLE

        Glide.with(holder.picture)
            .load("https://api.hackathon2024.divarteam.ru/file_storage/${noteResponse.photoFilename}")
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
            .into(holder.picture)

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
    }

    inner class Holder : KotlinHolder() {
        val root: MaterialCardView by bind(R.id.note_root)
        val avatar: ShapeableImageView by bind(R.id.avatar)
        val fullname: TextView by bind(R.id.fullname)
        val text: TextView by bind(R.id.text)
        val likeCard: MaterialCardView by bind(R.id.like_card)
        val like: TextView by bind(R.id.like)
        val commentCard: MaterialCardView by bind(R.id.comment_card)
        val comment: TextView by bind(R.id.comment)
        val dateTime: TextView by bind(R.id.datetime)
        val picture: ShapeableImageView by bind(R.id.picture)
    }
}