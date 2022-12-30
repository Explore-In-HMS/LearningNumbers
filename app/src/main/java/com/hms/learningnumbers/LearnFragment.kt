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
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

class LearnFragment : Fragment(), MLAsrListener
{
    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    private val args: LearnFragmentArgs by navArgs()

    private var bitmap: Bitmap? = null



    private lateinit var micButton: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLearnBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val img = args.filepath
        Log.e(ContentValues.TAG, "path $img")
        bitmap = BitmapFactory.decodeFile(img)
        binding.imageView.setImageBitmap(bitmap)

        micButton = binding.micLottie
        micButton.setMinAndMaxProgress(0f, 0.5f)
        micButton.setOnClickListener{
            if (micButton.progress == 0f){
                micButton.playAnimation()
                runObjectDetection(BitmapFactory.decodeFile(img)).run {
                speechToText()
                    micButton.cancelAnimation()
                }

            }
            //            else {
//                micButton.cancelAnimation()
//
//            }
        }



//        binding.micDeactive.setOnClickListener {
//            runObjectDetection(getImageAsBitmap()).run {
//                speechToText()
//            }
//        }

    }

    //    fun getImageAsBitmap(): Bitmap {
//        val img = args.filepath
//        Log.e(ContentValues.TAG, "path $img")
//        bitmap = BitmapFactory.decodeFile(img)
//        Log.e(ContentValues.TAG, "bune $bitmap")
//        return
//        return BitmapFactory.decodeResource(resources, R.drawable.sun)
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
            Toast.makeText(requireContext(), "Number is: ${it.size}", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun speechToText() {
//        MLApplication.getInstance().apiKey = utils.API_KEY
//        val intent = Intent(activity, MLAsrCaptureActivity::class.java)
//            .putExtra(MLAsrCaptureConstants.LANGUAGE, "tr-TR")
//            .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX)
//        startActivityForResult(intent, 100)

        val mSpeechRecognizer = MLAsrRecognizer.createAsrRecognizer(requireContext())

        MLApplication.getInstance().apiKey = utils.API_KEY
        // Create an Intent to set parameters.
        val mSpeechRecognizerIntent = Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH)
        // Use Intent for recognition parameter settings.
        mSpeechRecognizerIntent
        // Set the language that can be recognized to English. If this parameter is not set, English is recognized by default. Example: "zh-CN": Chinese; "en-US": English; "fr-FR": French; "es-ES": Spanish; "de-DE": German; "it-IT": Italian; "ar": Arabic; "th=TH": Thai; "ms-MY": Malaysian; "fil-PH": Filipino; "tr-TR": Turkish.
            .putExtra(MLAsrConstants.LANGUAGE, "tr-TR" ) // Set to return the recognition result along with the speech. If you ignore the setting, this mode is used by default. Options are as follows:
            // MLAsrConstants.FEATURE_WORDFLUX : Recognizes and returns texts through onRecognizingResults .
            // MLAsrConstants.FEATURE_ALLINONE : After the recognition is complete, texts are returned through onResults .
            .putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_WORDFLUX) // Set the application scenario. MLAsrConstants.SCENES_SHOPPING indicates shopping, which is supported only for Chinese. Under this scenario, recognition for the name of Huawei products has been optimized.
            .putExtra(MLAsrConstants.SCENES, MLAsrConstants.SCENES_SHOPPING)
        // Start speech recognition.
        mSpeechRecognizer.startRecognizing(mSpeechRecognizerIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Log.d(ContentValues.TAG, "Success choose photo")

            val result = data.getSerializableExtra(RecognizerIntent.EXTRA_RESULTS)




        }
    }


    object utils{
        const val API_KEY = "DAEDAHQwerh7OS7lnuOmuyDMLifl13P4bNCjTFmwfadXhD11Ct2Wneb3710tyiOEA37MJAQewSxQKot6D338dXODq7JHZKBHfQN1PA=="
    }

    override fun onResults(p0: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onRecognizingResults(p0: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onStartListening() {
        TODO("Not yet implemented")
    }

    override fun onStartingOfSpeech() {
        TODO("Not yet implemented")
    }

    override fun onVoiceDataReceived(p0: ByteArray?, p1: Float, p2: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onState(p0: Int, p1: Bundle?) {
        TODO("Not yet implemented")
    }

}