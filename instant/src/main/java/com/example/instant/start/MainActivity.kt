package com.example.instant.start

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.base.ui.base.CommonActivity
import com.example.base.util.showDialog
import com.example.base.util.viewBinding
import com.example.instant.R
import com.example.instant.databinding.ActivityMainBinding
import com.example.instant.start.dialog.DownloadFullAppFragmentDialog
import com.example.instant.start.dialog.ScanQRFragmentDialog

class MainActivity : CommonActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    override fun afterViewCreated() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view: View?, insets: WindowInsetsCompat ->
            val systemBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.main.setPadding(0, systemBar.top, 0, systemBar.bottom)
            insets
        }

        binding.apply {
            btnCreateEvent.setOnClickListener {
                DownloadFullAppFragmentDialog().showDialog(supportFragmentManager)
            }
            btnScanQR.setOnClickListener {
                ScanQRFragmentDialog().showDialog(supportFragmentManager)
            }
        }
    }


}