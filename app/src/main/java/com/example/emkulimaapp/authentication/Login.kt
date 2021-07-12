package com.example.emkulimaapp.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {
    @BindView(R.id.newAccount)
    lateinit var create: TextView
    @BindView(R.id.google)
    lateinit var googleLogin: ImageView

    lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var sharedPreferences: SharedPreferences

    var getRes = registerForActivityResult(StartActivityForResult(), ActivityResultCallback {
        if (it.resultCode == Activity.RESULT_OK){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            checkUser(task)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        sharedPreferences = this.getSharedPreferences("USER", MODE_PRIVATE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        this.create.setOnClickListener{
            startActivity(Intent(this, SignUp::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        this.googleLogin.setOnClickListener {
            loginUser()
        }

        setOptions()
    }

    private fun setOptions(){
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun loginUser() {
        val intent: Intent = googleSignInClient.signInIntent
        getRes.launch(intent)
    }

    private fun checkUser(task: Task<GoogleSignInAccount>) {
        try {
            val googleSignInAccount: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            val email: String = googleSignInAccount.email.toString()
            val firstName: String = googleSignInAccount.displayName.toString()
            val photo: String = googleSignInAccount.photoUrl.toString()
            val lastName: String = googleSignInAccount.familyName.toString()
            val middleName: String = googleSignInAccount.givenName.toString()

            if (googleSignInAccount.email != null){
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putBoolean("LOGGEDIN", true)
                editor.apply()
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }catch (e: ApiException){

        }
    }

    override fun onStart() {
        super.onStart()
        val status = sharedPreferences.getBoolean("LOGGEDIN", false)
        if (status){
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}