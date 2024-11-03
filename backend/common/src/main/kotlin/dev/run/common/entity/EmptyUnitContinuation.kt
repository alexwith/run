package dev.run.common.entity

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object EmptyUnitContinuation : Continuation<Unit> {

    override fun resumeWith(result: Result<Unit>) {}

    override val context: CoroutineContext = EmptyCoroutineContext
}