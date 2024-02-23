package ru.divarteam.franimal.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import ru.divarteam.franimal.R


class GenericTextWatcher internal constructor(
    private val currentView: View,
    private val nextView: View?,
    val list: List<View>
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        list.indexOf(currentView).let {
            nextView?.requestFocus()
            if (it == list.size - 1 && editable.toString().isNotEmpty()) {
                currentView.let {
                    val imm =
                        it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                return
            }
        }
    }
}

class GenericKeyEvent internal constructor(
    private val currentView: EditText,
    private val previousView: EditText?,
    val list: List<View>
) : View.OnKeyListener {
    override fun onKey(view: View, keyCode: Int, event: KeyEvent) =
        if (event.action == KeyEvent.ACTION_DOWN
            && keyCode == KeyEvent.KEYCODE_DEL
            && currentView.id != list.get(0).id
            && currentView.text.isEmpty()
        ) {
            previousView?.text = null
            previousView?.requestFocus()
            true
        } else false
}

fun List<EditText>.makeCustomOTP() {
    for (i in 0 until this.size) {
        this.get(i).addTextChangedListener(
            GenericTextWatcher(
                this.get(i),
                this.getOrNull(i + 1),
                this
            )
        )
    }

    for (i in this.size - 1 downTo 0) {
        this.get(i).setOnKeyListener(
            GenericKeyEvent(
                this.get(i),
                this.getOrNull(i - 1),
                this
            )
        )
    }
}