package id.ac.ukdw.tugasakhir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ListBukuActivity : AppCompatActivity() {
    val listBuku = arrayListOf<Buku>()
    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listbuku)
        refreshData()
    }

    fun createView() {
        val rcyContact = findViewById<RecyclerView>(R.id.rcyBuku)
        rcyContact.layoutManager = LinearLayoutManager(this)
        val adapter = BukuAdapter(listBuku, this)
        rcyContact.adapter = adapter
    }

    fun refreshData() {
        firestore.collection("buku").get()
            .addOnSuccessListener {documents ->
                for(document in documents) {
                    listBuku.add(Buku("${document.data["isbn"]}", "${document.data["namaBuku"]}", "${document.data["pengarang"]}", "${document.data["deskripsi"]}"))
                }
                createView()
            }.addOnFailureListener {
                Toast.makeText(this, "Data gagal direload", Toast.LENGTH_LONG)
            }

    }
}