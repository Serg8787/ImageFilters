package com.example.imagefilters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import com.mukesh.image_processing.ImageProcessor

class MainActivity : AppCompatActivity(),ViewHolder.FiltersCallback {
    lateinit var bitmap:Bitmap
    lateinit var processedBitmap:Bitmap
    lateinit var ivMain:ImageView
    lateinit var ivGallery:ImageView
    lateinit var ivCamera:ImageView
    lateinit var ivSave:ImageView
    lateinit var btLogin:Button
    lateinit var edLogin:EditText
    lateinit var edPassword:EditText
    lateinit var clShader:ConstraintLayout
    val PICK_IMAGE = 100
    val PICK_IMAGE2 = 101
    lateinit var rvFilters:RecyclerView
    var bitmaps = ArrayList<Filter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivMain = findViewById(R.id.ivMain)
        ivGallery = findViewById(R.id.ivGallery)
        ivCamera = findViewById(R.id.ivCamera)
        ivSave = findViewById(R.id.ivSave)
        edLogin = findViewById(R.id.edLogin)
        edPassword = findViewById(R.id.edPasword)
        btLogin = findViewById(R.id.btLogin)
        rvFilters = findViewById(R.id.rvFilters)
        clShader = findViewById(R.id.clShader)


        bitmap = BitmapFactory.decodeResource(resources,R.drawable.firstfoto)
        val processor = ImageProcessor()
        processedBitmap = processor.tintImage(bitmap,120)

        ivMain.setImageBitmap(processedBitmap)
        generateBitmaps()
        initAdapter()
        initOnClick()
    }
    fun initOnClick(){
        ivGallery.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE)

        }
        ivCamera.setOnClickListener {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,PICK_IMAGE2)

        }
        ivSave.setOnClickListener {
            ImageSave().saveTempBitmap(processedBitmap)
        }
        btLogin.setOnClickListener {
            if(edLogin.text.toString().equals("user") && edPassword.text.toString().equals("12345")){
                clShader.visibility = View.GONE

            }
        }


    }
   fun generateBitmaps(){
        val processor = ImageProcessor()
       bitmaps.clear()
       bitmaps.add(Filter("Original",bitmap))
       bitmaps.add(Filter("ColorShift",processor.tintImage(bitmap,120)))
//       bitmaps.add(Filter("Sepia",processor.createSepiaToningEffect(bitmap,20,30.0,20.0,20.0)))
       bitmaps.add(Filter("Saturation",processor.applySaturationFilter(bitmap,3)))
       bitmaps.add(Filter("ColorShift",processor.tintImage(bitmap,120)))
       bitmaps.add(Filter("MeanRemoval",processor.applyMeanRemoval(bitmap)))
       bitmaps.add(Filter("Snow",processor.applySnowEffect(bitmap)))

    }
    fun initAdapter(){
        val adapter = FilterAdapter(bitmaps,this,this)
        rvFilters.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvFilters.adapter = adapter

    }

    override fun itemSelected(index: Int) {
        processedBitmap = bitmaps[index].bitmap
        ivMain.setImageBitmap(bitmaps[index].bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== PICK_IMAGE && resultCode== RESULT_OK){
            val imageUri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
            generateBitmaps()
            initAdapter()
            ivMain.setImageBitmap(bitmap)
        } else if(requestCode==PICK_IMAGE2 && resultCode== RESULT_OK){
            bitmap = data?.extras?.get("data") as Bitmap
            generateBitmaps()
            initAdapter()
            ivMain.setImageBitmap(bitmap)
        }
    }

}