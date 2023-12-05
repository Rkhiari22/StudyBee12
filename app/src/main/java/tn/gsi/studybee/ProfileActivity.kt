package tn.gsi.studybee

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import tn.gsi.studybee.databinding.ProfileBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ProfileBinding
    private var imageURL: String? = null
    private var uri: Uri? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val GALLERY = 1
    private val CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check for camera permission before launching the camera
        if (checkCameraPermission()) {
            binding.profilepic.setOnClickListener {
                // Launch the picture dialog for image selection
                showPictureDialog()
            }

            binding.savebutton.setOnClickListener {
                saveData()
                val intent = Intent(this@ProfileActivity, SigninActivity::class.java)
                startActivity(intent)
            }
        } else {
            requestCameraPermission()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
                1 -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA)
                }
            }
        }
        pictureDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    uri = getImageUri(bitmap)
                    Toast.makeText(this@ProfileActivity, "Image Show!", Toast.LENGTH_SHORT).show()
                    binding.profilepic.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@ProfileActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            val thumbnail = data?.extras?.get("data") as Bitmap
            uri = getImageUri(thumbnail)
            binding.profilepic.setImageBitmap(thumbnail)
            Toast.makeText(this@ProfileActivity, "Photo Show!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun saveData() {
        if (uri != null) {
            val storageReference = FirebaseStorage.getInstance().reference.child("Profile pictures")
                .child(uri!!.lastPathSegment!!)
            val builder = AlertDialog.Builder(this@ProfileActivity)
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()

            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                // Get the download URL
                storageReference.downloadUrl.addOnCompleteListener { uriTask ->
                    if (uriTask.isSuccessful) {
                        val urlImage = uriTask.result
                        imageURL = urlImage.toString()
                        uploadData()
                    } else {
                        // Handle failure to get the download URL
                        dialog.dismiss()
                        Toast.makeText(this@ProfileActivity, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                dialog.dismiss()
                // Handle failure to upload
                Toast.makeText(this@ProfileActivity, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@ProfileActivity, "No image selected", Toast.LENGTH_SHORT).show()
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
