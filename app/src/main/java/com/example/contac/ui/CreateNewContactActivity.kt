package com.example.contac.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextUtils
import android.widget.Toast
import com.example.contac.databinding.ActivityCreateNewContactBinding

class CreateNewContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCreateNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.idbtnAddContact.setOnClickListener {
            val name=binding.idedtName.text
            val phone=binding.idedtPhoneNumber.text
            val email=binding.idedtEmail.text

            if(TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)){
                Toast.makeText(this, "Please enter the data in all fields", Toast.LENGTH_SHORT).show()
            }
            else{
                addConatct(name,email,phone)
            }
        }

    }
    fun addConatct(name: Editable, email: Editable, phone: Editable){
        val intent=Intent(ContactsContract.Intents.Insert.ACTION)
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
        intent.putExtra(ContactsContract.Intents.Insert.NAME,name)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,phone)
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL,email)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==1){
            Toast.makeText(this, "Contacts has been addded", Toast.LENGTH_SHORT).show()
            val i=Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show()
        }
    }
}