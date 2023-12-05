package tn.gsi.studybee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tn.gsi.studybee.frames.FoldersFrame


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_activity)
        // injection de frame dans la boite (fragment container)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, FoldersFrame())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}