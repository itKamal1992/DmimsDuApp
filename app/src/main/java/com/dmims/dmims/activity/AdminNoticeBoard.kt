package com.dmims.dmims.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.ImageClass
import com.dmims.dmims.ImageUpload
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.MyResponse
import com.dmims.dmims.remote.Api
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import com.google.gson.GsonBuilder
import dmax.dialog.SpotsDialog
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
import kotlin.collections.ArrayList

class AdminNoticeBoard : AppCompatActivity() {
    private var READ_REQUEST_CODE = 300
    private var SERVER_PATH = "http://103.68.25.26/dmims/UploadImage/"
    private var uri: Uri? = null
    private lateinit var mServices: IMyAPI
    private var selectedImage: Uri? = null
    private var confirmStatus = "F"
    private var TAG = AdminNoticeBoard::class.java.simpleName
    var noticetype = arrayOf("Administrative", "General")
    var facultystud = arrayOf("All", "Faculty", "Student")
    private lateinit var btnPickImage: Button
    private lateinit var btnPubNotice: Button
    private lateinit var spinner_noticetype: Spinner
    private lateinit var spinner_facultystud: Spinner
    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_courselist: Spinner
    private lateinit var spinner_deptlist: Spinner
    private lateinit var rImage: String
    private lateinit var rTitle: String
    var instituteName1 = ArrayList<String>()//Creating an empty arraylist
    var courselist = ArrayList<String>()
    var deptlist = ArrayList<String>()
    private lateinit var sel_notice_type: String
    var filename: String = "-"
    var course_id: String = "All"
    var dept_id: String = "All"
    private lateinit var student_flag: String
    private lateinit var faculty_flag: String
    private lateinit var admin_flag: String
    private lateinit var selectedInstituteName: String
    private lateinit var selectedcourselist: String
    private lateinit var selecteddeptlist: String
    private lateinit var selectedNoticeType: String
    private lateinit var selectedFacultyStud: String
    private lateinit var notice_date: String
    private lateinit var notice_title: String
    private lateinit var notice_desc: String
    private lateinit var editNoticeDate: TextView
    private lateinit var edit_notice_title: TextView
    private lateinit var edit_notice_desc: TextView

    var RESOU_FLAG: String = "F"
    var cal = Calendar.getInstance()
    var listsinstz: Int = 0
    private lateinit var id_admin: String
    private lateinit var roleadmin: String
    private val REQUEST_GALLERY_CODE = 111
    private lateinit var progressBarsubmit: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_notice_board)
        progressBarsubmit = findViewById(R.id.pb_notice_admin)
        btnPickImage = findViewById<Button>(R.id.admin_notice_upload)
        btnPubNotice = findViewById<Button>(R.id.btn_publish_notice2)
        editNoticeDate = findViewById(R.id.select_date)
        edit_notice_title = findViewById(R.id.edit_notice_title)
        edit_notice_desc = findViewById(R.id.notice_descr)
        spinner_noticetype = findViewById(R.id.spinner_noticetype)
        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_courselist = findViewById(R.id.spinner_courselist)
        spinner_deptlist = findViewById(R.id.spinner_deptlist)
        spinner_facultystud = findViewById(R.id.spinner_facultystud)
        //val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        roleadmin = intent.getStringExtra("roleadmin")
        id_admin = intent.getStringExtra("id_admin")
        instituteName1.add("All")
        mServices = Common.getAPI()

        progressBarsubmit.visibility = View.GONE

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editNoticeDate.text = sdf.format(cal.time).toString()

        //Spinner_1
        var noticetypeAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, noticetype)
        spinner_noticetype.adapter = noticetypeAdap

        spinner_noticetype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedNoticeType = p0!!.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //Spinner_2
        var usertypeAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, facultystud)
        spinner_facultystud.adapter = usertypeAdap

        spinner_facultystud.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFacultyStud = p0!!.getItemAtPosition(p2) as String

                if (selectedFacultyStud == "All") {
                    student_flag = "T"
                    admin_flag = "T"
                    faculty_flag = "T"
                }
                if (selectedFacultyStud == "Student") {
                    student_flag = "T"
                    admin_flag = "T"
                    faculty_flag = "F"
                }
                if (selectedFacultyStud == "Faculty") {
                    student_flag = "F"
                    admin_flag = "T"
                    faculty_flag = "T"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
try {
    mServices.GetInstituteData()
        .enqueue(object : Callback<APIResponse> {
            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                val result: APIResponse? = response.body()
                if (result!!.Responsecode == 204) {
                    Toast.makeText(this@AdminNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                } else {
                    listsinstz = result.Data6!!.size
                    for (i in 0..listsinstz - 1) {
                        instituteName1.add(result.Data6!![i].Course_Institute)
                    }
                }
            }
        })
}catch (ex: Exception) {

    ex.printStackTrace()
    GenericUserFunction.showApiError(this,"Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time.")
}

        var institueAdap: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this@AdminNoticeBoard,
                R.layout.support_simple_spinner_dropdown_item, instituteName1
            )
        spinner_institue.adapter = institueAdap
        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try {
                    selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                    courselist.clear()
                    mServices.GetInstituteData()
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                val result: APIResponse? = response.body()
                                if (result!!.Responsecode == 204) {
                                    Toast.makeText(this@AdminNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                                } else {
                                    val listsinstz: Int = result.Data6!!.size
                                    for (i in 0..listsinstz - 1) {
                                        if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                            val listscoursez: Int = result.Data6!![i].Courses!!.size
                                            for (j in 0..listscoursez - 1) {
                                                courselist.add(result.Data6!![i].Courses!![j].COURSE_NAME)
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    courselist.add("All")
                    var usercourselistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@AdminNoticeBoard,
                        R.layout.support_simple_spinner_dropdown_item,
                        courselist
                    )
                    spinner_courselist.adapter = usercourselistadp
                }catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(this@AdminNoticeBoard,"Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time.")
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_courselist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try {
                    selectedcourselist = p0!!.getItemAtPosition(p2) as String
                    deptlist.clear()
                    mServices.GetInstituteData()
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                val result: APIResponse? = response.body()
                                if (result!!.Responsecode == 204) {
                                    Toast.makeText(this@AdminNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                                } else {
                                    val listsinstz: Int = result.Data6!!.size
                                    for (i in 0..listsinstz - 1) {
                                        if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                            val listscoursez: Int = result.Data6!![i].Courses!!.size
                                            for (j in 0..listscoursez - 1) {
                                                if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
                                                    course_id =
                                                        result.Data6!![i].Courses!![j].COURSE_ID
                                                    val listsdeptz: Int =
                                                        result.Data6!![i].Courses!![j].Departments!!.size
                                                    for (k in 0 until listsdeptz) {
                                                        deptlist.add(result.Data6!![i].Courses!![j].Departments!![k].DEPT_NAME)
                                                    }
                                                }

                                            }
                                        }
                                    }

                                }
                            }
                        })
                    deptlist.add("All")
                    var userdeptlistadp: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this@AdminNoticeBoard,
                            R.layout.support_simple_spinner_dropdown_item, deptlist
                        )
                    spinner_deptlist.adapter = userdeptlistadp
                }catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(this@AdminNoticeBoard,"Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time.")
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_deptlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try{
                selecteddeptlist = p0!!.getItemAtPosition(p2) as String
                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(this@AdminNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                            } else {
                                val listsinstz: Int = result.Data6!!.size
                                for (i in 0..listsinstz - 1) {
                                    if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                        val listscoursez: Int = result.Data6!![i].Courses!!.size
                                        for (j in 0..listscoursez - 1) {
                                            if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
                                                val listsdeptz: Int =
                                                    result.Data6!![i].Courses!![j].Departments!!.size
                                                for (k in 0 until listsdeptz) {
                                                    if (result.Data6!![i].Courses!![j].Departments!![k].DEPT_NAME == selecteddeptlist) {
                                                        dept_id =
                                                            result.Data6!![i].Courses!![j].Departments!![k].DEPT_ID
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    })
            }catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(this@AdminNoticeBoard,"Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time.")
            }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnPickImage.setOnClickListener {
//            val intent = Intent(applicationContext, ImageUpload::class.java)
//            startActivityForResult(intent, 1)
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            println("path >> " + i.data)
            startActivityForResult(i, 100)
        }

        btnPubNotice.setOnClickListener {
            sendNoticeNew()
          //  checkFileFlag()

        }

    }


    private fun DialogWithoutFile() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_withoutfilenotice, null)
        // val txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl2)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Yes") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
            sendNotice()
        }

        dialog.setNegativeButton("No") { dialog, id ->
            println(dialog)
            println(id)
            dialog.cancel()

        }
        dialog.show()
    }

    fun noticedateclick(view: View) {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                editNoticeDate.text = """$dayOfMonth-${monthOfYear + 1}-$year"""
            }, year, month, day
        )
        dpd.show()
    }


    private fun sendNotice() {

        progressBarsubmit.visibility = View.VISIBLE
        btnPubNotice.visibility = View.GONE

        if (editNoticeDate.text.toString().isEmpty()) {
            editNoticeDate.error = "Please select notice date"
            return
        } else {
            notice_date = editNoticeDate.text.toString()
        }
        if (edit_notice_title.text.toString().isEmpty()) {
            edit_notice_title.error = "Please input notice board title"
            return
        } else {
            notice_title = edit_notice_title.text.toString()
        }
        if (edit_notice_desc.text.toString().isEmpty()) {
            edit_notice_desc.error = "Please input notice board description"
            return
        } else {
            notice_desc = edit_notice_desc.text.toString()
        }
        if (id_admin.isEmpty()) {
            edit_notice_desc.error = "Please relogin again"
            return
        } else {
            id_admin = id_admin
        }
        if (confirmStatus == "T")
        {


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
                val requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file)
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
                                mServices.UploadNotice(
                                    notice_date,
                                    notice_title,
                                    notice_desc,
                                    selectedInstituteName,
                                    selectedcourselist,
                                    selecteddeptlist,
                                    selectedNoticeType,
                                    selectedFacultyStud,
                                    confirmStatus,
                                    roleadmin,
                                    id_admin,
                                    filename,
                                    course_id,
                                    dept_id,
                                    student_flag,
                                    faculty_flag,
                                    admin_flag
                                ).enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                        dialog.dismiss()
                                        val result: APIResponse? = response.body()
//                                        Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()
                                        GenericUserFunction.showPositivePopUp(this@AdminNoticeBoard,"Notice Send Successfully")
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


//                    override fun onFailure(call: retrofit2.Call<MyResponse>, t: Throwable) {
//                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onResponse(call: retrofit2.Call<MyResponse>, response: Response<MyResponse>) {
//                        if (!response.body()!!.error) {
//                            filename = response.message()
//                                Toast.makeText(getApplicationContext(),  response.message(), Toast.LENGTH_LONG).show()
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show()
//                        }
//                    }
                })
//            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
//            var call: Call<ImageClass> = phpApiInterface.uploadImage(rTitle, rImage)
//            call.enqueue(object : Callback<ImageClass> {
//                override fun onFailure(call: Call<ImageClass>, t: Throwable) {
//                    Toast.makeText(this@InstituteNoticeBoard, "Server Response" + t.message, Toast.LENGTH_SHORT)
//                }
//
//                override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
//                    var imageClass: ImageClass? = response.body()
//                    Toast.makeText(this@InstituteNoticeBoard, imageClass!!.getResponse(), Toast.LENGTH_SHORT)
//                    filename = "http://avbrh.gearhostpreview.com/imageupload/" + imageClass.getuploadPath()
//                }
//            })
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
                    mServices.UploadNotice(
                        notice_date,
                        notice_title,
                        notice_desc,
                        selectedInstituteName,
                        selectedcourselist,
                        selecteddeptlist,
                        selectedNoticeType,
                        selectedFacultyStud,
                        confirmStatus,
                        roleadmin,
                        id_admin,
                        filename,
                        course_id,
                        dept_id,
                        student_flag,
                        faculty_flag,
                        admin_flag
                    ).enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            dialog.dismiss()
                            val result: APIResponse? = response.body()
                            GenericUserFunction.showPositivePopUp(this@AdminNoticeBoard,"Notice Send Successfully")
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
//
//                //   uploadFile(selectedImage!!,"Notice")
//                var fileUri = selectedImage!!
//                val file = File(getRealPathFromURI(fileUri))
//                //creating request body for file
//                val requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
//                val descBody = RequestBody.create(MediaType.parse("text/plain"), "Notice")
//                //The gson builder
//                val gson = GsonBuilder()
//                    .setLenient()
//                    .create()
//                //creating retrofit object
//                val retrofit = Retrofit.Builder()
//                    .baseUrl(Api.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build()
//                //creating our api
//                val api = retrofit.create(Api::class.java)
//                //creating a call and calling the upload image method
//                val call = api.uploadImage2(requestFile, descBody)
//                //finally performing the call
//                call.enqueue(object : Callback<MyResponse> {
//                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                    }
//
//                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
//                        if (!response.body()!!.error) {
//                            filename = response.body()!!.message.toString()
//
//
//
//                        }
//
//
//                    }
//
//
////                    override fun onFailure(call: retrofit2.Call<MyResponse>, t: Throwable) {
////                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
////                    }
////
////                    override fun onResponse(call: retrofit2.Call<MyResponse>, response: Response<MyResponse>) {
////                        if (!response.body()!!.error) {
////                            filename = response.message()
////                                Toast.makeText(getApplicationContext(),  response.message(), Toast.LENGTH_LONG).show()
////
////                        } else {
////                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show()
////                        }
////                    }
//                })
//            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
//            var call: Call<ImageClass> = phpApiInterface.uploadImage(rTitle, rImage)
//            call.enqueue(object : Callback<ImageClass> {
//                override fun onFailure(call: Call<ImageClass>, t: Throwable) {
//                    Toast.makeText(this@InstituteNoticeBoard, "Server Response" + t.message, Toast.LENGTH_SHORT)
//                }
//
//                override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
//                    var imageClass: ImageClass? = response.body()
//                    Toast.makeText(this@InstituteNoticeBoard, imageClass!!.getResponse(), Toast.LENGTH_SHORT)
//                    filename = "http://avbrh.gearhostpreview.com/imageupload/" + imageClass.getuploadPath()
//                }
//            })
            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }


    }

    private fun NoticeSuccessDialog() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_successnotice, null)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
            super.onBackPressed()
        }

        dialog.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
//                rImage = data!!.getStringExtra("notice_image")
//                rTitle = data.getStringExtra("notice_content")
//                RESOU_FLAG = "T"
//                btnPickImage.text = "Notice Uploaded Successfully"
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this@AdminNoticeBoard, "Nothing Selected", Toast.LENGTH_SHORT)
//            }
//        }
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

    private fun checkFileFlag() {
        if (RESOU_FLAG.equals("T")) {
            sendNotice()
        } else {
            DialogWithoutFile()
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

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(Math.floor(ratio).toInt())
        return if (k == 0)
            1
        else
            k
    }

    private fun sendNoticeNew() {
        if (editNoticeDate.text.toString().isEmpty()) {
            editNoticeDate.error = "Please select notice date"
            return
        } else {
            notice_date = editNoticeDate.text.toString()
        }
        if (edit_notice_title.text.toString().isEmpty()) {
            edit_notice_title.error = "Please input notice board title"
            return
        } else {
            notice_title = edit_notice_title.text.toString()
        }
        if (edit_notice_desc.text.toString().isEmpty()) {
            edit_notice_desc.error = "Please input notice board description"
            return
        } else {
            notice_desc = edit_notice_desc.text.toString()
        }
        if (id_admin.isEmpty()) {
            edit_notice_desc.error = "Please relogin again"
            return
        } else {
            id_admin = id_admin
        }
        if (selectedInstituteName.equals("Select institute")) {
            Toast.makeText(this, "Please select valid institute name", Toast.LENGTH_SHORT).show()
            return
        }
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
                                mServices.UploadNotice(
                                    notice_date,
                                    notice_title,
                                    notice_desc,
                                    selectedInstituteName,
                                    selectedcourselist,
                                    selecteddeptlist,
                                    selectedNoticeType,
                                    selectedFacultyStud,
                                    confirmStatus,
                                    roleadmin,
                                    id_admin,
                                    filename,
                                    course_id,
                                    dept_id,
                                    student_flag,
                                    faculty_flag,
                                    admin_flag
                                ).enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                        dialog.dismiss()
                                        val result: APIResponse? = response.body()
//                                        Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()
                                        GenericUserFunction.showPositivePopUp(this@AdminNoticeBoard,"Notice Send Successfully")
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


//                    override fun onFailure(call: retrofit2.Call<MyResponse>, t: Throwable) {
//                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
//                    }
//
//                    override fun onResponse(call: retrofit2.Call<MyResponse>, response: Response<MyResponse>) {
//                        if (!response.body()!!.error) {
//                            filename = response.message()
//                                Toast.makeText(getApplicationContext(),  response.message(), Toast.LENGTH_LONG).show()
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show()
//                        }
//                    }
                })
//            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
//            var call: Call<ImageClass> = phpApiInterface.uploadImage(rTitle, rImage)
//            call.enqueue(object : Callback<ImageClass> {
//                override fun onFailure(call: Call<ImageClass>, t: Throwable) {
//                    Toast.makeText(this@InstituteNoticeBoard, "Server Response" + t.message, Toast.LENGTH_SHORT)
//                }
//
//                override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
//                    var imageClass: ImageClass? = response.body()
//                    Toast.makeText(this@InstituteNoticeBoard, imageClass!!.getResponse(), Toast.LENGTH_SHORT)
//                    filename = "http://avbrh.gearhostpreview.com/imageupload/" + imageClass.getuploadPath()
//                }
//            })
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
                    mServices.UploadNotice(
                        notice_date,
                        notice_title,
                        notice_desc,
                        selectedInstituteName,
                        selectedcourselist,
                        selecteddeptlist,
                        selectedNoticeType,
                        selectedFacultyStud,
                        confirmStatus,
                        roleadmin,
                        id_admin,
                        filename,
                        course_id,
                        dept_id,
                        student_flag,
                        faculty_flag,
                        admin_flag
                    ).enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@AdminNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            dialog.dismiss()
                            val result: APIResponse? = response.body()
                            GenericUserFunction.showPositivePopUp(this@AdminNoticeBoard,"Notice Send Successfully")
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
//
//                //   uploadFile(selectedImage!!,"Notice")
//                var fileUri = selectedImage!!
//                val file = File(getRealPathFromURI(fileUri))
//                //creating request body for file
//                val requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
//                val descBody = RequestBody.create(MediaType.parse("text/plain"), "Notice")
//                //The gson builder
//                val gson = GsonBuilder()
//                    .setLenient()
//                    .create()
//                //creating retrofit object
//                val retrofit = Retrofit.Builder()
//                    .baseUrl(Api.BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .build()
//                //creating our api
//                val api = retrofit.create(Api::class.java)
//                //creating a call and calling the upload image method
//                val call = api.uploadImage2(requestFile, descBody)
//                //finally performing the call
//                call.enqueue(object : Callback<MyResponse> {
//                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                    }
//
//                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
//                        if (!response.body()!!.error) {
//                            filename = response.body()!!.message.toString()
//
//
//
//                        }
//
//
//                    }
//
//
////                    override fun onFailure(call: retrofit2.Call<MyResponse>, t: Throwable) {
////                        Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_LONG).show()
////                    }
////
////                    override fun onResponse(call: retrofit2.Call<MyResponse>, response: Response<MyResponse>) {
////                        if (!response.body()!!.error) {
////                            filename = response.message()
////                                Toast.makeText(getApplicationContext(),  response.message(), Toast.LENGTH_LONG).show()
////
////                        } else {
////                            Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show()
////                        }
////                    }
//                })
//            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
//            var call: Call<ImageClass> = phpApiInterface.uploadImage(rTitle, rImage)
//            call.enqueue(object : Callback<ImageClass> {
//                override fun onFailure(call: Call<ImageClass>, t: Throwable) {
//                    Toast.makeText(this@InstituteNoticeBoard, "Server Response" + t.message, Toast.LENGTH_SHORT)
//                }
//
//                override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
//                    var imageClass: ImageClass? = response.body()
//                    Toast.makeText(this@InstituteNoticeBoard, imageClass!!.getResponse(), Toast.LENGTH_SHORT)
//                    filename = "http://avbrh.gearhostpreview.com/imageupload/" + imageClass.getuploadPath()
//                }
//            })
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


