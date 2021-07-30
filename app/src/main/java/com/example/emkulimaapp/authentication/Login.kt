package com.example.emkulimaapp.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityOptionsCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.MainActivity
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.GoogleLoginRetrofit
import com.example.emkulimaapp.RetrofitClasses.LoginRetrofit
import com.example.emkulimaapp.interfaces.GoogleLoginInterface
import com.example.emkulimaapp.interfaces.LoginInterface
import com.example.emkulimaapp.models.AllCustomer
import com.example.emkulimaapp.models.Customer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    @BindView(R.id.newAccount)
    lateinit var create: TextView
    @BindView(R.id.google)
    lateinit var googleLogin: RelativeLayout
    @BindView(R.id.login)
    lateinit var login: RelativeLayout
    @BindView(R.id.emailLogin)
    lateinit var email: EditText
    @BindView(R.id.passwordLogin)
    lateinit var password: EditText
    @BindView(R.id.progressLogin)
    lateinit var progressBar: ProgressBar

    lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googleLoginInterface: GoogleLoginInterface
    private lateinit var loginInterface: LoginInterface

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
            window.statusBarColor = resources.getColor(R.color.white, Resources.getSystem().newTheme())
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        progressBar.visibility = View.GONE
        this.create.setOnClickListener{
            startActivity(Intent(this, SignUp::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        this.login.setOnClickListener {
            var email = this.email.text.toString().trim()
            var password = this.password.text.toString().trim()
            confirmDetails(email, password)
            progressBar.visibility = View.VISIBLE
        }

        this.googleLogin.setOnClickListener {
            //loginUser()
        }

        setOptions()
    }

    private fun confirmDetails(email: String, password: String) {
        loginInterface = LoginRetrofit.getRetrofit().create(LoginInterface::class.java)
        val call: Call<AllCustomer> = loginInterface.loginUser(email, password)
        call.enqueue(object : Callback<AllCustomer>{
            override fun onResponse(call: Call<AllCustomer>, response: Response<AllCustomer>) {
                if (response.isSuccessful){
                    if (response.body()!!.data.size > 0){
                        progressBar.visibility = View.GONE
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putBoolean("LOGGEDIN", true)
                        editor.apply()
                        saveUserData(response.body()!!.data)
                        startActivity(Intent(this@Login, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        finish()
                    }
                    else{
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@Login, "Wrong username or password", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<AllCustomer>, t: Throwable) {
                Toast.makeText(this@Login, "Check Network Connection", Toast.LENGTH_LONG).show()
                Log.i("LOGIN", t.message.toString())
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun saveUserData(data: ArrayList<Customer>) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("EMAIL", data[0].email)
        editor.putString("FIRSTNAME", data[0].firstName)
        editor.putString("LASTNAME", data[0].lastName)
        editor.putString("USERID", data[0].userId.toString())
        editor.putString("LOCATION", data[0].location)
        editor.putString("PHONENUMBER", data[0].phoneNumber)
        editor.apply()

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
                checkUserDetails(email, firstName)
            }
        }catch (e: ApiException){

        }
    }

    private fun checkUserDetails(email: String, firstName: String) {
        googleLoginInterface = GoogleLoginRetrofit.getRetrofit().create(GoogleLoginInterface::class.java)
        val call: Call<AllCustomer> = googleLoginInterface.getUser(email)
        call.enqueue(object : Callback<AllCustomer>{
            override fun onResponse(call: Call<AllCustomer>, response: Response<AllCustomer>) {
                if (response.isSuccessful){
                    if (response.body()!!.data.size > 0){
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putBoolean("LOGGEDIN", true)
                        editor.apply()
                        setUser(response.body()!!.data)
                        startActivity(Intent(this@Login, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        finish()
                    }
                    else{
                        Toast.makeText(this@Login, "Email does not exist", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<AllCustomer>, t: Throwable) {
                Toast.makeText(this@Login, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun setUser(data: ArrayList<Customer>) {
        if (data.size > 0){
            val sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("EMAIL", data[0].email)
            editor.putString("FIRSTNAME", data[0].firstName)
            editor.putString("LASTNAME", data[0].lastName)
            editor.putString("USERID", data[0].userId.toString())
            editor.putString("LOCATION", data[0].location)
            editor.putString("PHONENUMBER", data[0].phoneNumber)
            editor.apply()
            editor.apply()
        }
    }

    override fun onStart() {
        super.onStart()
        val status = sharedPreferences.getBoolean("LOGGEDIN", false)
        if (status){
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }
    }
}