package com.example.idnet_task.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.idnet_task.Extensions.toast
import com.example.idnet_task.R
import com.example.idnet_task.databinding.FragmentCarEditBinding
import com.example.idnet_task.model.Cars
import com.example.idnet_task.retrofit.RetrofitClient
import com.example.idnet_task.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditCarFragment : Fragment(R.layout.fragment_car_edit) {


    private lateinit var binding: FragmentCarEditBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        container?.removeAllViews()
        binding = FragmentCarEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editCarEdit.setOnClickListener {

            if (binding.carImageURLEdit.text.trim().toString() == "") {
                toast("შეიყვანეთ ავტომობილის სურათის ბმული")

            } else {
                val carsFragment: Fragment = CarsFragment()
                updateData()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.editCarsFragment, carsFragment)?.addToBackStack(null)?.commit()
            }
        }
    }

    private fun updateData() {
        val bundleArgs = arguments

        val client: RetrofitService =
            RetrofitClient.getInstance().create(RetrofitService::class.java)

        val call: Call<Cars> = client.updateCar(
            bundleArgs?.getInt("Position")?.plus(1),
            binding.carImageURLEdit.text.toString(),
            binding.carManufacturersEdit.selectedItem.toString(),
            binding.putDescriptionEdit.text.toString()
        )
        call.enqueue(object : Callback<Cars> {


            override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                toast("წარმატებით განახლდა")
            }

            override fun onFailure(call: Call<Cars>, t: Throwable) {
                Log.i("updateFailure", t.toString())
            }
        })

    }
}