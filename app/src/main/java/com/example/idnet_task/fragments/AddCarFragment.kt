package com.example.idnet_task.fragments


import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.idnet_task.Counter
import com.example.idnet_task.Extensions.toast
import com.example.idnet_task.R
import com.example.idnet_task.databinding.AddCarFragmentBinding
import com.example.idnet_task.model.Cars
import com.example.idnet_task.retrofit.RetrofitClient
import com.example.idnet_task.retrofit.RetrofitService
import kotlinx.android.synthetic.main.add_car_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCarFragment : Fragment(R.layout.add_car_fragment) {
    private lateinit var binding: AddCarFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddCarFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        binding.addCar.setOnClickListener {

            if (binding.carImageURL.text.trim().toString() == "") {
                toast("შეიყვანეთ ავტომობილის სურათის ბმული")

            } else post()
            Counter.listSize += 1

        }
        chooseFromContact()


    }


    private fun post() {

        val client: RetrofitService =
            RetrofitClient.getInstance().create(RetrofitService::class.java)

        val call1: Call<Cars> = client.postCar(
            Cars(
                Counter.listSize + 1,
                binding.carImageURL.text.toString(),
                binding.carManufacturers.selectedItem.toString(),
                binding.editName.text.toString(),
                binding.putDescription.text.toString()
            )
        )
        call1.enqueue(object : Callback<Cars> {

            override fun onResponse(call: Call<Cars>, response: Response<Cars>) {
                toast("წარმატებით დაემატა")
            }

            override fun onFailure(call: Call<Cars>, t: Throwable) {
                toast("დამატება ვერ მოხერხდა")
            }
        })

    }

    private fun chooseFromContact() {


        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    val contactUri = intent?.data ?: return@registerForActivityResult
                    val cols = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val rs: Cursor? =
                        activity?.contentResolver?.query(contactUri, cols, null, null, null)
                    if (rs?.moveToFirst()!!) {
                        binding.editName.setText(rs.getString(0))
                    }
                    rs.close()
                }


            }
        binding.editName.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startForResult.launch(i)
        }
    }


}