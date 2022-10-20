package com.miniproj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.miniproj.R
import com.miniproj.model.RecordBlock
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecordAdapter(private val RecordBlockList: ArrayList<String>): RecyclerView.Adapter<RecordAdapter.MyViewHolder>(){

    var onItemClick: ((String) -> Unit)? = null

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.tv_patient_row)
        val cvPatient: CardView = itemView.findViewById(R.id.cv_patient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.patient_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val block = RecordBlockList[position]
        holder.textView.text = block

        holder.cvPatient.setOnClickListener {
            onItemClick?.invoke(block)
        }
    }

    override fun getItemCount(): Int {
        return RecordBlockList.size
    }
}