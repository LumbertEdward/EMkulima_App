package com.example.emkulimaapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.Adapter
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.emkulimaapp.R
import com.example.emkulimaapp.RetrofitClasses.DeleteCartItemRetrofit
import com.example.emkulimaapp.interfaces.DeleteCartItemInterface
import com.example.emkulimaapp.models.Cart
import com.example.emkulimaapp.models.message
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class SwipeToDeleteCallBack(val context: Context, val adapter: CartAdapter, val snack: RelativeLayout): ItemTouchHelper.Callback() {

    private val colorDrawable: ColorDrawable = ColorDrawable()
    private var paint: Paint = Paint()
    private val backgroundColor = Color.parseColor("#03B203")
    private val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)!!
    private val intrinsicWidth: Int = drawable.intrinsicWidth
    private val intrinsicHeight: Int = drawable.intrinsicHeight

    init {
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        drawable.setTint(Color.WHITE)
    }

    private lateinit var deleteCartItemInterface: DeleteCartItemInterface



    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val view: View = viewHolder.itemView
        val itemHeight = view.height

        val isCancelled: Boolean = dX.equals(0) && !isCurrentlyActive

        if (isCancelled){
            clearCanvas(c, view.right + dX, view.top.toFloat(), view.right.toFloat(), view.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        colorDrawable.color = backgroundColor
        colorDrawable.setBounds(view.right + dX.toInt(), view.top, view.right, view.bottom)
        colorDrawable.draw(c)

        val deleteIconTop: Int = view.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin: Int = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft: Int = view.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight: Int = view.right - deleteIconMargin
        val deleteIconBottom: Int = deleteIconTop + intrinsicHeight

        drawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        drawable.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float){
        c.drawRect(left, top, right, bottom, paint)
    }

    @SuppressLint("ResourceAsColor")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.adapterPosition
        val item = adapter.getItem(position)
        adapter.deleteItem(position)
        var sharedPreferences: SharedPreferences = context.getSharedPreferences("USERDETAILS", Context.MODE_PRIVATE)!!
        val userId = sharedPreferences.getString("USERID", "1").toString()

        deleteCartItemInterface = DeleteCartItemRetrofit.getRetrofit().create(DeleteCartItemInterface::class.java)
        val call: Call<message> = deleteCartItemInterface.deleteItem(userId, item.cartId!!)
        call.enqueue(object : Callback<message>{
            override fun onResponse(call: Call<message>, response: Response<message>) {
                if (response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<message>, t: Throwable) {
                Toast.makeText(context, "Check Network Connection", Toast.LENGTH_LONG).show()
            }

        })

        val snackBar: Snackbar = Snackbar.make(snack, "Item Deleted from list", Snackbar.LENGTH_LONG)
        snackBar.setAction("UNDO", View.OnClickListener {
            adapter.redoItem(position, item)
            val snackBar1: Snackbar = Snackbar.make(snack, "Item Restored", Snackbar.LENGTH_LONG)
            snackBar1.setBackgroundTint(Color.WHITE)
            snackBar1.setActionTextColor(Color.GREEN)
            snackBar1.show()
        })
        snackBar.setActionTextColor(Color.GREEN)
        snackBar.setBackgroundTint(Color.WHITE)
        val view: View = snackBar.view
        val txtView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
        txtView.setTextColor(Color.GREEN)
        snackBar.show()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return super.getSwipeThreshold(viewHolder)
    }
}