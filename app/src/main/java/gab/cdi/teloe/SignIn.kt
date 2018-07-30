package gab.cdi.teloe

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class SignIn : AppCompatActivity() {
    private lateinit var emailEditText : EditText
    private lateinit var phoneNumberEditText : EditText
    private lateinit var signInButton : Button
    private lateinit var signUpText : TextView
    private lateinit var progressBar : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        emailEditText = findViewById(R.id.emailSignIn)
        phoneNumberEditText = findViewById(R.id.phoneNumberSignIn)
        signInButton = findViewById(R.id.signInButton)
        signUpText = findViewById(R.id.registerText)
        signInButton.setOnClickListener{ signIn() }
        signUpText.setOnClickListener { registerPage() }
    }

    private fun signIn(){
        var loginAuth = FirebaseAuth.getInstance()
        var email = emailEditText.text.toString().trim()
        var phoneNumber = phoneNumberEditText.text.toString().trim()
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext,"Please fill in email",Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(applicationContext,"Please fill in phone number",Toast.LENGTH_SHORT).show()
            return
        }
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Signing In...")
        progressBar.setCancelable(false)
        progressBar.show()
        loginAuth.signInWithEmailAndPassword(email,phoneNumber).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
                var userId : String = firebaseUser.uid
                if(userId != null)     {
                    fetchUserData(userId)
                }
                var intent = Intent(this,Home::class.java)
                intent.putExtra("fromSignup",false)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                progressBar.dismiss()
            }
            else{
                Log.d("Tag error",task.exception?.message)
                Toast.makeText(applicationContext,task.exception?.message, Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }
        }
    }

    private fun registerPage(){
        var registerIntent = Intent(applicationContext,MainActivity::class.java)
        startActivity(registerIntent)
    }

    private fun fetchUserData(id : String){
        var userReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        userReference.child(id).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                var fetchedUser : User? = p0?.getValue(User::class.java)
                var userSettings : SharedPreferences.Editor = getSharedPreferences("User Settings", Context.MODE_PRIVATE).edit()
                if(fetchedUser != null){

                    userSettings.putInt("preferedRegion",fetchedUser.userSettings.preferedRegion)
                    userSettings.putString("userId",id)
                    userSettings.putInt("themeUsed",fetchedUser.userSettings.themeUsed)
                    userSettings.apply()
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
}
