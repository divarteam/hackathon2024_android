package ru.divarteam.franimal.presentation.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import com.google.android.material.appbar.AppBarLayout
import ru.divarteam.franimal.R

class FollowyAppBar constructor(
    cont: Context,
    attrs: AttributeSet?
) : AppBarLayout(cont, attrs) {

    fun setStartButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.start_button).setOnClickListener(p0)
    }

    fun setEndButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.end_button).setOnClickListener(p0)
    }

    fun hideEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.INVISIBLE
    }

    fun showEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.VISIBLE
    }

    val startButton: ImageView
        get() = findViewById(R.id.start_button)

    val endButton: ImageView
        get() = findViewById(R.id.end_button)

    init {
        View.inflate(context, R.layout.layout_followy_appbar, this)

        elevation = 0f
        background = ColorDrawable("#00000000".toColorInt())

        val text = findViewById<View>(R.id.appbar_title) as TextView
        text.isSelected = true

        addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            text.scaleX = 1f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 0.15f)
            text.scaleY = 1f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 0.15f)
//            val vaX = ObjectAnimator.ofFloat(
//                text,
//                SCALE_X,
//                1f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 0.15f)
//            )
//            val vaY = ObjectAnimator.ofFloat(
//                text,
//                SCALE_Y,
//                1f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 0.15f)
//            )
//            vaX.duration = 300
//            vaY.duration = 300
//            vaX.start()
//            vaY.start()
        }

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.FollowyAppBar)

            typedArray.getResourceId(
                R.styleable.FollowyAppBar_startIcon,
                0
            ).let {
                if (it != 0)
                    findViewById<ImageView>(R.id.start_button).setImageResource(it)
            }

            typedArray.getResourceId(
                R.styleable.FollowyAppBar_endIcon,
                0
            ).let {
                if (it != 0)
                    findViewById<ImageView>(R.id.end_button).setImageResource(it)
            }

            text.text = typedArray.getText(
                R.styleable.FollowyAppBar_titleText
            )

            typedArray.recycle()
        }
    }
}