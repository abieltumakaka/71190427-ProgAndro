package id.ac.ukdw.tugasakhir

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class BukuAdapter(var listBuku: ArrayList<Buku>, var context: Context): RecyclerView.Adapter<BukuAdapter.BukuHolder>(){
    class BukuHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(buku: Buku, context: Context, position: Int) {
            var firestore = FirebaseFirestore.getInstance()
            view.findViewById<TextView>(R.id.txtJudul).setText(buku.namaBuku)
            view.findViewById<TextView>(R.id.txtPengarang).setText(buku.pengarang)
            view.findViewById<TextView>(R.id.txtIsbn).setText(buku.isbn)
            val editBtn = view.findViewById<Button>(R.id.editBtn)
            val deleteBtn = view.findViewById<Button>(R.id.deleteBtn)

            editBtn.setOnClickListener {
                val intent = Intent(context, EditBukuActivity::class.java)
                intent.putExtra("namaBuku", buku.namaBuku)
                intent.putExtra("deskripsi", buku.deskripsi)
                intent.putExtra("isbn", buku.isbn)
                intent.putExtra("pengarang", buku.pengarang)
                context.startActivity(intent)
                (context as ListBukuActivity).finish()
            }

            deleteBtn.setOnClickListener {
                firestore.collection("buku")
                    .whereEqualTo("isbn", buku.isbn)
                    .get()
                    .addOnSuccessListener {
                        for(document in it) {
                            firestore.collection("buku").document(document.id)
                                .delete()
                        }
                        Toast.makeText(context,"Data Berhasil dihapus!!",Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, ListBukuActivity::class.java))
                    }.addOnFailureListener {
                        Toast.makeText(context, "Data gagal dihapus!!", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_buku, parent, false)
        return BukuHolder(v)
    }

    override fun onBindViewHolder(holder: BukuHolder, position: Int) {
        holder.bind(listBuku[position], context, position)
    }

    override fun getItemCount(): Int {
        return listBuku.size
    }
}