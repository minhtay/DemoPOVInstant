package com.example.base.ui.auth


import android.content.Intent
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.base.R
import com.example.base.ui.base.CommonActivity
import com.example.base.ui.camera.CameraActivity
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import java.util.UUID

class LoginActivity : CommonActivity(R.layout.activity_login) {
    init {
        FirebaseApp.initializeApp(this)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    override fun afterViewCreated() {
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)
    }

    private fun signInWithGoogle() {
        val request = createGetCredentialRequest()
        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@LoginActivity, request)
                val googleIdTokenCredential = result.credential as GoogleIdTokenCredential
                val idToken = googleIdTokenCredential.idToken
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { taskId ->
                        if (taskId.isSuccessful) {
                            Log.d("TAGLogin", "signInWithGoogle: succes")
                        } else {
                            Log.d("TAGLogin", "signInWithGoogle: fails")
                        }
                    }
            } catch (e: Exception) {
                // show error
            }
        }
    }

    private fun createGetCredentialRequest(): GetCredentialRequest {
        val googleIdOption =
            com.google.android.libraries.identity.googleid.GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(getString(R.string.web_client_id))
                .setNonce(UUID.randomUUID().toString())
                .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) startActivity(Intent(this, CameraActivity::class.java))
    }
}