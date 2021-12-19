package id.ac.ukdw.tugasakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    //constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val buttonLogin = findViewById<com.google.android.gms.common.SignInButton>(R.id.googleSignInBtn)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@Login, gso)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        //Google SignIn Button, Click to begin Google SignIn
        buttonLogin.setOnClickListener {
            Log.d(TAG, "onCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser!=null) {
            startActivity(Intent(this@Login, DashboardActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google SignIn success, now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e: Exception) {
                //failed Google SignIn
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //Login Success
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                //get loggedIn user
                val firebaseUser = firebaseAuth.currentUser
                //get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                //check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {
                    //user is new - Account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... \n$email")
                    Toast.makeText(this@Login, "Account created... \n$email", Toast.LENGTH_SHORT).show()
                }
                else {
                    //existing user - LoggedIn
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user... \n$email")
                    Toast.makeText(this@Login, "LoggedIn... \n$email", Toast.LENGTH_SHORT).show()
                }

                //start profile activity
                startActivity(Intent(this@Login, DashboardActivity::class.java))
            }
            .addOnFailureListener {e ->
                //login failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Login Failed due to ${e.message}")
                Toast.makeText(this@Login, "Login Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}