package com.xfinite.mzaalosdksample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xfinite.mzaaloauth.MzaaloAuth
import com.xfinite.mzaaloauth.MzaaloAuthInitListener
import com.xfinite.mzaaloauth.MzaaloEnvironment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MzaaloAuthInitListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInitialize.setOnClickListener {
            progress.visibility = View.VISIBLE

            //Add to initialize Mzaalo Auth
            MzaaloAuth.init(this, edtPartnerCode.text.toString(), this, getSelectedEnvironment())
        }
    }

    private fun getSelectedEnvironment(): MzaaloEnvironment {
        return when (spinnerEnvironment.selectedItem.toString()) {
            MzaaloEnvironment.STAGING.name -> {
                MzaaloEnvironment.STAGING
            }

            MzaaloEnvironment.PRODUCTION.name -> {
                MzaaloEnvironment.PRODUCTION
            }

            else -> {
                MzaaloEnvironment.STAGING
            }
        }
    }

    override fun onError(error: String) {
        progress.visibility = View.GONE
        Toast.makeText(this,  error, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        progress.visibility = View.GONE
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
