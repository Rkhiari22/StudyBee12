package tn.gsi.studybee

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import tn.gsi.studybee.databinding.ProfileBinding
import java.text.DateFormat
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ProfileBinding
    var imageURL: String? = null
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.profilepic.setImageURI(uri)
            } else {
                Toast.makeText(this@ProfileActivity, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.profilepic.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.savebutton.setOnClickListener {
            saveData()
            val intent = Intent(this@ProfileActivity, SigninActivity::class.java)
            startActivity(intent)
        }

    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().reference.child("Profile pictures")
            .child(uri!!.lastPathSegment!!)
        val builder = AlertDialog.Builder(this@ProfileActivity)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener {
            dialog.dismiss()
        }

    }

    private fun uploadData() {
        val name = binding.name.text.toString()
        val establishment = binding.establishment.text.toString()
        val field = binding.fields.text.toString()
        val number = binding.contactn.text.toString()

        val profileData = ProfileData(name, establishment, field, number, imageURL)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Profiles").child(currentDate)
            .setValue(profileData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                    val intent = Intent(this, SigninActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener { e->
                Toast.makeText(this@ProfileActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}