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

import FileUtil
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hms.learningnumbers.databinding.FragmentUploadBinding
import java.io.File
import kotlin.random.Random


class UploadFragment: Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    val REQUEST_CODE = 200


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

            val action = UploadFragmentDirections.actionUploadFragmentToLearnFragment(getRandom(),1)
            findNavController().navigate(action)

        }

        binding.btnGallery.setOnClickListener {
            openGallery()
        }
    }

    fun getRandom (): String {
        val assetManager = requireContext().assets
        val fileList = assetManager.list("samples/") ?: emptyArray()

        Log.e(TAG, "file is $fileList")

        val randomIndex = Random.nextInt(fileList.size)
        val randomFile = fileList[randomIndex]
        val imagePath ="samples/$randomFile"

        Log.e(TAG, "file is $imagePath")

     return imagePath
    }


    private fun openGallery() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Log.d(TAG, "Success choose photo")

            val path = filePath(data)
            Log.e(TAG, "path is $path")

            val action = path?.let {
                UploadFragmentDirections.actionUploadFragmentToLearnFragment(it,0) }
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

            }
            return null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}