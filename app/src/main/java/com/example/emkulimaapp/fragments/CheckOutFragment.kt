package com.example.emkulimaapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemSelected
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CheckOutRetrofit
import com.example.emkulimaapp.RetrofitClasses.OrderRetrofit
import com.example.emkulimaapp.RetrofitClasses.SendNotificationRetrofit
import com.example.emkulimaapp.interfaces.*
import com.example.emkulimaapp.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class CheckOutFragment : Fragment(), AdapterView.OnItemSelectedListener {
    @BindView(R.id.spinner)
    lateinit var spinner: Spinner
    @BindView(R.id.calendar)
    lateinit var calendar: ImageView
    @BindView(R.id.orderId)
    lateinit var orderId: TextView
    @BindView(R.id.txtLocation)
    lateinit var location: TextView
    @BindView(R.id.txtUserName)
    lateinit var name: TextView
    @BindView(R.id.txtUserPhone)
    lateinit var phone: TextView
    @BindView(R.id.txtNumber)
    lateinit var number: TextView
    @BindView(R.id.txtItemsPrice)
    lateinit var price: TextView
    @BindView(R.id.txtDate)
    lateinit var date: TextView
    @BindView(R.id.cardConfirmOrder)
    lateinit var confirm: CardView
    @BindView(R.id.imgBackCheckOut)
    lateinit var imgBack: ImageView
    @BindView(R.id.linearCheckOutAll)
    lateinit var linearCheckOut: LinearLayout
    @BindView(R.id.progressCheckOut)
    lateinit var progressBar: ProgressBar

    private lateinit var orderInterface: OrderInterface
    private lateinit var checkOutInterface: CheckOutInterface
    private lateinit var generalInterface: GeneralInterface
    private lateinit var sendNotificationInterface: SendNotificationInterface

    val options = arrayOf("8:30 Am", "10:00 AM", "12:00PM", "4:30PM")

    private var time: String = "8:30 Am"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_check_out, null)
        ButterKnife.bind(this, view)
        imgBack.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
        linearCheckOut.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        var activity = activity as Context
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(activity, android.R.layout.simple_spinner_item, options)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = this

        confirm.setOnClickListener {
            checkOut()
        }
        getList()
        return view
    }

    private fun checkOut() {
        makeOrder()
    }

    private fun makeOrder() {
        var ordId = orderId.text.toString()
        var order_price = price.text.toString().toInt()
        var delivery_date = date.text.toString()
        val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
            AppCompatActivity.MODE_PRIVATE
        )!!
        var uId = sharedPreferences.getString("USERID", "1")!!
        orderInterface = OrderRetrofit.getRetrofit().create(OrderInterface::class.java)
        val call: Call<AllOrders> = orderInterface.orderProduct(ordId, order_price, delivery_date, time, uId)
        call.enqueue(object : Callback<AllOrders>{
            override fun onResponse(call: Call<AllOrders>, response: Response<AllOrders>) {
                if (response.isSuccessful){
                    showSuccessDialog()
                    sendOrderProducts()
                }
            }

            override fun onFailure(call: Call<AllOrders>, t: Throwable) {

            }
        })
    }

    private fun sendOrderProducts() {
        var lst: ArrayList<Cart> = arguments?.getParcelableArrayList<Cart>("CART")!!
        for (i in lst.indices){
            checkOutInterface = CheckOutRetrofit.getRetrofit().create(CheckOutInterface::class.java)
            var call: Call<AllOrderItems> = checkOutInterface.checkOut(lst[i].productId!!, lst[i].farmerId!!, orderId.text.toString(), "Pending")
            call.enqueue(object : Callback<AllOrderItems>{
                override fun onResponse(
                    call: Call<AllOrderItems>,
                    response: Response<AllOrderItems>
                ) {
                    if (response.isSuccessful){

                    }
                }

                override fun onFailure(call: Call<AllOrderItems>, t: Throwable) {
                    Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
                }

            })
        }

    }

    private fun showSuccessDialog(){
        var activity = activity as Context
        var alertDial: AlertDialog.Builder = AlertDialog.Builder(activity)
        var layoutInflater: LayoutInflater = this.layoutInflater
        var view: View = layoutInflater.inflate(R.layout.success_layout, null)
        alertDial.setView(view)
        var close: ImageView = view.findViewById(R.id.imgClose)
        var rel: RelativeLayout = view.findViewById(R.id.relSuccess)

        var alertDialog: AlertDialog = alertDial.create()
        alertDialog.show()
        close.setOnClickListener {
            generalInterface.goToOrders()
            sendNotification()
            alertDialog.dismiss()
        }

        rel.setOnClickListener {
            generalInterface.goToOrders()
            sendNotification()
            alertDialog.dismiss()
        }
    }

    private fun getList() {
        var lst: ArrayList<Product> = arguments?.getParcelableArrayList<Product>("CART")!!
        if (lst.size > 0){
            linearCheckOut.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            number.text = lst.size.toString()
            var y: Int = 0
            for (i in lst.indices){
                y += lst[i].price!!
            }
            price.text = y.toString()
            val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
                AppCompatActivity.MODE_PRIVATE
            )!!
            var em = sharedPreferences.getString("EMAIL", "")
            var first = sharedPreferences.getString("FIRSTNAME", "")
            var last = sharedPreferences.getString("LASTNAME", "")
            var id = sharedPreferences.getString("USERID", "")
            var loc = sharedPreferences.getString("LOCATION", "")
            var pn = sharedPreferences.getString("PHONENUMBER", "")

            name.text = first + last
            location.text = loc
            phone.text = pn

            var random = Random.nextInt(10000, 100000)
            orderId.text = "#" + random.toString()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        time = options[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun sendNotification() {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = Date()
        val myDate = simpleDateFormat.format(date)
        val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
            AppCompatActivity.MODE_PRIVATE
        )!!
        var id = sharedPreferences.getString("USERID", "")
        val str: String = "Your order ${orderId.text.toString()} has been received and is being processed and you will be notified on delivery"
        sendNotificationInterface = SendNotificationRetrofit.getRetrofit().create(SendNotificationInterface::class.java)
        val call: Call<AllNotifications> = sendNotificationInterface.sendNotification(id!!, str, myDate)
        call.enqueue(object : Callback<AllNotifications>{
            override fun onResponse(
                call: Call<AllNotifications>,
                response: Response<AllNotifications>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(activity, "Notification Added", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AllNotifications>, t: Throwable) {
                Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}