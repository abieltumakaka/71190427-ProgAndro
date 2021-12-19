package id.ac.ukdw.tugasakhir

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var logoutButton = findViewById<Button>(R.id.LogoutButton)
        var tambahBukuButton = findViewById<Button>(R.id.TambahBukutxv)
        var lihatBukuButton = findViewById<Button>(R.id.LihatBukutxv)
        var emailTxv  = findViewById<TextView>(R.id.emailtxv)
        var cariBukuButton = findViewById<Button>(R.id.CariBukutxv)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val firebaseUser = firebaseAuth.currentUser
        val email = firebaseUser?.email
        emailTxv.text = email

        //handle click, logout user
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
        lihatBukuButton.setOnClickListener {
            val toListBuku = Intent(this, ListBukuActivity::class.java)
            startActivity(toListBuku)
        }
        tambahBukuButton.setOnClickListener {
            val toTambahBuku = Intent(this, TambahBukuActivity::class.java)
            startActivity(toTambahBuku)
        }
        cariBukuButton.setOnClickListener {
            val toCariBuku = Intent(this, SearchActivity::class.java)
            startActivity(toCariBuku)
        }


    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            //user not logged in
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}