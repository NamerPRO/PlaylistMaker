package ru.namerpro.playlistmaker.common.utils

import android.content.res.Resources

class Dp(
    private val resources: Resources
) {

    fun of(
        px: Int
    ): Int {
        return (resources.displayMetrics.density * px + 0.5f).toInt()
    }

}