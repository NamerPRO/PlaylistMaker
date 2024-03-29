package ru.namerpro.playlistmaker.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: suspend (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null

    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (param != null && (debounceJob?.isCompleted != false || useLastParam)) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}
