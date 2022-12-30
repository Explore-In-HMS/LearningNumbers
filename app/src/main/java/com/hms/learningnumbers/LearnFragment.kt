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

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.hms.learningnumbers.databinding.FragmentLearnBinding
import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.asr.MLAsrConstants
import com.huawei.hms.mlsdk.asr.MLAsrListener
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzerSetting


class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    private val args: LearnFragmentArgs by navArgs()

    private var bitmap: Bitmap? = null


    private var numObj= 1
    private var numSpc= 1

    private lateinit var micButton: LottieAnimationView

    object utils{
        const val API_KEY = "YOUR API KEY HERE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLearnBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bitmap = moveAction()

        binding.imageView.setImageBitmap(bitmap)

        micButton = binding.micLottie
        micButton.setMinAndMaxProgress(0f, 0.5f)
        micButton.setOnClickListener{
            if (micButton.progress == 0f){
                micButton.playAnimation()
                moveAction()?.let { it1 ->
                    runObjectDetection(it1).run {
                        speechToText()
                    }
                }


            } else {
                micButton.progress = 0f

            }
        }

    }

    fun moveAction(): Bitmap? {
        val moveAct = args.moveActtion
        val imgPath = args.filepath

        if (moveAct == 0){
            Log.e(ContentValues.TAG, "path $imgPath")
            return BitmapFactory.decodeFile(imgPath)
        } else {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open(imgPath)
            return BitmapFactory.decodeStream(inputStream)

        }

    }



    private fun runObjectDetection(bitmap: Bitmap) {
        val setting = MLObjectAnalyzerSetting.Factory()
            .setAnalyzerType(MLObjectAnalyzerSetting.TYPE_PICTURE)
            .allowMultiResults()
            .allowClassification()
            .create()
        val analyzer = MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(setting)
        val frame = MLFrame.fromBitmap(bitmap)
        val task = analyzer!!.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
//            Toast.makeText(requireContext(), "Number is: ${it.size}", Toast.LENGTH_LONG).show()
            numObj = it.size
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun speechToText() {
        MLApplication.getInstance().apiKey = utils.API_KEY
        val intent = Intent(activity, MLAsrCaptureActivity::class.java)
            .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
            .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var text = ""

        if (requestCode == 100) {
            when (resultCode) {
                MLAsrCaptureConstants.ASR_SUCCESS -> {
                    if (data != null) {
                        val bundle = data.extras
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                            text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT)!!
                               val num = text.toIntOrNull()
                                 if (num != null){
                                     numSpc = text.toInt()
                                 } else{
                                     Toast.makeText(requireContext(), "Wrong Speech: $text", Toast.LENGTH_LONG).show()

                                 }
                        }

                        if (text != null && text.isNotEmpty()) {

                            binding.resultText.text = text
                            micButton.cancelAnimation()
                            micButton.progress = 0f
                            result()
                        }

                    }
                }

                MLAsrCaptureConstants.ASR_FAILURE -> {
                    if (data != null) {
                        val bundle = data.extras
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                            val errorCode = bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE)
//                            showFailedDialog(getPrompt(errorCode))
                        }
                    }
                }
                else -> {}
            }
        }



    }

    fun result(){
        var action: NavDirections?
        if (numObj.equals(numSpc)){
             action = LearnFragmentDirections.actionLearnFragmentToResultFragment(true)
//            Toast.makeText(requireContext(), "Number OK: $numObj", Toast.LENGTH_LONG).show()
        } else{
             action= LearnFragmentDirections.actionLearnFragmentToResultFragment(false)
            Toast.makeText(requireContext(), "Number should be $numObj", Toast.LENGTH_LONG).show()
        }
        val handler = Handler()
        handler.postDelayed({
            findNavController().navigate(action)
        },1000)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }





}