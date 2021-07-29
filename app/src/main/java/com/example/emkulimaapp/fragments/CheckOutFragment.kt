package com.example.emkulimaapp.fragments

import android.app.VoiceInteractor
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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CheckOutRetrofit
import com.example.emkulimaapp.RetrofitClasses.OrderRetrofit
import com.example.emkulimaapp.RetrofitClasses.SendNotificationRetrofit
import com.example.emkulimaapp.constants.constants
import com.example.emkulimaapp.interfaces.*
import com.example.emkulimaapp.models.*
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
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
    private var jsonArrayRequest: JsonObjectRequest? = null
    private var requestQueue: RequestQueue? = null

    val options = arrayOf("8:30 Am", "10:00 AM", "12:00PM", "4:30PM")

    private var time: String = "8:30 Am"
    private var orderNum: String = "0"

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

        requestQueue = Volley.newRequestQueue(activity)

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
        var order_price = price.text.toString().toInt()
        var delivery_date = date.text.toString()
        val sharedPreferences: SharedPreferences = activity?.getSharedPreferences("USERDETAILS",
            AppCompatActivity.MODE_PRIVATE
        )!!
        var uId = sharedPreferences.getString("USERID", "1")!!
        orderInterface = OrderRetrofit.getRetrofit().create(OrderInterface::class.java)
        val call: Call<AllOrders> = orderInterface.orderProduct(orderNum, order_price, delivery_date, time, uId)
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
        var lst: ArrayList<Product> = arguments?.getParcelableArrayList<Product>("CART")!!
        for (i in lst.indices){
            var jsonObject: JSONObject = JSONObject()
            try {
                jsonObject.put("product_id", lst[i].productId)
                jsonObject.put("farmer_id", lst[i].farmerId!!)
                jsonObject.put("order_id", orderNum)
            }
            catch (e: JSONException){

            }

            jsonArrayRequest = JsonObjectRequest(Request.Method.POST, constants.BASE_URL + "customer/products/checkout", jsonObject,
                {
                    //Toast.makeText(activity, "Done", Toast.LENGTH_LONG).show()
                },
                {
                    //Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                }
            )

            requestQueue!!.add(jsonArrayRequest)
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
            orderNum = random.toString()
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        time = options[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun sendNotification() {
        var notificationSharedPreferences: SharedPreferences = activity?.getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)!!
        var pres: Int = notificationSharedPreferences.getInt("PRESENT", 0)

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
                    pres += 1
                    var notificationSharedPreferences: SharedPreferences = activity?.getSharedPreferences("NOTIFICATIONS", Context.MODE_PRIVATE)!!
                    var editor: SharedPreferences.Editor = notificationSharedPreferences.edit()
                    editor.putInt("PRESENT", pres)
                    editor.apply()
                    //Toast.makeText(activity, prs.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AllNotifications>, t: Throwable) {
                //Toast.makeText(activity, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}