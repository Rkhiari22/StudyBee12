package tn.gsi.studybee

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoadingpageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loadingpage)

        val signinIntent = Intent(this, SigninActivity::class.java)
        startActivity(signinIntent)
    }
}