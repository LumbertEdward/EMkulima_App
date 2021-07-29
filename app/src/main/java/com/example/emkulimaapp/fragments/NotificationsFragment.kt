package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.NotificationsRetrofit
import com.example.emkulimaapp.adapters.NotificationAdapter
import com.example.emkulimaapp.interfaces.NotificationsInterface
import com.example.emkulimaapp.models.AllNotifications
import com.example.emkulimaapp.models.Notification
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsFragment : Fragment() {
    @BindView(R.id.imgBackNotification)
    lateinit var back: ImageView
    @BindView(R.id.recyclerNotifications)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.txtNoNotifications)
    lateinit var noNot: TextView
    @BindView(R.id.progressNotifications)
    lateinit var progressBar: ProgressBar

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationInterface: NotificationsInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_notifications, null)
        ButterKnife.bind(this, view)
        back.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        recyclerView.visibility = View.GONE
        noNot.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        getNotifications()
        return view
    }

    private fun getNotifications() {
        var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        var activity = activity as Context
        linearLayoutManager = LinearLayoutManager(activity)
        notificationAdapter = NotificationAdapter(activity)

        notificationInterface = NotificationsRetrofit.getRetrofit().create(NotificationsInterface::class.java)
        val call: Call<AllNotifications> = notificationInterface.getNotification(userId)
        call.enqueue(object : Callback<AllNotifications>{
            override fun onResponse(
                call: Call<AllNotifications>,
                response: Response<AllNotifications>
            ) {
                if (response.isSuccessful){
                    progressBar.visibility = View.GONE
                    setNotifications(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<AllNotifications>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun setNotifications(data: ArrayList<Notification>) {
        if (data.size > 0){
            var sharedPreferences: SharedPreferences = activity?.getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)!!
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("TOTAL", data.size)
            editor.apply()
            notificationAdapter.getNotifications(data)
            recyclerView.adapter = notificationAdapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.visibility = View.VISIBLE

        }
        else{
            noNot.visibility = View.VISIBLE
        }

    }
}