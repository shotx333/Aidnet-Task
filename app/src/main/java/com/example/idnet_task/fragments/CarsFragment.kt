package com.example.idnet_task.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.idnet_task.Counter
import com.example.idnet_task.adapter.CarsAdapter
import com.example.idnet_task.databinding.FragmentCarsBinding
import com.example.idnet_task.model.Cars
import com.example.idnet_task.retrofit.RetrofitClient
import com.example.idnet_task.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CarsFragment : Fragment(com.example.idnet_task.R.layout.fragment_cars) {
    private lateinit var carsAdapter: CarsAdapter
    private lateinit var binding: FragmentCarsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        container?.removeAllViews()
        binding = FragmentCarsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getFeed()


    }

    private fun setupRecyclerView() = binding.carRecyclerView.apply {
        layoutManager = LinearLayoutManager(requireContext())
        carsAdapter = CarsAdapter(requireActivity())
        adapter = carsAdapter
    }

    private fun getFeed() {
        val client: RetrofitService =
            RetrofitClient.getInstance().create(RetrofitService::class.java)
        val call: Call<List<Cars>> = client.getPosts()




        call.enqueue(object : Callback<List<Cars>> {
            override fun onResponse(call: Call<List<Cars>>, response: Response<List<Cars>>) {
                val cars: ArrayList<Cars>? = response.body() as ArrayList<Cars>?
                cars?.let { carsAdapter.updateAdapter(it) }
                Counter.listSize = cars?.size!!
                Log.i("counting", carsAdapter.itemCount.toString())

            }

            override fun onFailure(call: Call<List<Cars>>, t: Throwable) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_LONG).show()
            }
        })


    }


}
