package gab.cdi.teloe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var facebookSignInButton : Button
    private lateinit var signUpButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        facebookSignInButton = findViewById(R.id.facebookLoginButton)
        signUpButton = findViewById(R.id.signUpButton)
        signUpButton.setOnClickListener {
            _ ->
                startActivity(Intent(this,Login::class.java))
        }
        facebookSignInButton.setOnClickListener{
            _ ->
                startActivity(Intent(this,FacebookLogin::class.java))
        }
    }
}
