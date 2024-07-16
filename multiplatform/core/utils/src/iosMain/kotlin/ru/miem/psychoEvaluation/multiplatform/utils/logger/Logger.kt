package ru.miem.psychoEvaluation.multiplatform.utils.logger

@Suppress("EmptyFunctionBlock")
actual object Logger {

    actual fun d(message: String, vararg args: Any) {}

    actual fun w(message: String, vararg args: Any) {}

    actual fun e(throwable: Throwable, message: String, vararg args: Any) {}

    actual fun e(message: String, vararg args: Any) {}
}
