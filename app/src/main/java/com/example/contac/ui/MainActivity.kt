package com.example.contac.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contac.R
import com.example.contac.adapter.ContactsRVAdapter
import com.example.contac.databinding.ActivityMainBinding
import com.example.contac.modal.ContactsModal
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayList: ArrayList<ContactsModal>
    private lateinit var contactRv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        contactRv=findViewById(R.id.idRVContacts)
        arrayList = ArrayList()

        prepareContactRV()

        requistPermission()

        binding.idFABadd.setOnClickListener {
            val i=Intent(this, CreateNewContactActivity::class.java)
            startActivity(i)
        }
    }

    fun filter(text:String){
        val filteredlist= ArrayList<ContactsModal>()
        for(i in arrayList){
            if(i.contactName.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))){
                filteredlist.add(i)
            }
        }
        if(filteredlist.isEmpty()){
            Toast.makeText(applicationContext, "No Contact Found", Toast.LENGTH_SHORT).show()
        }
        else{
            // shu joyini togirlash kerak
        }
    }

    private fun requistPermission(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CONTACTS
            )
            .withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if(p0!!.areAllPermissionsGranted()){
                        getContacts()
                        Toast.makeText(applicationContext, "All the permission are granted", Toast.LENGTH_SHORT).show()
                    }
                    if (p0.isAnyPermissionPermanentlyDenied){
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show()
            }.onSameThread().check()
    }

    fun showSettingsDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle("Need Permissions")

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS"
        ) { dialog, _ -> // this method is called on click on positive
            dialog.cancel()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        }
        builder.setNegativeButton("Cancel"
        ) { dialog, _ -> // this method is called when
            // user click on negative button.
            dialog.cancel()
        }
        builder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchItem?.actionView as SearchView


        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText!!.lowercase(Locale.getDefault()))
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun prepareContactRV(){
        val adaptercontacts = ContactsRVAdapter(this,arrayList)
        contactRv.layoutManager=LinearLayoutManager(this)
        binding.idRVContacts.adapter=adaptercontacts
    }

    @SuppressLint("Range", "NotifyDataSetChanged")
    fun getContacts(){
        var contactId: String
        var displayName: String

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if(cursor!!.count>0){
            while (cursor.moveToNext()){
                val hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        .toInt()
                if (hasPhoneNumber > 0) {
                    contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    displayName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneCursor: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(contactId),
                        null
                    )
                    if (phoneCursor!!.moveToNext()) {
                        val phoneNumber: String =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        arrayList.add(ContactsModal(displayName, phoneNumber, R.drawable.ic_person))
                    }
                    phoneCursor.close()
                }
            }
        }

        cursor.close()

        binding.idPBLoading.visibility=View.GONE
        binding.idRVContacts.adapter?.notifyDataSetChanged()
    }
}
