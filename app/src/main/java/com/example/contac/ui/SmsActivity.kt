package com.example.contac.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import com.example.contac.databinding.ActivitySmsBinding

@Suppress("DEPRECATION")
class SmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsBinding
    private var contactName: String? = null; private  var contactNumber:String? = null
    private var contactImage :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contactName = intent.getStringExtra("name")
        contactNumber = intent.getStringExtra("number")
        contactImage = intent.getStringExtra("image")
        
        binding.textView.text=contactName
        if(contactImage!=null) {
            binding.imageView.setImageResource(contactImage!!.toInt())
        }
        
        binding.namesms.setOnClickListener { 
            val i=Intent(applicationContext,MainActivity::class.java)
            startActivity(i)
        }
        
        binding.send.setOnClickListener { 
            try {
                val smsManager :SmsManager = SmsManager.getDefault() as SmsManager
                smsManager.sendTextMessage(contactNumber.toString().trim(),null,binding.editSms.text.toString(),null,null)
                Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_SHORT).show()
            }
            catch (ex:java.lang.Exception){
                Toast.makeText(applicationContext, "Please enter all the data.."+ex.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}
