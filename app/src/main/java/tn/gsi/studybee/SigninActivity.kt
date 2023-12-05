package tn.gsi.studybee

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import tn.gsi.studybee.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySigninBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.button1.setOnClickListener{
            val email = binding.signinEmail.text.toString()
            val password = binding.signInPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                   if (it.isSuccessful){
                       val intent = Intent(this, MainActivity::class.java)
                       startActivity(intent)
                   } else {
                      Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                   }
                }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.signupRedirect.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupIntent)
        }
    }
}