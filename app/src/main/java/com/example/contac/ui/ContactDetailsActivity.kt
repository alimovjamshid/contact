package com.example.contac.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.contac.databinding.ActivityContactDetailsBinding


class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailsBinding
    private lateinit var context: Context
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
            sendMessage()
        }

        binding.idTvCall.setOnClickListener {
            makeCall(contactNumber.toString())
        }

    }

    private fun sendMessage(){
        val i=Intent(applicationContext,SmsActivity::class.java)
        //val intent=Intent(Intent.ACTION_VIEW, Uri.parse("sms:$contactNumber"))
        i.putExtra("number",contactNumber)
        i.putExtra("name",contactName)
        i.putExtra("image",contactImage)
        startActivity(i,Bundle())
    }

    private fun makeCall(contactNumber:String){
        val intent=Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$contactNumber")
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return
        }
        startActivity(intent)
    }
}
