package gab.cdi.teloe

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

/**
 * Created by Default on 12/07/2018.
 */

class TeleoUserAuthentication{
    private var firebaseUser : FirebaseUser? = null
    private var userReference : DatabaseReference
    private var userId : String? = ""
    var user : User? = null
    constructor(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().getReference("users")
        userId = firebaseUser?.uid

        if(firebaseUser != null){
            userReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot?) {
                    var fetchedUser = p0?.getValue(User::class.java)
                    getUser(fetchedUser)
                }

                override fun onCancelled(p0: DatabaseError?) {

                }
            })
        }

    }

    fun getUserSettings() : User?{ //temporary no Settings class yet
        return this.user
    }

    private fun getUser(user : User?){
        this.user = user
    }

    fun signInWithEmailAndPassword(email : String, password : String, context : Context, success : BooleanArray){
        var loginAuth = FirebaseAuth.getInstance()
        loginAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            task ->
                if(task.isSuccessful){
                    success[0] = true
                }
                else{
                    Toast.makeText(context,task.exception?.message, Toast.LENGTH_SHORT).show()
                    success[0] = false
                }
        }
    }
}