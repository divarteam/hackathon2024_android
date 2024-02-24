package ru.divarteam.franimal.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.TypedValue
import androidx.annotation.ColorInt
import org.joda.time.DateTime
import java.io.File
import java.lang.Exception
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale


@ColorInt
fun Context.attrColor(attr: Int) = TypedValue().let {
    theme.resolveAttribute(attr, it, true)
    it.data
}

val String.date
    get() = Date.from(
        OffsetDateTime.parse(this)
                .toInstant()
    )

val Date.russianString
    get() = DateFormat.format("dd MMM yyyy в HH:mm", this)


fun Uri.photoUriToFile(context: Context): File {
    val filePath = arrayOf(MediaStore.Images.Media.DATA)
    return File(context.contentResolver.query(
        this, filePath, null, null, null
    )?.let {
        it.moveToFirst()
        val filePathStr = it.getString(
            it.getColumnIndex(filePath[0]).let {
                if (it < 0)
                    0
                else
                    it
            }
        )
        it.close()
        filePathStr
    } ?: "")
}