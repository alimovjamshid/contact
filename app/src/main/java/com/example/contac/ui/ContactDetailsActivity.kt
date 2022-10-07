package com.example.contac.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.contac.databinding.ActivityContactDetailsBinding


class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailsBinding

    private var contactName: String? = null; private  var contactNumber:kotlin.String? = null
    private var contactImage :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactName = intent.getStringExtra("name")
        contactNumber = intent.getStringExtra("number")
        contactImage = intent.getStringExtra("image")

        binding.idTvName.text=contactName
        binding.idTvPhone.text=contactNumber
        if(contactImage!=null) {
            binding.idTvContact.setImageResource(contactImage!!.toInt())
        }
        binding.idTvMessage.setOnClickListener {
            sendMessage(contactNumber.toString())
        }

        binding.idTvCall.setOnClickListener {
            makeCall(contactNumber.toString())
        }

    }

    fun sendMessage(contactNumber:String){
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse("sms:$contactNumber"))
        intent.putExtra("sms_body","Enter your message")
        startActivity(intent)
    }

    fun makeCall(contactNumber:String){
        val intent=Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$contactNumber"))
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return
        }
        startActivity(intent)
    }
}
