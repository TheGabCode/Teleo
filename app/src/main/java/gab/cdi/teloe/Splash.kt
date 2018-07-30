package gab.cdi.teloe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {
    private var firebaseUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
        var startIntent : Intent? = null
        if(firebaseUser != null){
            startIntent = Intent(applicationContext,Home::class.java)
            startActivity(startIntent)
        }
        else{
            startIntent = Intent(applicationContext,SignIn::class.java)
            startActivity(startIntent)
        }
        finish()
    }
}
