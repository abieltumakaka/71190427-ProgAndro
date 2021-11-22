package id.ac.ukdw.pertemuan11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MahasiswaAdapter(var listMahasiswa: ArrayList<String>): RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder>() {
    class MahasiswaHolder(val v: View): RecyclerView.ViewHolder(v){

        fun bindView(data: String){
            val txtInfo = v.findViewById<TextView>(R.id.txvInfo)
            txtInfo.setText(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa, parent, false)
        return MahasiswaHolder(view)
    }

    override fun onBindViewHolder(holder:MahasiswaHolder, position: Int) {
        holder.bindView(listMahasiswa.get(position))
    }

    override fun getItemCount(): Int = listMahasiswa.size
}