package com.adl.portalearning

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_content.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.jar.Manifest

class Add_Content_Activity : AppCompatActivity() {

    //actionbar
    private lateinit var actionBar: ActionBar
    //constant to pick video
    private val VIDEO_PICK_GALLERY_CODE = 100
    private val VIDEO_PICK_CAMERA_CODE = 101
    //constant to pick image from gallery
    private val IMAGE_PICK_GALLERY_CODE = 105
    //constant to request camera permission to record video from camera
    private val CAMERA_REQUEST_CODE = 102

    //array for camera request permissions
    private lateinit var cameraPermissions:Array<String>


    //progress bar
    private lateinit var progressDialog:ProgressDialog

    private var videoUri: Uri? = null //uri of picked video
    private var imageUri: Uri? = null // uri of picked image
    private var title:String = "";
    private var description:String = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_content)


        //init actionbar
        actionBar = supportActionBar!!
        //title
        actionBar.title = "Add new Video"
        //add bac button
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        //init progressbar
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Uploading Video...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init camera permission array
        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        //handle click, upload video
        btnSaveAddContent.setOnClickListener({
            //get title
            title = inputJudulVideo.text.toString().trim()
            description = editTextMultiVideoDescription.text.toString().trim()
            if (TextUtils.isEmpty(title)){
                //no title is entered
                Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(description)){
                //no description is entered
                Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show()
            }
            else if (videoUri == null){
                //video is not picked
                Toast.makeText(this, "pick the video first", Toast.LENGTH_SHORT).show()
            }
            else{
                // all data entered
                uploadVideoFirebase()
            }
        })

        //handle click, pick video
        btnUploadPickVideo.setOnClickListener({
            videoPickDialog()
        })

        //handle click, pick thumbnail picture
        btnUploadThumbailVideo.setOnClickListener({
            imagePickDialog()
        })


    }



    private fun uploadVideoFirebase() {
        //show progress
        progressDialog.show()

        //timestamp
        val timestamp = ""+System.currentTimeMillis()

        //file path and name in firebase storage
        val filePathAndName = "Videos/video_$timestamp"
        val imgFilePathAndName = "Image/img_$timestamp"

        //downloaded uri
        var uploadimageUri: Uri? = null // uri of picked image

        //storage refrence
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        val storageReferenceImg = FirebaseStorage.getInstance().getReference(imgFilePathAndName)

        //upload img first
        storageReferenceImg.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                //uploaded, get url or uploaded img
                val uriTaskImg = taskSnapshot.storage.downloadUrl
                while (!uriTaskImg.isSuccessful);
                val downloadUri = uriTaskImg.result
                if(uriTaskImg.isSuccessful){
                    //set with uploaded uri
                    uploadimageUri = downloadUri
                }
            }
            .addOnFailureListener { e ->
                //failed uploading
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

            }
        //upload video using uri of video storage
        storageReference.putFile(videoUri!!)
            .addOnSuccessListener { taskSnapshot ->
                //uploaded, get url of uploaded video
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val downloadUri = uriTask.result
                if(uriTask.isSuccessful){
                    //video url is recieved succesfully

                    //add video details to firebase db
                    val hashMap = HashMap<String, Any>()
                    hashMap["id"] = "$timestamp"
                    hashMap["title"] = "$title"
                    hashMap["description"] = "$description"
                    hashMap["videoUri"] = "$downloadUri"
                    hashMap["imageUri"] = "$uploadimageUri"

                    //put the aboive info to db
                    val dbReference = FirebaseDatabase.getInstance().getReference("Videos")
                    dbReference.child(timestamp)
                        .setValue(hashMap)
                        .addOnSuccessListener {
                            //video info added succesfully
                            progressDialog.dismiss()
                            finish()
                        }
                        .addOnFailureListener{ e ->
                            //video info failed added
                            progressDialog.dismiss()
                            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

                        }
                }

            }
            .addOnFailureListener { e ->
                //failed uploading
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

            }

    }


    private fun imagePickDialog() {
        //Image pick intent gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(
            Intent.createChooser(intent,"Choose Image"),
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun videoPickDialog() {
        //option to display in dialog
        val options = arrayOf("Camera", "Gallery")

        //alert dialog
        val builder = AlertDialog.Builder(this)
        //title
        builder.setTitle("Pick Video From")
            .setItems(options){ dialogInterface, i->
            //handle item clicks
            if(i==0){
                //camera clicked
                if (!checkCameraPermissions()){
                    //permissions was not allowed, request
                    requestCameraPermissions()
                }
                else{
                    //permissions was allowed, pick video
                    videoPickCamera()
                }
            }
            else{
                //gallery clicked
                videoPickGallery()
            }
        }
            .show()
    }

    private fun requestCameraPermissions(){
        //request camera permissions
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            CAMERA_REQUEST_CODE
        )
    }

    private fun checkCameraPermissions():Boolean{
        // check if camera permissions i.e camera and storage is allowed or not
        val result1 = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val result2 = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        return result1 && result2
    }

    private fun setVideoToView() {
        //set the picked video to video view

        //video play controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        //set media controller
        videoView.setMediaController(mediaController)
        //set video uri
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.setOnPreparedListener({
            //when video is ready, by default dont play automaticly
            videoView.pause()
        })
    }

//    private fun setImageToView(){
//        img
//    }

    private fun videoPickGallery(){
        //video pick intent gallery
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(
            Intent.createChooser(intent,"Choose Video"),
            VIDEO_PICK_GALLERY_CODE
        )
    }

    private fun videoPickCamera(){
        //video pick intent camera
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(
            intent, VIDEO_PICK_CAMERA_CODE
        )
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //goto previous activity
        return super.onSupportNavigateUp()
    }

    //handle permission results
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE ->
                if (grantResults.size > 0){
                    //check if permissions allowed or denied
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted){
                        //both permission is allowed
                        videoPickCamera()
                    }
                    else{
                        //both or one of those are denied
                        Toast.makeText(this,"Permissions denied", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    //handle video pick result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == RESULT_OK){
            //video is picked from camera or gallery
                if (requestCode == VIDEO_PICK_CAMERA_CODE){
                    //video picked form camera
                    videoUri == data!!.data
                    setVideoToView()
                }else if(requestCode == VIDEO_PICK_GALLERY_CODE){
                    //video picked from gallery
                    videoUri = data!!.data
                    setVideoToView()
                }else if(requestCode == IMAGE_PICK_GALLERY_CODE){
                    imageUri = data!!.data
                    imgViewField.setImageURI(imageUri)
                }
        }
        else{
            //cancelled picking video
            Toast.makeText(this,"cancelled", Toast.LENGTH_SHORT).show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }



}