package ru.namerpro.playlistmaker.common.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.namerpro.playlistmaker.R

fun showToast(
    fragment: Fragment,
    layoutInflater: LayoutInflater,
    message: String
) {
    val successPlaylistAddSnackbar = Snackbar.make(fragment.requireView(), "", Snackbar.LENGTH_LONG)
    val customSuccessPlaylistAddSnackbar = layoutInflater.inflate(R.layout.custom_toast, null)

    val snackbarContent = customSuccessPlaylistAddSnackbar?.findViewById<TextView>(R.id.custom_toast_content)

    snackbarContent?.text = message

    successPlaylistAddSnackbar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackbarLayout = successPlaylistAddSnackbar.view as Snackbar.SnackbarLayout

    snackbarLayout.setPadding(0, 0, 0, 0)
    snackbarLayout.addView(customSuccessPlaylistAddSnackbar, 0)

    successPlaylistAddSnackbar.show()
}