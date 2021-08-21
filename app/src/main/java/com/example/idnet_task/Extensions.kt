package com.example.idnet_task

import android.widget.Toast
import androidx.fragment.app.Fragment

object Extensions {
    fun Fragment.toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}