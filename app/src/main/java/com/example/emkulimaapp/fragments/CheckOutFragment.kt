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
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemSelected
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.CheckOutRetrofit
import com.example.emkulimaapp.RetrofitClasses.OrderRetrofit
import com.example.emkulimaapp.interfaces.CheckOutInterface
import com.example.emkulimaapp.interfaces.GeneralInterface
import com.example.emkulimaapp.interfaces.OrderInterface
import com.example.emkulimaapp.interfaces.OrderProductsInterface
import com.example.emkulimaapp.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    private lateinit var orderInterface: OrderInterface
    private lateinit var checkOutInterface: CheckOutInterface
    private lateinit var generalInterface: GeneralInterface

    val options = arrayOf("8:30 Am", "10:00 AM", "12:00PM", "4:30PM")

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
        var uId = sharedPreferences.getString("USERID", "1")!!.toInt()
        orderInterface = OrderRetrofit.getRetrofit().create(OrderInterface::class.java)
        val call: Call<AllOrders> = orderInterface.orderProduct(ordId, order_price, delivery_date, uId)
        call.enqueue(object : Callback<AllOrders>{
            override fun onResponse(call: Call<AllOrders>, response: Response<AllOrders>) {
                if (response.isSuccessful){
                    showSuccessDialog()
                    sendOrderProducts()
                }
            }

            override fun onFailure(call: Call<AllOrders>, t: Throwable) {
                Toast.makeText(activity, t.message.toString(), Toast.LENGTH_LONG).show()
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
                        Toast.makeText(activity, "Added", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AllOrderItems>, t: Throwable) {

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
            alertDialog.dismiss()
        }

        rel.setOnClickListener {
            generalInterface.goToOrders()
            alertDialog.dismiss()
        }
    }

    private fun getList() {
        var lst: ArrayList<Cart> = arguments?.getParcelableArrayList<Cart>("CART")!!
        if (lst.size > 0){
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
        Toast.makeText(activity, options[position], Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        generalInterface = context as GeneralInterface
    }
}