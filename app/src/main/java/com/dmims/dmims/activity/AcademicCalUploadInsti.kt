package com.dmims.dmims.activity

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.TimeTableDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.TimeTableData
import com.dmims.dmims.model.TimeTableDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AcademicCalUploadInsti : AppCompatActivity()
{
    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_semester: Spinner
    private lateinit var spinner_timetabletype: Spinner
    private lateinit var spinner_yearlist: Spinner
    
    var typeoftimetbl = ArrayList<String>()


    var instituteName1 = ArrayList<String>()
    var semister = ArrayList<String>()
    var yearlist = ArrayList<String>()
    private var trsnsdlist: java.util.ArrayList<TimeTableDataRef>? = null

    private var selectedInstituteName: String? = null
    private lateinit var selectedtypeoftimetbl: String
 var institute_name: String=""
    var subtype:String=""
    var year_tt:String=""
    var sem_tt:String=""

    var listsinstz: Int = 0
    var REQUEST_CODE: Int = 0
    var type : String? = null
    private var uri: Uri? = null
    private var confirmStatus = "F"

    private lateinit var btnPickPdf: Button
    private lateinit var btnPublishCal: Button

    var pdfName:String=""
    var PdfNameHolder:String=""
    var PdfPathHolder:String=""
    lateinit var PdfID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.academic_cal_upload_insti)

        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_timetabletype=findViewById(R.id.spinner_subType)
        spinner_yearlist = findViewById(R.id.spinner_yearlist)
        spinner_semester=findViewById(R.id.spinner_semesterlist)

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

        instituteName1.add("Select Institute")

        typeoftimetbl.clear()
        typeoftimetbl.add("Select type")
        typeoftimetbl.add("Clinical")
        typeoftimetbl.add("Theory")


        try {
            GenericPublicVariable.mServices.GetInstituteData()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@AcademicCalUploadInsti, t.message, Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {
                            Toast.makeText(this@AcademicCalUploadInsti, result.Status, Toast.LENGTH_SHORT).show()
                        } else {
                            listsinstz = result.Data6!!.size
                            for (i in 0..listsinstz - 1) {
                                instituteName1.add(result.Data6!![i].Course_Institute)
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
        var institueAdap: ArrayAdapter<String> = ArrayAdapter<String>(this@AcademicCalUploadInsti,
            R.layout.support_simple_spinner_dropdown_item, instituteName1)
        spinner_institue!!.adapter = institueAdap

        spinner_institue.onItemSelectedListener=object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                institute_name=spinner_institue.selectedItem.toString()
            }
        }


        
        

        var typeoftimetblAdap: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typeoftimetbl)
        spinner_timetabletype.adapter = typeoftimetblAdap

        spinner_timetabletype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try {

                    selectedtypeoftimetbl = p0!!.getItemAtPosition(p2) as String
                    if (selectedtypeoftimetbl == "Theory") {
                            selectedtypeoftimetbl = "TH"
                        }
                        if (selectedtypeoftimetbl == "Clinical") {
                            selectedtypeoftimetbl = "CL"
                        }

                        if (selectedtypeoftimetbl == "Select type") {

                            return
                        }
                    yearlist.clear()
                    semister.clear()
                    institute_name= spinner_institue.selectedItem.toString()
                    if (institute_name.equals("SPDC") || institute_name.equals("RNPC"))
                    {
                        selectedtypeoftimetbl="TH_CL"
                    }
                    println("second insti "+spinner_institue.selectedItem.toString())
                    println("selectedtypeoftimetbl  "+selectedtypeoftimetbl)
                    var phpApiInterface: PhpApiInterface =
                        ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                    var call: Call<TimeTableData> =
                        phpApiInterface.timetable_details(institute_name, selectedtypeoftimetbl)
                    call.enqueue(object : Callback<TimeTableData> {
                        override fun onFailure(call: Call<TimeTableData>, t: Throwable) {
                            Toast.makeText(
                                this@AcademicCalUploadInsti,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<TimeTableData>,
                            response: Response<TimeTableData>
                        ) {

                            var users = java.util.ArrayList<TimeTableDataC>()

                            if (response.isSuccessful) {
                                users.clear()
                                trsnsdlist = response.body()!!.Data
                                if (trsnsdlist!![0].ID == "error") {
                                    Toast.makeText(
                                        this@AcademicCalUploadInsti,
                                        "No Data in Time Table Master.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    users.clear()
                                    var listSize = trsnsdlist!!.size


                                    for (i in 0..listSize -1) {

                                        if (trsnsdlist!![i].INSTITUTE == institute_name) {
                                            if (yearlist.contains(trsnsdlist!![i].YEAR)) {
                                                if (semister.contains(trsnsdlist!![i].SEMESTER)){

                                                }else {
                                                    semister.add((trsnsdlist!![i].SEMESTER))
                                                }

                                            } else {

                                                yearlist.add(trsnsdlist!![i].YEAR)
                                                if (semister.contains(trsnsdlist!![i].SEMESTER)){

                                                }else {
                                                    semister.add((trsnsdlist!![i].SEMESTER))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    yearlist.add("Select Year")

                    var yearAdap: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this@AcademicCalUploadInsti,
                            R.layout.support_simple_spinner_dropdown_item, yearlist
                        )
                    spinner_yearlist!!.adapter = yearAdap

                    semister.add("Select Semister")
                    var usersemlistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@AcademicCalUploadInsti,
                        R.layout.support_simple_spinner_dropdown_item,
                        semister
                    )
                    spinner_semester.adapter = usersemlistadp

                } catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@AcademicCalUploadInsti,
                        "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?)
            {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            type = "pdf"
            uri = data!!.data
            println("Uri pdf"+uri)
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

    private fun CheckValidation()
    {
        when {
            spinner_institue.selectedItem.toString().equals("Select Institute") -> Toast.makeText(applicationContext,"Please Select Institute",Toast.LENGTH_LONG).show()
            spinner_timetabletype.selectedItem.toString().equals("Select type") -> Toast.makeText(applicationContext,"Please Select Type",Toast.LENGTH_LONG).show()
            spinner_yearlist.selectedItem.toString().equals("Select Year") -> Toast.makeText(applicationContext,"Please Select Year",Toast.LENGTH_LONG).show()
            spinner_semester.selectedItem.toString().equals("Select Semister") -> Toast.makeText(applicationContext,"Please Select Semister",Toast.LENGTH_LONG).show()
            uri==null -> println("no uri")
            else -> PdfUploadFunction()
        }
        }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun PdfUploadFunction() {
        PdfPathHolder = FilePath.getPath(this, uri)
        println("PdfNameHolder "+PdfNameHolder+"PdfPathHolder "+PdfPathHolder)
        subtype=spinner_timetabletype.selectedItem.toString()
        year_tt=spinner_yearlist.selectedItem.toString()
            sem_tt=spinner_semester.selectedItem.toString()

        pdfName=institute_name+"_"+subtype.substring(0,2)+"_"+year_tt+"_"+sem_tt

        println(" data "+institute_name+subtype+year_tt+sem_tt+" "+pdfName)

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


                MultipartUploadRequest(this, PdfID, ExamMcqUpload.PDF_UPLOAD_HTTP_URL)
                    .addFileToUpload(PdfPathHolder, "pdf")
                    .addParameter("name",pdfName )
                    .addParameter("institute",pdfName )
                    .addParameter("subtype",subtype )
                    .addParameter("year",year_tt )
                    .addParameter("sem",sem_tt )
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

    companion object {

        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/pdfupload/upload.php"
    }


}
