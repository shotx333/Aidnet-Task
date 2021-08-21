package com.example.idnet_task.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.idnet_task.Counter
import com.example.idnet_task.R
import com.example.idnet_task.databinding.CarsBinding
import com.example.idnet_task.fragments.EditCarFragment
import com.example.idnet_task.model.Cars
import com.example.idnet_task.retrofit.RetrofitClient
import com.example.idnet_task.retrofit.RetrofitService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CarsAdapter(val context: Context) : RecyclerView.Adapter<CarsAdapter.Holder>() {
    private var list: ArrayList<Cars> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        CarsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()

    }

    override fun getItemCount(): Int {
        return list.size

    }

    fun updateAdapter(newList: ArrayList<Cars>) {
        list = newList
        notifyDataSetChanged()
    }


    inner class Holder(private val itemBinding: CarsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private lateinit var model: Cars
        fun bind() {
            model = list[adapterPosition]
            Picasso.get().load(model.carImage).into(itemBinding.imageView)
            itemBinding.getManufacturer.text = model.carName
            itemBinding.owner.text = model.ownerName
            itemBinding.description.text = model.descriptionText


            itemBinding.editButton.setOnClickListener {

                val bundle = Bundle()
                bundle.putInt("Position", adapterPosition)
                val newFragment: Fragment = EditCarFragment()
                newFragment.arguments = bundle
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.displayCars, newFragment).addToBackStack(null).commit()


            }

            itemBinding.deleteButton.setOnClickListener {


                val client: RetrofitService =
                    RetrofitClient.getInstance().create(RetrofitService::class.java)
                val call1: Call<Unit> = client.deleteCar(model.id)

                call1.enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        Log.i("success", response.toString())
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.i("error", t.toString())
                    }
                })
                list.removeAt(adapterPosition)
                notifyDataSetChanged()
                Counter.listSize -= 1
            }


        }

    }
}