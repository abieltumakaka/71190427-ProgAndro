package id.ac.ukdw.tugasakhir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchActivity : AppCompatActivity() {
    val listBuku = arrayListOf<Buku>()
    val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchTxv = findViewById<EditText>(R.id.searchTxv)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        searchBtn.setOnClickListener {
            listBuku.clear()
            firestore.collection("buku").whereEqualTo("namaBuku", searchTxv.text.toString()).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        listBuku.add(
                            Buku(
                                "${document.data["isbn"]}",
                                "${document.data["namaBuku"]}",
                                "${document.data["pengarang"]}",
                                "${document.data["deskripsi"]}"
                            )
                        )
                    }
                    createView()
                }.addOnFailureListener {
                    Toast.makeText(this, "Data gagal direload", Toast.LENGTH_LONG)
                }
        }
    }

    fun createView() {
        val rcyContact = findViewById<RecyclerView>(R.id.rcyBuku)
        rcyContact.layoutManager = LinearLayoutManager(this)
        val adapter = BukuAdapter(listBuku, this)
        rcyContact.adapter = adapter
    }
}