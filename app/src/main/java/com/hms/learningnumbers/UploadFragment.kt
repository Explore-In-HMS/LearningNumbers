/*
 * Copyright 2022. Huawei Technologies Co., Ltd. All rights reserved.
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.hms.learningnumbers

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hms.learningnumbers.databinding.FragmentUploadBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class UploadFragment: Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private var bitmap: Bitmap? = null

    private lateinit var photoFile: File
    lateinit var currentPhotoPath: String

    val REQUEST_CODE = 200


    companion object {
        private const val RC_CHOOSE_PHOTO = 3
        private const val PICTURE_FROM_CAMERA = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnCamera.setOnClickListener {
            takePicture()
//            capturePhoto()

        }
        binding.btnGallery.setOnClickListener {
            openGallery()
        }
    }



    private fun takePicture(){
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create the File where the photo should go
        photoFile= createImageFile()

        val uri= FileProvider.getUriForFile(requireContext(),"com.hms.learningnumbers.fileprovider", photoFile)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(pictureIntent, REQUEST_CODE)
    }

    private fun createImageFile(): File {
        val timeStamp: String= SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File?= context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply{
            currentPhotoPath = absolutePath}
    }

//    fun capturePhoto() {
//
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, REQUEST_CODE)
//    }

    private fun openGallery() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Log.d(TAG, "Success choose photo")


//            val img = data?.extras?.get("data") as Bitmap
//            val imgUri = filePath(img)

//            val action =  UploadFragmentDirections.actionUploadFragmentToLearnFragment(data)
//            binding.imageView.setImageBitmap(data.extras?.get("data") as Bitmap)


            val path = filePath(data)
//            BitmapFactory.decodeFile(path)

            Log.e(TAG, "path is $path")
//
//            bitmap = BitmapFactory.decodeFile(path)
//            binding.imageView.setImageBitmap(bitmap)
            val action = path?.let {
                UploadFragmentDirections.actionUploadFragmentToLearnFragment(it) }
            if (action != null) {
                findNavController().navigate(action)
            }

        }

    }


    private fun filePath( data: Intent?): String? {
            val uri: Uri? = data?.data
            val filePath = FileUtil.getFilePathByUri(requireContext(), uri!!)
            if (!TextUtils.isEmpty(filePath)) {
                Log.e(TAG, "file is $filePath")
                return filePath
//              return BitmapFactory.decodeFile(filePath)
            }
//        }
            return null
    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}