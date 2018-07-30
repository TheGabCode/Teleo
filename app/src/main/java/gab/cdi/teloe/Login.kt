package gab.cdi.teloe

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var fullnameEditText : EditText
    private lateinit var emailEditText : EditText
    private lateinit var phoneNumberEditText : EditText
    private lateinit var usernameEditText : EditText
    private lateinit var submitSignUp : Button
    private lateinit var termsConditionsCheckbox : CheckBox
    private lateinit var updatesCheckbox : CheckBox
    private lateinit var progressBar : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fullnameEditText = findViewById(R.id.fullnameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        submitSignUp = findViewById(R.id.finalizeSignUpButton)
        termsConditionsCheckbox = findViewById(R.id.termsCheckbox)
        updatesCheckbox = findViewById(R.id.updatesCheckbox)
        termsConditionsCheckbox.setTypeface(ResourcesCompat.getFont(applicationContext,R.font.roboto_light))
        updatesCheckbox.setTypeface(ResourcesCompat.getFont(applicationContext,R.font.roboto_light))
        submitSignUp.setOnClickListener (View.OnClickListener {
           signUp()
        })



    }

    private fun signUp(){
        val firebaseAuth = FirebaseAuth.getInstance()
        val fullname : String = fullnameEditText.text.toString().trim()
        val username : String = usernameEditText.text.toString().trim()
        val phoneNumber : String = phoneNumberEditText.text.toString().trim()
        val email : String = emailEditText.text.toString().trim()

        if(TextUtils.isEmpty(fullname)) {
            Toast.makeText(applicationContext,"Please fill in full name",Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(username)) {
            Toast.makeText(applicationContext,"Please fill in username",Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(applicationContext,"Please fill in phone number",Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext,"Please fill in email",Toast.LENGTH_SHORT).show()
            return
        }
        if(!termsConditionsCheckbox.isChecked) {
            Toast.makeText(applicationContext,"You have not yet agreed to the terms and conditions",Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(fullname,username ,email,phoneNumber,Settings(0,0))
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Registering...")
        progressBar.setCancelable(false)
        progressBar.show()
        firebaseAuth.createUserWithEmailAndPassword(email,phoneNumber).addOnCompleteListener{
            task : Task<AuthResult> ->
                if(task.isSuccessful){

                    var id = firebaseAuth.currentUser?.uid
                    var usersReference = FirebaseDatabase.getInstance().getReference("users")
                    usersReference.child(id).setValue(newUser)
                    Log.d("Tag ",newUser.toString())
                    var intent = Intent(this,Home::class.java)
                    intent.putExtra("fromSignup",true)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    progressBar.dismiss()
                }
                else{
                    Toast.makeText(applicationContext,task.exception.toString(),Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }
        }
    }
}
