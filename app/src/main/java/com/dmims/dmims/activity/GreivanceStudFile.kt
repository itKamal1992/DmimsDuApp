package com.dmims.dmims.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.MyResponse
import com.dmims.dmims.remote.Api
import com.dmims.dmims.remote.IMyAPI
import com.google.gson.GsonBuilder
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_greivance_stud_file.*
import kotlinx.android.synthetic.main.activity_grievance_cell.*
import kotlinx.android.synthetic.main.activity_mcq__examcell.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GreivanceStudFile : AppCompatActivity()
{
    lateinit var str_NameGriev: String
    lateinit var str_SubOfComplaintGriev: String
    lateinit var str_CategoryGriev: String
    lateinit var str_ComplaintAgainstDetailGriev: String
    lateinit var str_DetailDescriGriev: String
    lateinit var str_CollegeNameGrievGriev: String
    lateinit var str_ComplaintToGriev: String
    lateinit var str_DateGriev: String



    lateinit var et_NameGriev:EditText
    lateinit var et_SubOfComplaintGriev:EditText
    lateinit var et_ComplaintAgainstDetailGriev:EditText
    lateinit var et_DetailDescriGriev:EditText
    lateinit var et_DateGriev:EditText

    lateinit var spinner_CategoryGriev: Spinner
    lateinit var spinner_CollegeNameGriev: Spinner
    lateinit var spinner_ComplaintToGriev: Spinner

    lateinit var btnPickerFile:Button

    var cal = Calendar.getInstance()
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var current_date: String = "-"
    private lateinit var mServices: IMyAPI


    private var selectedImage: Uri? = null
    private var confirmStatus = "F"
    var filename: String = "-"

    lateinit var STUD_ID:String
    lateinit var course_id:String
    lateinit var roll_no:String
   // lateinit var course_id:String

     var  Grev_Filename:String=""
     var  G_TICKETNO:String=""
     var  G_ATTACHMENT:String=""
     var  G_STATUS:String=""
     var  U_ID:String=""
     var  ASSING_TO_ID:String=""
     var  REMINDER:String=""
     var  Principal_Status:String=""
     var  Dean_Status:String=""
     var  Conveyour_Status:String=""
     var  G_SUBJECT:String=""
     var  G_CATEGORY:String=""
     var  G_AGAINST:String=""
     var  G_DISCRIPTION:String=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greivance_stud_file)

        et_NameGriev=findViewById(R.id.et_namegrivienceStud)
        et_SubOfComplaintGriev=findViewById(R.id.et_subComplaintGrive)
        spinner_CategoryGriev=findViewById(R.id.spinner_studGriveCategory)
        et_ComplaintAgainstDetailGriev=findViewById(R.id.et_DetailsComplaintGrive)
        et_DetailDescriGriev=findViewById(R.id.et_DetailsDescriGrive)
        spinner_CollegeNameGriev=findViewById(R.id.spinner_CollegeNameGriev)
        spinner_ComplaintToGriev=findViewById(R.id.spinner_ComplaintToGriev)
        et_DateGriev=findViewById(R.id.et_dateGriev)
        btnPickerFile=findViewById(R.id.admin_griev_upload)

        btnPickerFile.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            println("path >> " + i.data)
            startActivityForResult(i, 100)
        }

        var dateCurrent:Date
        mServices = Common.getAPI()

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)


        current_date = sdf.format(cal.time)
        from_date_sel = sdf.format(cal.time)

        println("current_date " +current_date)
        et_DateGriev.setHint(current_date)
        et_DateGriev.isEnabled=false


        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)


        var STUD_ID = mypref.getString("Stud_id_key", null)
        var course_id = mypref.getString("course_id", null)
        var roll_no = mypref.getString("roll_no", null)



        btn_submit_grievance.setOnClickListener {


            if (et_NameGriev.text.toString().equals(""))
            {
                et_NameGriev.setError("Plese fill information")
            }else  if (et_SubOfComplaintGriev.text.toString().equals(""))
            {
                et_SubOfComplaintGriev.setError("Plese fill information")
            }else  if (spinner_CategoryGriev.selectedItem.toString().equals("--Select Grievance Category--"))
            {
                Toast.makeText(this,"Select Grievance Category",Toast.LENGTH_LONG).show()

            }else  if (et_ComplaintAgainstDetailGriev.text.toString().equals(""))
            {
                et_ComplaintAgainstDetailGriev.setError("Plese fill information")
                et_ComplaintAgainstDetailGriev?.isFocusable = true
            }else  if (et_DetailDescriGriev.text.toString().equals(""))
            {
                et_DetailDescriGriev.setError("Plese fill information")
            }else  if (spinner_CollegeNameGriev.selectedItem.equals("--Select College--"))
            {
                Toast.makeText(this,"Select College",Toast.LENGTH_LONG).show()
            }
            else  if (spinner_ComplaintToGriev.selectedItem.toString().equals("--Select Complaint To--"))
            {
                Toast.makeText(this,"Select Complaint To",Toast.LENGTH_LONG).show()
            }
            else
            {
                str_NameGriev=et_NameGriev.text.toString()
                str_SubOfComplaintGriev=et_SubOfComplaintGriev.text.toString()
                str_CategoryGriev=spinner_CategoryGriev.selectedItem.toString()
                str_ComplaintAgainstDetailGriev=et_ComplaintAgainstDetailGriev.text.toString()
                str_DetailDescriGriev=et_DetailDescriGriev.text.toString()
                str_CollegeNameGrievGriev=spinner_CollegeNameGriev.selectedItem.toString()
                str_ComplaintToGriev=spinner_ComplaintToGriev.selectedItem.toString()
                str_DateGriev=et_DateGriev.text.toString()


//                editor.putString("Stud_id_key", STUD_ID)
//                editor.putString("key_userrole", USER_ROLE)
//                editor.putString("key_doa", DOA)
//                editor.putString("course_id", courseid_1)
//                editor.putString("roll_no", roll_no)


//                println(" Grievience submitted  "+str_NameGriev+str_SubOfComplaintGriev+str_CategoryGriev+str_ComplaintAgainstDetailGriev+str_DetailDescriGriev+
//                        str_CollegeNameGrievGriev+str_ComplaintToGriev+str_DateGriev)

                try {

                    println("confirmStatus "+confirmStatus)
                    // if (RESOU_FLAG == "T") {
                    if (confirmStatus == "T") {
                        try {
                            //Dialog Start
                            val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
                            dialog.setMessage("Please Wait!!! \nwhile we are sending your grievance")
                            dialog.setCancelable(false)
                            dialog.show()
                            //Dialog End

                            //   uploadFile(selectedImage!!,"Notice")
                            var fileUri = selectedImage!!
                            val file = File(getRealPathFromURI(fileUri))
                            //creating request body for file
                            val requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
                            val descBody = RequestBody.create(MediaType.parse("text/plain"), "Notice")
                            //The gson builder
                            val gson = GsonBuilder()
                                .setLenient()
                                .create()
                            //creating retrofit object
                            val retrofit = Retrofit.Builder()
                                .baseUrl(Api.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build()
                            //creating our api
                            val api = retrofit.create(Api::class.java)
                            //creating a call and calling the upload image method
                            val call = api.uploadImage2(requestFile, descBody)
                            //finally performing the call
                            call.enqueue(object : Callback<MyResponse> {
                                override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                                    if (!response.body()!!.error) {
                                        filename = response.body()!!.message.toString()
                                        println("file name is "+filename)

                                        try {
                                            mServices.InsertStudentGrievance(
                                                STUD_ID,
                                                course_id,
                                                roll_no,
                                                str_NameGriev,
                                                str_SubOfComplaintGriev,
                                                str_CategoryGriev,
                                                str_ComplaintAgainstDetailGriev,
                                                str_DetailDescriGriev,
                                                str_CollegeNameGrievGriev,
                                                str_ComplaintToGriev,
                                                current_date,
                                                filename,
                                                G_TICKETNO,
                                                G_ATTACHMENT,
                                                G_STATUS,
                                                U_ID,
                                                ASSING_TO_ID,
                                                REMINDER,
                                                Principal_Status,
                                                Dean_Status,
                                                Conveyour_Status,
                                                G_SUBJECT,
                                                G_CATEGORY,
                                                G_AGAINST,
                                                G_DISCRIPTION
                                            ).enqueue(object : Callback<APIResponse> {
                                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                                    Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                                                }

                                                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                                    dialog.dismiss()
                                                    val result: APIResponse? = response.body()
//                                        Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()
                                                    GenericUserFunction.showPositivePopUp(this@GreivanceStudFile,"Grievance Send Successfully")
                                                }
                                            })
                                        } catch (ex: Exception) {
                                            dialog.dismiss()

                                            ex.printStackTrace()
                                            GenericUserFunction.showApiError(
                                                applicationContext,
                                                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                                            )
                                        }
                                    }
                                }
                            })

                        } catch (ex: Exception) {

                            ex.printStackTrace()
                            GenericUserFunction.showApiError(
                                this,
                                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
                    if (confirmStatus == "F") {
                        try {
                            //Dialog Start
                            val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
                            dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
                            dialog.setCancelable(false)
                            dialog.show()
                            //Dialog End
                            filename="-"
                            try {
                                mServices.InsertStudentGrievance(
                                    STUD_ID,
                                    course_id,
                                    roll_no,
                                    str_NameGriev,
                                    str_SubOfComplaintGriev,
                                    str_CategoryGriev,
                                    str_ComplaintAgainstDetailGriev,
                                    str_DetailDescriGriev,
                                    str_CollegeNameGrievGriev,
                                    str_ComplaintToGriev,
                                    current_date,
                                    filename,
                                    G_TICKETNO,
                                    G_ATTACHMENT,
                                    G_STATUS,
                                    U_ID,
                                    ASSING_TO_ID,
                                    REMINDER,
                                    Principal_Status,
                                    Dean_Status,
                                    Conveyour_Status,
                                    G_SUBJECT,
                                    G_CATEGORY,
                                    G_AGAINST,
                                    G_DISCRIPTION

                                ).enqueue(object : Callback<APIResponse>
                                {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable)
                                    {
                                        Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                        dialog.dismiss()
                                        val result: APIResponse? = response.body()
                                        GenericUserFunction.showPositivePopUp(this@GreivanceStudFile,"Notice Send Successfully")
//                            Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                .show()
                                    }
                                })
                            } catch (ex: Exception)
                            {
                                dialog.dismiss()

                                ex.printStackTrace()
                                GenericUserFunction.showApiError(
                                    applicationContext,
                                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }

                        } catch (ex: Exception) {

                            ex.printStackTrace()
                            GenericUserFunction.showApiError(
                                this,
                                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
                }catch (ex: Exception) {
                    ex.printStackTrace()
                }


                println(

                    "STUD_ID >> " + STUD_ID
                            +"\n"+ "course_id >> " + course_id
                            +"\n"+ "roll_no >> " + roll_no
                            +"\n"+ "Grev_name >> " + str_NameGriev
                            +"\n"+ "Sub_Grev >> " + str_SubOfComplaintGriev
                            +"\n"+ "Grev_Cat >> " + str_CategoryGriev
                            +"\n"+ "Comp_agst >> " + str_ComplaintAgainstDetailGriev
                            +"\n"+ "Grev_Desc >> " + str_DetailDescriGriev
                            +"\n"+ "Inst_Name >> " + str_CollegeNameGrievGriev
                            +"\n"+ "Comp_To >> " + str_ComplaintToGriev
                            +"\n"+ "Grev_Date >> " + current_date
                            +"\n"+ "Grev_Filename >> " + filename
                )
            }




        }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //the image URI
            selectedImage = data.getData()
            var bitmap: Bitmap? = getThumbnail(selectedImage!!)
            //calling the upload file method after choosing the file
            if (selectedImage != null) {
                println("Selected Image >>> " + selectedImage)
                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_image_yes_no_custom_popup)
                var ivNegClose1: ImageView = CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button = CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                var image: ImageView = CustDialog.findViewById(R.id.dialog_image) as ImageView
                image.setImageBitmap(bitmap)

                tvMsg.text = "Do you want to Submit Selected Image?"
                //    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "T"
                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "F"
                }
                ivNegClose1.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "F"
                }
                CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog.show()

//                uploadFile(selectedImage, "My Image")
            }
        }
    }

    @Throws(FileNotFoundException::class, IOException::class)
    fun getThumbnail(uri: Uri): Bitmap? {
        var input = this.contentResolver.openInputStream(uri)

        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1)
            return null

        val originalSize =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth

        val ratio = if (originalSize > 100.0) originalSize / 100.0 else 1.0

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
        bitmapOptions.inDither = true//optional

        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        input = this.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return bitmap
    }
    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int
    {
        val k = Integer.highestOneBit(Math.floor(ratio).toInt())
        return if (k == 0)
            1
        else
            k
    }

    private fun sendNoticeNew() {

        // if (RESOU_FLAG == "T") {
        if (confirmStatus == "T") {
            try {
                //Dialog Start
                val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
                dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
                dialog.setCancelable(false)
                dialog.show()
                //Dialog End

                //   uploadFile(selectedImage!!,"Notice")
                var fileUri = selectedImage!!
                val file = File(getRealPathFromURI(fileUri))
                //creating request body for file
                val requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
                val descBody = RequestBody.create(MediaType.parse("text/plain"), "Notice")
                //The gson builder
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                //creating retrofit object
                val retrofit = Retrofit.Builder()
                    .baseUrl(Api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                //creating our api
                val api = retrofit.create(Api::class.java)
                //creating a call and calling the upload image method
                val call = api.uploadImage2(requestFile, descBody)
                //finally performing the call
                call.enqueue(object : Callback<MyResponse> {
                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                        if (!response.body()!!.error) {
                            filename = response.body()!!.message.toString()

                            try {
                                mServices.InsertStudentGrievance(
                                    STUD_ID,
                                    course_id,
                                    roll_no,
                                    str_NameGriev,
                                    str_SubOfComplaintGriev,
                                    str_CategoryGriev,
                                    str_ComplaintAgainstDetailGriev,
                                    str_DetailDescriGriev,
                                    str_CollegeNameGrievGriev,
                                    str_ComplaintToGriev,
                                    current_date,
                                    filename,
                                    G_TICKETNO,
                                    G_ATTACHMENT,
                                    G_STATUS,
                                    U_ID,
                                    ASSING_TO_ID,
                                    REMINDER,
                                    Principal_Status,
                                    Dean_Status,
                                    Conveyour_Status,
                                    G_SUBJECT,
                                    G_CATEGORY,
                                    G_AGAINST,
                                    G_DISCRIPTION

                                ).enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                        dialog.dismiss()
                                        val result: APIResponse? = response.body()
//                                        Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()
                                        GenericUserFunction.showPositivePopUp(this@GreivanceStudFile,"Notice Send Successfully")
                                    }
                                })
                            } catch (ex: Exception) {
                                dialog.dismiss()

                                ex.printStackTrace()
                                GenericUserFunction.showApiError(
                                    applicationContext,
                                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }
                        }
                    }
                })

            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }
        if (confirmStatus == "F") {
            try {
                //Dialog Start
                val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
                dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
                dialog.setCancelable(false)
                dialog.show()
                //Dialog End
                filename="-"
                try {
                    mServices.InsertStudentGrievance(
                        STUD_ID,
                        course_id,
                        roll_no,
                        str_NameGriev,
                        str_SubOfComplaintGriev,
                        str_CategoryGriev,
                        str_ComplaintAgainstDetailGriev,
                        str_DetailDescriGriev,
                        str_CollegeNameGrievGriev,
                        str_ComplaintToGriev,
                        current_date,
                        filename,
                        G_TICKETNO,
                        G_ATTACHMENT,
                        G_STATUS,
                        U_ID,
                        ASSING_TO_ID,
                        REMINDER,
                        Principal_Status,
                        Dean_Status,
                        Conveyour_Status,
                        G_SUBJECT,
                        G_CATEGORY,
                        G_AGAINST,
                        G_DISCRIPTION
                    ).enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            dialog.dismiss()
                            val result: APIResponse? = response.body()
                            GenericUserFunction.showPositivePopUp(this@GreivanceStudFile,"Notice Send Successfully")
//                            Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                .show()
                        }
                    })
                } catch (ex: Exception)
                {
                    dialog.dismiss()

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        applicationContext,
                        "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                    )
                }

            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }

    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

}
