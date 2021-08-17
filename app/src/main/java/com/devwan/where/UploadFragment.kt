package com.devwan.where

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract.Attendees.query
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.fragment_upload.*
import kotlinx.android.synthetic.main.fragment_upload.view.*
import java.io.File


class UploadFragment : Fragment() {

    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var filePath : String
    lateinit var file : File
    lateinit var image : String
    lateinit var title : String
    lateinit var detail : String
    lateinit var address : GeoPoint
    val like : Number = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_upload, container, false)

        val btnSetImage = rootView.findViewById<ImageView>(R.id.btn_set_image)
        btnSetImage.setOnClickListener(View.OnClickListener {
            getPicture()
        })

        btn_upload.setOnClickListener{
            if(file == null) Toast.makeText(context, "no Image", Toast.LENGTH_LONG).show()
            else if(address == null) Toast.makeText(context, "no address", Toast.LENGTH_LONG).show()
            else if(edit_title.text.toString().isBlank()) Toast.makeText(context, "no title", Toast.LENGTH_LONG).show()
            else uploadWhere()
        }
        return rootView
    }


    fun uploadWhere(){
        title = edit_title.text.toString()
        detail = edit_detail.text.toString()
    }

    fun getPicture(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000){
            val uri : Uri = data!!.data!!
            btn_set_image.setImageURI(uri)
            btn_set_image.setPadding(0, 0, 0, 0)
            btn_set_image.scaleType = ImageView.ScaleType.CENTER_CROP
            filePath = getImageFilePath(uri)
            file = File(filePath)
        }
    }

    fun getImageFilePath(contentUri : Uri): String{
        if(contentUri.path!!.startsWith("/storage")) return contentUri.path!!

        var columnIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = mContext.contentResolver.query(contentUri, projection, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }
}






