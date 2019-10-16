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
import android.widget.*
import com.dmims.dmims.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_mcq__examcell.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import java.util.*

class AcademicCalenderUploadA : AppCompatActivity() {
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

    lateinit var et_pdfName:EditText
    var pdfName:String=""
     var PdfNameHolder:String=""
     var PdfPathHolder:String=""
    lateinit var PdfID:String


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.academic_calender_upload)

        spinnerSession=findViewById(R.id.spinner_sessionAc)
        et_pdfName=findViewById(R.id.et_pdfname)



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
            pdfName=titlename+spinnerSession.selectedItem.toString()

            println("pdfName pdf " +pdfName)
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
        else if (et_pdfName.text.toString().equals(""))
        {
            et_pdfName.setError("Please Enter Pdf Name")
        }else if (uri==null)
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

        PdfNameHolder = pdfName + "_" +et_pdfName.text.toString()


       PdfPathHolder = FilePath.getPath(this, uri)

        println("PdfNameHolder "+PdfNameHolder+"PdfPathHolder "+PdfPathHolder)




     if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show()

        } else {
            //Dialog Start
            val dialog: AlertDialog = SpotsDialog.Builder().setContext(this).build()

            try {
                dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
                dialog.setCancelable(false)
                dialog.show()
                //Dialog End


                PdfID = UUID.randomUUID().toString()
                str_spinnerSession=spinnerSession.selectedItem.toString()

                MultipartUploadRequest(this, PdfID, ExamMcqUpload.PDF_UPLOAD_HTTP_URL)
                    .addFileToUpload(PdfPathHolder, "pdf")
                    .addParameter("name", PdfNameHolder)
                    .addParameter("Session", str_spinnerSession)
                    .setNotificationConfig(UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload()

                dialog.dismiss()

            } catch (exception: Exception) {
                dialog.dismiss()

                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        }

    }

}
