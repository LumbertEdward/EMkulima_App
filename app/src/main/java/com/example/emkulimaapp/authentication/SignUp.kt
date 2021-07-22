package com.example.emkulimaapp.authentication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.MainActivity
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.GoogleRegisterRetrofit
import com.example.emkulimaapp.RetrofitClasses.RegisterRetrofit
import com.example.emkulimaapp.interfaces.GoogleRegisterInterface
import com.example.emkulimaapp.interfaces.RegsiterInterface
import com.example.emkulimaapp.models.AllCustomer
import com.example.emkulimaapp.models.Customer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SignUp : AppCompatActivity() {
    @BindView(R.id.backLogin)
    lateinit var back: TextView
    @BindView(R.id.googleSignUp)
    lateinit var googleRegister: RelativeLayout
    @BindView(R.id.relRegister)
    lateinit var register: RelativeLayout
    @BindView(R.id.firstNameRegister)
    lateinit var firstName: EditText
    @BindView(R.id.lastNameRegister)
    lateinit var lastName: EditText
    @BindView(R.id.emailRegister)
    lateinit var email: EditText
    @BindView(R.id.passwordRegister)
    lateinit var password: EditText
    @BindView(R.id.confirmPasswordRegister)
    lateinit var confirm: EditText
    @BindView(R.id.locationRegister)
    lateinit var location: EditText
    @BindView(R.id.genderRegister)
    lateinit var gender: EditText
    @BindView(R.id.phoneNumberRegister)
    lateinit var phone: EditText
    @BindView(R.id.progressSignUp)
    lateinit var progressSign: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var googleRegisterInterface: GoogleRegisterInterface
    private lateinit var registerInterface: RegsiterInterface

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
            window.statusBarColor = resources.getColor(R.color.white, Resources.getSystem().newTheme())
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        setGoogleOptions()
        progressSign.visibility = View.GONE

        this.back.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        this.register.setOnClickListener {
            progressSign.visibility = View.VISIBLE
            val first = this.firstName.text.toString().trim()
            val last = this.lastName.text.toString().trim()
            val em = this.email.text.toString().trim()
            val gen = this.gender.text.toString().trim()
            val loc = this.location.text.toString().trim()
            val ph = this.phone.text.toString().trim()
            val pass = this.password.text.toString().trim()
            val conf = this.confirm.text.toString().trim()
            var uuId: UUID = UUID.randomUUID()

            if (pass == conf){
                if (!checkView(first, this.firstName) && !checkView(last, this.lastName) && !checkView(em, this.email) && !checkView(gen, this.gender) && !checkView(loc, this.location) && !checkView(ph, this.phone) && !checkView(pass, this.password)){

                    registerInterface = RegisterRetrofit.getRetrofit().create(RegsiterInterface::class.java)
                    val call: Call<AllCustomer> = registerInterface.registerUser(uuId.toString(), first, last, em, gen, ph, loc, pass)
                    call.enqueue(object : Callback<AllCustomer>{
                        override fun onResponse(
                            call: Call<AllCustomer>,
                            response: Response<AllCustomer>
                        ) {
                            if (response.isSuccessful){
                                progressSign.visibility = View.GONE
                                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                editor.putBoolean("LOGGEDIN", true)
                                editor.apply()
                                setUser(response.body()!!.data[0])
                            }
                        }

                        override fun onFailure(call: Call<AllCustomer>, t: Throwable) {
                            Toast.makeText(this@SignUp, t.message.toString(), Toast.LENGTH_LONG).show()
                            progressSign.visibility = View.GONE
                        }

                    })
                }
                else{
                    Toast.makeText(this@SignUp, "Passwords okay", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(this@SignUp, "Passwords do not match", Toast.LENGTH_LONG).show()
                this.confirm.error = "Passwords do not match"
                progressSign.visibility = View.GONE
            }
        }

        this.googleRegister.setOnClickListener{
            signUser()
        }
    }

    private fun setUser(customer: Customer) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("EMAIL", customer.email)
        editor.putString("FIRSTNAME", customer.firstName)
        editor.putString("USERID", customer.userId.toString())
        editor.putString("LOCATION", customer.location)
        editor.putString("PHONENUMBER", customer.phoneNumber)
        editor.apply()

        startActivity(Intent(this@SignUp, MainActivity::class.java))
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        finish()

    }

    private fun checkView(item: String, view: EditText): Boolean{
        if (TextUtils.isEmpty(item)){
            view.error = "Parameter Needed"
            return true
        }
        else{
            return false
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
                val email: String = userAccount.email.toString()
                val firstName: String = userAccount.displayName.toString()
                val photo: String = userAccount.photoUrl.toString()
                val lastName: String = userAccount.familyName.toString()
                val middleName: String = userAccount.givenName.toString()

                regUser(email, firstName, lastName, middleName, photo)
            }
        } catch (e: ApiException){
            Toast.makeText(this, "Error is " + e.statusCode.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun regUser(email: String, firstName: String, lastName: String, middleName: String, photo: String) {
        var uuId: UUID = UUID.randomUUID()
        googleRegisterInterface = GoogleRegisterRetrofit.getRetrofit().create(GoogleRegisterInterface::class.java)
        val call: Call<AllCustomer> = googleRegisterInterface.sendUser(uuId.toString(), firstName, lastName, email, photo)
        call.enqueue(object : Callback<AllCustomer>{
            override fun onResponse(call: Call<AllCustomer>, response: Response<AllCustomer>) {
                if (response.isSuccessful){
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("LOGGEDIN", true)
                    editor.apply()
                    setUserData(response.body()!!.data)
                    startActivity(Intent(this@SignUp, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    finish()
                }
            }

            override fun onFailure(call: Call<AllCustomer>, t: Throwable) {

            }

        })

    }

    private fun setUserData(data: ArrayList<Customer>) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("USERDETAILS", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("EMAIL", data[0].email)
        editor.putString("FIRSTNAME", data[0].firstName)
        editor.putString("USERID", data[0].userId.toString())
        editor.putString("LOCATION", data[0].location)
        editor.putString("PHONENUMBER", data[0].phoneNumber)
        editor.apply()

    }

    private fun setGoogleOptions(){
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }
}