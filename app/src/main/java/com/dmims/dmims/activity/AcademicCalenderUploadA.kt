package com.dmims.dmims.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.broadCasts.SingleUploadBroadcastReceiver
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_mcq__examcell.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import org.json.JSONObject
import java.util.*

class AcademicCalenderUploadA : AppCompatActivity() , SingleUploadBroadcastReceiver.Delegate {
    private val TAG1: String = "AndroidUploadService"
    var dialogCommon: android.app.AlertDialog? = null
    val uploadReceiver: SingleUploadBroadcastReceiver = SingleUploadBroadcastReceiver()

    override fun onResume() {
        super.onResume()
        uploadReceiver.register(this)
    }

    override fun onPause() {
        super.onPause()
        uploadReceiver.unregister(this)
    }


    override fun onError(exception: Exception) {
        println("onError >>> " + exception!!.stackTrace)
        dialogCommon!!.dismiss()
        GenericUserFunction.showApiError(
            this,
            "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
        )
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCompleted(serverResponseCode: Int, serverResponseBody: ByteArray) {
        println("onCompleted >>> serverResponseCode >>> " + serverResponseCode + " serverResponseBody >>> " + serverResponseBody)

        val charset = Charsets.UTF_8
        println("onCompleted 1 >>> " + serverResponseBody.toString(charset))

        var filename = serverResponseBody.toString(charset)
        dialogCommon!!.dismiss()
        var jsonObject=JSONObject(filename)
        GenericUserFunction.showPositivePopUp(this,jsonObject.getString("response"))

//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProgress(progress: Int) {
        println("onProgress 1 >>> uploadedBytes " + progress)
    }


    override fun onProgress(uploadedBytes: Long, totalBytes: Long) {
        println("onProgress 2 >>> uploadedBytes " + uploadedBytes + " totalBytes >>> " + totalBytes)
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelled() {
        println("onCancelled >>> ")
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var btnPickPdf: Button
    private lateinit var btnPublishCal: Button
    var REQUEST_CODE: Int = 0
    var type : String? = null
    private var uri: Uri? = null
    private var confirmStatus = "F"
    var titlename:String="AC_"
    lateinit var str_spinner:String
    lateinit var spinnerSession: Spinner
    lateinit var str_spinnerSession:String

    lateinit var et_pdfName:TextView
    var pdfName1:String? = null
     var PdfNameHolder:String=""
     var PdfPathHolder:String=""
    lateinit var PdfID:String


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.academic_calender_upload)

        dialogCommon = SpotsDialog.Builder().setContext(this).build()

        spinnerSession=findViewById(R.id.spinner_sessionAc)
        et_pdfName=findViewById(R.id.et_pdfname)

        spinnerSession.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var selectedtypeoftimetbl = p0!!.getItemAtPosition(p2) as String
                if(!selectedtypeoftimetbl.equals("--Select Session--")) {
                    pdfName1 = "CAAC_" + selectedtypeoftimetbl
                    et_pdfName.text = pdfName1
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnPickPdf=findViewById(R.id.btn_pdfChoose)
        btnPickPdf.setOnClickListener {

            REQUEST_CODE = 200
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                startActivityForResult(intent, 200)


        }
        btnPublishCal=findViewById(R.id.btn_uploadCalender)
        btnPublishCal.setOnClickListener {

            CheckValidation()

        }

    }




    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            type = "pdf"
            uri = data!!.data
            println("Uri pdf"+uri)


            if(uri.toString().isNotEmpty()) {
                confirmStatus = "T"

            }
            else
            {
                confirmStatus = "F"
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun CheckValidation() {
        if (spinnerSession.selectedItem.toString().equals("--Select Session--"))
        {
            Toast.makeText(applicationContext,"Please Select Session",Toast.LENGTH_LONG).show()
        }
        else if (uri==null)
        {
            println("no uri")
        }
        else
        {
            PdfUploadFunction()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun PdfUploadFunction() {

       PdfPathHolder = FilePath.getPath(this, uri)

        println("PdfNameHolder "+PdfNameHolder+"PdfPathHolder "+PdfPathHolder)




     if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show()

        } else {
            //Dialog Start

         //Dialog Start
         dialogCommon!!.setMessage("Please Wait!!! \nwhile we are Uploading Calender")
         dialogCommon!!.setCancelable(false)
         dialogCommon!!.show()

            try {

                PdfID = UUID.randomUUID().toString()
                uploadReceiver.setDelegate(this)
                uploadReceiver.setUploadID(PdfID!!)
                str_spinnerSession=spinnerSession.selectedItem.toString()

                MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                    .addFileToUpload(PdfPathHolder, "pdf")
                    .addParameter("name", pdfName1)
                    .addParameter("Session", str_spinnerSession)
                    .setNotificationConfig(UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload()


            } catch (exception: Exception) {
                dialogCommon!!.dismiss()

                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        }

    }
    companion object {

        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/uploadAcademicCalenderA.php"
    }
}
