package id.ac.ukdw.tugasakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EditBukuActivity : AppCompatActivity() {
    var firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_buku)

        val nama = findViewById<EditText>(R.id.namaTxvT)
        val pengarang = findViewById<EditText>(R.id.pengarangTxvT)
        val isbn = findViewById<EditText>(R.id.isbnTxvT)
        val deskripsi = findViewById<EditText>(R.id.deskripsiTxvT)
        val submit = findViewById<Button>(R.id.sumbitBtnT)

        nama.setText("${getIntent().getStringExtra("namaBuku")}")
        pengarang.setText("${getIntent().getStringExtra("pengarang")}")
        isbn.setText("${getIntent().getStringExtra("isbn")}")
        deskripsi.setText("${getIntent().getStringExtra("deskripsi")}")
//
        submit.setOnClickListener {
            val data = Buku(isbn.text.toString(), nama.text.toString(), pengarang.text.toString(), deskripsi.text.toString())
            firestore.collection("buku")
                .whereEqualTo("isbn", getIntent().getStringExtra("isbn"))
                .get()
                .addOnSuccessListener {
                    for(document in it) {
                        firestore.collection("buku").document(document.id)
                            .update("isbn", isbn.text.toString(), "namaBuku", nama.text.toString(), "pengarang", pengarang.text.toString(), "deskripsi", deskripsi.text.toString())
                    }
                Toast.makeText(this@EditBukuActivity, "Data berhasil diudpate!!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ListBukuActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this@EditBukuActivity, "Data gagal diudpate!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}