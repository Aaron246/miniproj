package com.miniproj.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.miniproj.R

class ItemAdapter(private val patientList: ArrayList<String>): RecyclerView.Adapter<ItemAdapter.PatientViewHolder>(){

    var onItemClick: ((String) -> Unit)? = null

    class PatientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.tv_patient_row)
        val cvPatient: CardView = itemView.findViewById(R.id.cv_patient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.patient_list_item, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patientList[position]
        holder.textView.text = patient

        holder.cvPatient.setOnClickListener {
            onItemClick?.invoke(patient)
        }
    }

    override fun getItemCount(): Int {
        return patientList.size
    }
}