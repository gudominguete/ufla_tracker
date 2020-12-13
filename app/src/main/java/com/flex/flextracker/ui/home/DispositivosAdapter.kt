package com.flex.flextracker.ui.home

import android.bluetooth.BluetoothDevice
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.flex.flextracker.R


class DispositivosAdapter(
    private val mList: List<BluetoothDevice>,
    val funcao: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DispositivosAdapter.DispositivosViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DispositivosViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycle_item_dispositivos, viewGroup, false)
        return DispositivosViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: DispositivosViewHolder, i: Int) {
        val dispositivo = mList[i]
        if(dispositivo.name.isNullOrEmpty()){
            viewHolder.viewNome.text = "Sem identificação"
        } else {
            viewHolder.viewNome.text = dispositivo.name
        }
        viewHolder.viewEndereco.text = dispositivo.address
        viewHolder.layout.setOnClickListener {
            funcao(dispositivo)
        }
        viewHolder.textConectar.setOnClickListener {
            funcao(dispositivo)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class DispositivosViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var textConectar: Button
        var viewNome: TextView
        var viewEndereco: TextView
        var layout : ConstraintLayout
        init {
            textConectar = itemView.findViewById(R.id.text_conectar)
            viewNome = itemView.findViewById(R.id.conteudo_nome)
            viewEndereco = itemView.findViewById(R.id.conteudo_endereco)
            layout = itemView.findViewById(R.id.layout_recycle_dispositivos)
        }
    }
}