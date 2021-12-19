package id.ac.ukdw.tugasakhir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class TambahBukuActivity : AppCompatActivity() {
    var firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_buku)
        val settings = FirebaseFirestoreSettings.Builder().setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED).build()
        firestore.firestoreSettings = settings

        val nama = findViewById<EditText>(R.id.namaTxv)
        val pengarang = findViewById<EditText>(R.id.pengarangTxv)
        val isbn = findViewById<EditText>(R.id.isbnTxv)
        val deskripsi = findViewById<EditText>(R.id.deskripsiTxv)
        val submit = findViewById<Button>(R.id.sumbitBtn)

        submit.setOnClickListener {
            val data = Buku(isbn.text.toString(), nama.text.toString(), pengarang.text.toString(), deskripsi.text.toString())
            firestore.collection("buku").add(data).addOnSuccessListener {
                Toast.makeText(this, "Data berhasil disimpan!!", Toast.LENGTH_SHORT).show()
                nama.setText("")
                pengarang.setText("")
                isbn.setText("")
                deskripsi.setText("")
            }.addOnFailureListener {
                Toast.makeText(this, "Data gagal disimpan!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}