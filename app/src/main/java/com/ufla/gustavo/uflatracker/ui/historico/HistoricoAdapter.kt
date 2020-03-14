package com.ufla.gustavo.uflatracker.ui.historico

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ufla.gustavo.uflatracker.R
import com.ufla.gustavo.uflatracker.entity.Atividade
import com.ufla.gustavo.uflatracker.ui.visualizacao.VisualizacaoActivity
import kotlinx.android.synthetic.main.historico_item.view.*
import java.text.SimpleDateFormat


class HistoricoAdapter(private val context: Context, private var listaAtividade: List<Atividade>):
    RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.historico_item, parent, false)

        return HistoricoViewHolder(view)
    }

    private fun configClickListener(holder: HistoricoViewHolder, position: Int){
        holder.layout.setOnClickListener {
            var intent = Intent(context, VisualizacaoActivity::class.java)
            intent.putExtra("atividade", listaAtividade[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listaAtividade.size

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        holder.bindView(listaAtividade[position])
        configClickListener(holder, position)
    }

    class HistoricoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val textViewNome = itemView.label_nome_atividade
        val textViewData = itemView.label_data_historico
        val layout = itemView.layout_recycle_historico
        val tempoHistorico = itemView.tempo_atividade_historico

        @SuppressLint("SimpleDateFormat")
        fun bindView(atividade: Atividade) {
            textViewNome.text = atividade.nome
            val pattern = "dd/MM/yyyy HH:mm"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date = simpleDateFormat.format(atividade.dataCriacao?.time)
            textViewData.text = date
            tempoHistorico.text = atividade.tempoAtividade
        }
    }
}