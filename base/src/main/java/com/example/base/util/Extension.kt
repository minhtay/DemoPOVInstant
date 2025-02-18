package com.example.base.util

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.showDialog(fragmentManager: FragmentManager) {
    val tag = this::class.java.simpleName
    try {
        this.show(fragmentManager, tag)
        fragmentManager.executePendingTransactions()
    } catch (ex: Exception) {
        val existingFragment = fragmentManager.findFragmentByTag(tag)
        if (existingFragment is DialogFragment && existingFragment.isAdded) {
            existingFragment.dismissAllowingStateLoss()
            this.show(fragmentManager, tag)
        }
    }
}