package ru.miem.psychoEvaluation.common.designSystem.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(clazz: Class<T>): T = f() as T
    }
