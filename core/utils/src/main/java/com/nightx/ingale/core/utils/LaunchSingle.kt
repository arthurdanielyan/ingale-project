package com.nightx.ingale.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

private val jobMap = ConcurrentHashMap<String, Job?>()

fun CoroutineScope.launchSingle(
    key: String,
    block: suspend CoroutineScope.() -> Unit
) {
    val newJob = launch(block = block)

    // Atomically swap in the new job, get the previous one
    val old = jobMap.put(key, newJob)

    // Cancel the previous (if any)
    old?.cancel()

    // Cleanup atomically: only remove if we're still the current job
    newJob.invokeOnCompletion {
        jobMap.compute(key) { _, current ->
            if (current === newJob) null else current
        }
    }
}