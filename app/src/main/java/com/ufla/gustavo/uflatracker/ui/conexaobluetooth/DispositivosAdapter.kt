package com.ufla.gustavo.uflatracker.ui.conexaobluetooth

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.nio.file.Files.size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.ufla.gustavo.uflatracker.R


class DispositivosAdapter(
    internal var mctx: Context,
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

        viewHolder.viewNome.text = dispositivo.name
        viewHolder.viewEndereco.text = dispositivo.address
        viewHolder.layout.setOnClickListener {
            funcao(dispositivo)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class DispositivosViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var viewNome: TextView
        var viewEndereco: TextView
        var layout : ConstraintLayout
        init {
            viewNome = itemView.findViewById(R.id.conteudo_nome)
            viewEndereco = itemView.findViewById(R.id.conteudo_endereco)
            layout = itemView.findViewById(R.id.layout_recycle_dispositivo)
        }
    }
}