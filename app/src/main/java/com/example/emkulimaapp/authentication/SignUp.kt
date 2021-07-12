package com.example.emkulimaapp.authentication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.MainActivity
import com.example.emkulimaapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SignUp : AppCompatActivity() {
    @BindView(R.id.backLogin)
    lateinit var back: TextView
    @BindView(R.id.googleSignUp)
    lateinit var googleRegister: ImageView

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var googleSignInClient: GoogleSignInClient
    val getCont = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if (it.resultCode == Activity.RESULT_OK){
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResults(task)
            }
            else{
                Toast.makeText(this, "Error is ", Toast.LENGTH_LONG).show()
            }

        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        ButterKnife.bind(this)

        sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        setGoogleOptions()

        this.back.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        this.googleRegister.setOnClickListener{
            signUser()
        }
    }

    private fun signUser() {
        val intent: Intent = googleSignInClient.signInIntent
        getCont.launch(intent)
    }

    private fun handleSignInResults(task: Task<GoogleSignInAccount>) {
        try {
            val userAccount: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            if (userAccount.email != null){
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putBoolean("LOGGEDIN", true)
                editor.apply()
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        } catch (e: ApiException){
            Toast.makeText(this, "Error is " + e.statusCode.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun setGoogleOptions(){
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }
}