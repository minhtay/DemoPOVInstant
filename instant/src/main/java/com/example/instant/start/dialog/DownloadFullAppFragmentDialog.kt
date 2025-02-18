package com.example.instant.start.dialog

import com.example.base.ui.base.BaseBottomSheetDialogFragment
import com.example.instant.databinding.FragmentDialogDownloadFullAppBinding

class DownloadFullAppFragmentDialog :
    BaseBottomSheetDialogFragment<FragmentDialogDownloadFullAppBinding>(
        FragmentDialogDownloadFullAppBinding::inflate
    ) {

    override fun listenerViewEvent() {
        binding.apply {
            btnDownloadFullApp.setOnClickListener {
                //open play store is download
            }
            btnCancel.setOnClickListener {
                this@DownloadFullAppFragmentDialog.dismiss()
            }
        }
    }
}
