package com.example.base.util

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.base.ui.base.CommonActivity

fun <T : ViewBinding> CommonActivity.viewBinding(
    bindingInflater: (LayoutInflater) -> T,
    beforeSetContent: () -> Unit = {}
) = ActivityViewBindingDelegate(this, bindingInflater, beforeSetContent)