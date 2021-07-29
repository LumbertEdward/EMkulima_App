package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.ProfileRetrofit
import com.example.emkulimaapp.RetrofitClasses.UpdateProfileRetrofit
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.interfaces.ProfileInterface
import com.example.emkulimaapp.interfaces.UpdateProfileInterface
import com.example.emkulimaapp.models.AllCustomer
import com.example.emkulimaapp.models.Customer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    @BindView(R.id.editFirst)
    lateinit var first: EditText
    @BindView(R.id.editLast)
    lateinit var last: EditText
    @BindView(R.id.editEmail)
    lateinit var email: EditText
    @BindView(R.id.editLocation)
    lateinit var location: EditText
    @BindView(R.id.editPhone)
    lateinit var phone: EditText
    @BindView(R.id.cardSubmitProfile)
    lateinit var submit: CardView
    @BindView(R.id.scrollProfile)
    lateinit var scroll: ScrollView
    @BindView(R.id.progressProfile)
    lateinit var profile: ProgressBar
    @BindView(R.id.imgLogOut)
    lateinit var logOut: ImageView

    private lateinit var profileInterface: ProfileInterface
    private lateinit var updateProfileInterface: UpdateProfileInterface
    private lateinit var generalInterface: GeneralInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_profile, null)
        ButterKnife.bind(this, view)
        getDetails()
        profile.visibility = View.VISIBLE
        scroll.visibility = View.GONE
        submit.setOnClickListener {
            sendDetails()
        }

        logOut.setOnClickListener {
            generalInterface.logOut()
        }
        return view
    }

    private fun sendDetails() {
        var fs = first.text.toString().trim()
        var ls = last.text.toString().trim()
        var pn = phone.text.toString().trim()
        var lc = location.text.toString().trim()

        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        updateProfileInterface = UpdateProfileRetrofit.getRetrofit().create(UpdateProfileInterface::class.java)
        val call: Call<AllCustomer> = updateProfileInterface.updateUser(userId, fs, ls, pn, lc)
        call.enqueue(object : Callback<AllCustomer>{
            override fun onResponse(call: Call<AllCustomer>, response: Response<AllCustomer>) {
                if (response.isSuccessful){
                    Toast.makeText(activity, "Updated", Toast.LENGTH_LONG).show()
                    val newSharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
                        AppCompatActivity.MODE_PRIVATE
                    )!!
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("FIRSTNAME", fs)
                    editor.putString("LASTNAME", ls)
                    editor.putString("LOCATION", lc)
                    editor.putString("PHONENUMBER", pn)
                    editor.apply()
                }
            }

            override fun onFailure(call: Call<AllCustomer>, t: Throwable) {
                //Toast.makeText(activity, "Check Internet Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getDetails() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        profileInterface = ProfileRetrofit.getRetrofit().create(ProfileInterface::class.java)
        val call: Call<AllCustomer> = profileInterface.getUserDetails(userId)
        call.enqueue(object : Callback<AllCustomer>{
            override fun onResponse(call: Call<AllCustomer>, response: Response<AllCustomer>) {
                if (response.isSuccessful){
                    profile.visibility = View.GONE
                    showDetails(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AllCustomer>, t: Throwable) {
                Toast.makeText(activity, "Check Internet Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showDetails(data: ArrayList<Customer>) {
        if (data.size > 0){
            first.setText(data[0].firstName.toString())
            last.setText(data[0].lastName.toString())
            email.setText(data[0].email.toString())
            location.setText(data[0].location.toString())
            phone.setText(data[0].phoneNumber)
            scroll.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}