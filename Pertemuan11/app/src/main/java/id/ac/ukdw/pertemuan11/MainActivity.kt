package id.ac.ukdw.pertemuan11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    data class Mahasiswa(val nim: String, val nama: String, val ipk: String)
    var listMahasiswa = ArrayList<String>()
    var adapter: MahasiswaAdapter? = null
    var firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refreshData()

        val etNama = findViewById<EditText>(R.id.etNama)
        val etNim = findViewById<EditText>(R.id.etNim)
        val etIpk = findViewById<EditText>(R.id.etIpk)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val searchNama = findViewById<EditText>(R.id.searchBar)
        val btnCariNim = findViewById<Button>(R.id.btnCariNim)
        val btnCariNama = findViewById<Button>(R.id.btnCariNama)
        val btnCariIpk = findViewById<Button>(R.id.btnCariIpk)

        btnSimpan.setOnClickListener {
            val data = Mahasiswa(etNim.text.toString(), etNama.text.toString(), etIpk.text.toString())
            firestore.collection("mahasiswa").add(data).addOnSuccessListener {
                Toast.makeText(this,"Data berhasil disimpan!!",Toast.LENGTH_SHORT)
            }.addOnFailureListener {
                Toast.makeText(this,"Data gagal disimpan!!",Toast.LENGTH_SHORT)
            }
            etNama.setText("")
            etIpk.setText("")
            etNim.setText("")
            refreshData()
        }

        btnCariNim.setOnClickListener {
            searchData(searchNama.text.toString(), "nim")
            searchNama.setText("")
        }

        btnCariNama.setOnClickListener {
            searchData(searchNama.text.toString(), "nama")
            searchNama.setText("")
        }

        btnCariIpk.setOnClickListener {
            searchData(searchNama.text.toString(), "ipk")
            searchNama.setText("")
        }

        val rcyMahasiswa = findViewById<RecyclerView>(R.id.rcyMahasiswa)
        rcyMahasiswa.layoutManager = LinearLayoutManager(this)
        adapter = MahasiswaAdapter(listMahasiswa)
        rcyMahasiswa.adapter = adapter
        refreshData()
    }

    fun searchData(a: String, b: String) {
        listMahasiswa.clear()

        firestore.collection("mahasiswa").whereEqualTo(b, a).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    listMahasiswa.add("${document.data}")
                }
            }.addOnFailureListener {
            Toast.makeText(this, "Data yang dicari tidak ada", Toast.LENGTH_LONG)
        }
        adapter?.notifyDataSetChanged()
    }

    fun refreshData() {
        listMahasiswa.clear()

        firestore.collection("mahasiswa").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    listMahasiswa.add("${document.data}")
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Data gagal direload", Toast.LENGTH_LONG)
            }
    }
}