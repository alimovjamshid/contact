package com.example.contac.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contac.modal.ContactsModal
import com.example.contac.R
import com.example.contac.ui.ContactDetailsActivity

class ContactsRVAdapter(private val context: Context, private var arraylist:List<ContactsModal>,
                        )
    :RecyclerView.Adapter<ContactsRVAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val name:TextView=itemView.findViewById(R.id.idContactName)
        val image:ImageView=itemView.findViewById(R.id.idContactImage)
        val number:TextView=itemView.findViewById(R.id.idContactNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.contacts_rv_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val modal = arraylist[position]
        holder.name.text=modal.contactName
        holder.image.setImageResource(modal.contactImage)
        holder.number.text=modal.contactNumber

        holder.itemView.setOnClickListener {
            val intent=Intent(context, ContactDetailsActivity::class.java)
            intent.putExtra("name",modal.contactName)
            intent.putExtra("number",modal.contactNumber)
            intent.putExtra("image",modal.contactImage)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arraylist.count()
    }

}
