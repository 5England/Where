package com.devwan.where

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract.Attendees.query
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.android.synthetic.main.fragment_upload.view.*
import java.io.File


class UploadFragment : Fragment() {

    lateinit var mContext: Context
    var storage = Firebase.storage

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

//    lateinit var filePath : String
//    lateinit var file : Uri

    lateinit var imageUri: Uri
    lateinit var image: String
    lateinit var title: String
    lateinit var detail: String
    lateinit var address: GeoPoint
    val like: Number = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.fragment_upload, container, false)

        val btnSetImage = rootView.findViewById<ImageView>(R.id.btn_set_image)
        btnSetImage.setOnClickListener(View.OnClickListener {
            getPicture()
        })

        val btnSetGeopoint = rootView.findViewById<LinearLayout>(R.id.btn_set_geopoint)
        btnSetGeopoint.setOnClickListener(View.OnClickListener {
            getGeopoint()
        })

        rootView.btn_upload.setOnClickListener {
            if (imageUri == null) Toast.makeText(context, "no Image", Toast.LENGTH_LONG).show()
            else if (address == null) Toast.makeText(context, "no address", Toast.LENGTH_LONG)
                .show()
            else if (edit_title.text.toString().isBlank()) Toast.makeText(
                context,
                "no title",
                Toast.LENGTH_LONG
            ).show()
            else uploadWhere()

            uploadWhere()
        }

        return rootView
    }

    fun uploadWhere() {
        title = edit_title.text.toString()
        detail = edit_detail.text.toString()
        uploadImageStorage()
    }

    fun uploadImageStorage() {
        var storageRef = storage.reference
        val ref = storageRef.child("images/" + imageUri.toString())
        var uploadTask = ref.putFile(imageUri)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                Toast.makeText(mContext, "이미지가 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_LONG)
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                image = task.result.toString()
            } else {
                // Handle failures
                // ...
            }
        }
    }

    //mapAcitivity를 띄워서 marker 찍은 곳의 경로를 가져오는 기능.
    fun getGeopoint() {
        val intent = Intent(mContext, MapsActivity::class.java)

        startActivityForResult(intent, 2000)
    }

    //사진 uri 구하기
    fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //이미지 담당 인텐트 처리
        if (requestCode == 1000) {
            if(data != null)
            {
                imageUri = data!!.data!!
                btn_set_image.setImageURI(imageUri)
                btn_set_image.setPadding(0, 0, 0, 0)
                btn_set_image.scaleType = ImageView.ScaleType.CENTER_CROP
//            filePath = getImageFilePath(curUri)
//            file = Uri.fromFile(File(filePath))
            }else{
                Toast.makeText(mContext, "이미지가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        //Geopoint 담당 인텐트 처리
        if (requestCode == 2000){
            if(data != null)
            {
                address = GeoPoint(data.getDoubleExtra("latitude", 0.0),
                    data.getDoubleExtra("longitude", 0.0))

                Toast.makeText(mContext, address.longitude.toString(), Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(mContext, "데이터가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

//    fun getImageFilePath(contentUri : Uri): String{
//        if(contentUri.path!!.startsWith("/storage")) return contentUri.path!!
//
//        var columnIndex = 0
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = mContext.contentResolver.query(contentUri, projection, null, null, null)
//        if(cursor!!.moveToFirst()){
//            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        }
//        return cursor.getString(columnIndex)
//    }
}






