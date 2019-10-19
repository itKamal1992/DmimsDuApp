package com.dmims.dmims.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.dashboard.*
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.NewUserInsert
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var edit_mob: EditText
    private lateinit var edit_password: EditText
    private lateinit var mServices: IMyAPI
    private lateinit var btn_login: Button
    private var set_status_frm: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //reteriveData()
        setContentView(R.layout.activity_main)

        mServices = Common.getAPI()
        val mob = intent.getStringExtra("edit_mobotp")
        val otp_gen_user = intent.getStringExtra("otp_gen_user")

//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
        //  txt_register = findViewById<TextView>(R.id.txt_register)
        edit_mob = findViewById(R.id.edit_mob)
        edit_password = findViewById(R.id.edit_password)
        btn_login = findViewById(R.id.btn_login)
        edit_mob.setText(mob)

        if (otp_gen_user.equals("-")) {
            set_status_frm = "myDb"
        } else {
            set_status_frm = "Orcl"
            edit_password.setText(otp_gen_user)
        }

        btn_login.setOnClickListener {
            if (set_status_frm.equals("Orcl")) {
                authenticateUser(edit_mob.text.toString(), edit_password.text.toString())
            }
            if (set_status_frm.equals("myDb")) {
                authenticateUsermyDb(edit_mob.text.toString(), edit_password.text.toString())
            }

        }
    }


    private fun authenticateUser(mobile: String, password: String) {
        try {
            val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are authenticating your details")
            dialog.setCancelable(false)
            dialog.show()
            mServices.VerifyOtp(mobile, password)
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {
                            dialog.dismiss()
                            Toast.makeText(this@MainActivity, result.Status, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            if (result.Data1?.USER_ROLE        != null && result.Data1?.USER_ROLE.equals(
                                    "Student",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, StudentDashboard::class.java)
                                println(" course >>> " + result.Data1!!.COURSE_ID!!.get(0))
                                saveData(
                                    result.Data1!!.USER_ROLE,
                                    result.Data1!!.NAME,
                                    result.Data1!!.STUDENTID + " / " + result.Data1!!.PUNCH_ID,
                                    result.Data1!!.STUDENTID,
                                    result.Data1!!.DOA,
                                    result.Data1!!.COURSE_ID!!.get(0),
                                    result.Data1!!.ROLL_NO

                                )
                                intent.putExtra("NAME", result.Data1!!.NAME)
                                intent.putExtra(
                                    "STUD_INFO",
                                    result.Data1!!.STUDENTID + " / " + result.Data1!!.PUNCH_ID
                                )
                                intent.putExtra("STUD_ID_KEY", result.Data1!!.STUDENTID)
                                startActivity(intent)
                            }
                            if (result.Data2?.USER_ROLE != null && result.Data2?.USER_ROLE.equals("Parent", ignoreCase = true))
                            {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, StudentDashboard::class.java)

                                saveData(
                                    result.Data2!!.USER_ROLE,
                                    result.Data2!!.GARDIAN_NAME,
                                    result.Data2!!.NAME,
                                    result.Data2!!.STUDENTID,
                                    "-",
                                    result.Data1!!.COURSE_ID!!.get(0),
                                    "-"
                                )
                                intent.putExtra("NAME", result.Data2!!.GARDIAN_NAME)
                                intent.putExtra("STUD_INFO", result.Data2!!.NAME)
                                intent.putExtra("STUD_ID_KEY", result.Data2!!.STUDENTID)
                                startActivity(intent)
                            }
                            if (result.Data3?.USER_ROLE != null && result.Data3?.USER_ROLE.equals(
                                    "Faculty",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, FacultyDashboard::class.java)
                                saveData(
                                    result.Data3!!.USER_ROLE,
                                    result.Data3!!.NAME,
                                    result.Data3!!.FACULTY_ID,
                                    "-",
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "ADMIN",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, AdminDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    "",
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-",
                                    result.Data4!!.DESIGNATION,
                                    result.Data4!!.EMAIL
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "INSTITUTE",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                println(" result >>> " + result.Data4)

                                val intent =
                                    Intent(applicationContext, InstituteDashboard::class.java)
                                saveData(
//                                result.Data4!!.USER_ROLE,
//                                result.Data4!!.ADMIN_NAME,
//                                result.Data4!!.ID,
//                                result.Data4!!.ID,
                                    result.Data4!!.USER_ROLE,//
                                    result.Data4!!.ADMIN_NAME,
                                    "",
                                    result.Data4!!.ID,
                                    "-",
                                    result.Data4!!.COURSE_ID,
                                    result.Data4!!.INSTITUTE_NAME,
                                    result.Data4!!.DESIGNATION,
                                    result.Data4!!.EMAIL

//                                "PUNCH_ID": "13026",
//                            "NAME": "PRATIK RAJABHAU TAKSALE",
//                            "COURSE_ID": [
//                            "C000006"
//                            ],
//                            "SEM_ID": "S000024",
//                            "ROLL_NO": "2013-026",
//                            "ENROLL_YEAR": 2013,
//                            "BATCH": "C",
//                            "ENROLLMENT_NO": "I-5700",
//                            "STUDENTID": 336,
//                            "UNIVERSITY": "DMIMS(DU)",
//                            "MOBILE_NO": "9860928368",
//                            "USER_ROLE": "Student"

                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "REGISTRAR",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, RegisterarCellDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "GREVIANCE_CELL",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, GreivanceCellDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "CONVENER",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, AdminDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "COCONVENER",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, AdminDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "EXAMINCHARGE",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, ExamCellDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    "",
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-",
                                    result.Data4!!.DESIGNATION,
                                    result.Data4!!.EMAIL
                                )
                                startActivity(intent)
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


    private fun authenticateUsermyDb(mobile: String, password: String) {
        try {
            val dialog2: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
            dialog2.setMessage("Please Wait!!! \nwhile we are authenticating your details")
            dialog2.setCancelable(false)
            dialog2.show()

            var phpApiInterface: PhpApiInterface =
                ApiClientPhp.getClient()
                    .create(PhpApiInterface::class.java)
            var call3: Call<NewUserInsert> =
                phpApiInterface.VerifyOtpMob(
                    mobile, password
                )

            call3.enqueue(object :
                Callback<NewUserInsert> {
                override fun onFailure(
                    call: Call<NewUserInsert>,
                    t: Throwable
                ) {
                    dialog2.dismiss()
                    Toast.makeText(
                        this@MainActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<NewUserInsert>,
                    response: Response<NewUserInsert>
                ) {
                    dialog2.dismiss()
                    val result3: NewUserInsert? =
                        response.body()
                    result3!!.response
                    var arrSplit: List<String> = result3!!.response.split("SEPARATOR")
                    if (arrSplit[0].equals("1")) {
                        dialog2.dismiss()
//NAME,STUDENT_ID,COURSE_ID,ROLLNO
                        val intent = Intent(applicationContext, StudentDashboard::class.java)
                        saveData(
                            "Student",
                            arrSplit[1],
                            arrSplit[2],
                            arrSplit[2],
                            "-",
                            arrSplit[3],
                            arrSplit[4]

                        )
                        intent.putExtra("NAME", arrSplit[1])
                        intent.putExtra(
                            arrSplit[2],
                            arrSplit[2]
                        )
                        intent.putExtra("STUD_ID_KEY", arrSplit[2])
                        startActivity(intent)


                    }
                    if (arrSplit[0].equals("2")) {
                        //Toast.makeText(this@RegActivity, result.Status, Toast.LENGTH_SHORT).show()
                        GenericUserFunction.showNegativePopUp(
                            this@MainActivity,
                            "Please Input Correct Mobile number and Password"
                        )
                        dialog2.dismiss()
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

    private fun saveData(
        USER_ROLE: String,
        NAME: String,
        STUD_INFO: String,
        STUD_ID: String,
        DOA: String,
        courseid_1: String,
        roll_no: String
    ) {
        if (edit_mob.text.isEmpty()) {
            edit_mob.error = "Please enter Mobile Number"
            return
        }
        if (edit_password.text.isEmpty()) {
            edit_password.error = "Please enter password"
            return
        }
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()
        editor.putString("key_editmob", edit_mob.text.toString().trim())
        editor.putString("key_password", edit_password.text.toString().trim())
        editor.putString("key_drawer_title", NAME)
        editor.putString("key_enroll_no", STUD_INFO)
        editor.putString("Stud_id_key", STUD_ID)
        editor.putString("key_userrole", USER_ROLE)
        editor.putString("key_doa", DOA)
        editor.putString("course_id", courseid_1)
        editor.putString("roll_no", roll_no)
        editor.apply()
    }

    private fun saveData(
        USER_ROLE: String,
        NAME: String,
        ExtraField_1: String,
        STUD_ID: String,
        ExtraField_2: String,
        courseid_1: String,
        Institute: String,
        Designation: String,
        Email: String
    ) {
        if (edit_mob.text.isEmpty()) {
            edit_mob.error = "Please enter Mobile Number"
            return
        }
        if (edit_password.text.isEmpty()) {
            edit_password.error = "Please enter password"
            return
        }
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()
        editor.putString("Stud_id_key", STUD_ID)
        editor.putString("key_drawer_title", NAME)
        editor.putString("key_editmob", edit_mob.text.toString().trim())
        editor.putString("key_userrole", USER_ROLE)
        editor.putString("key_institute", Institute)
        editor.putString("key_email", Email)
        editor.putString("key_designation", Designation)
        editor.putString("course_id", courseid_1)
        editor.putString("key_password", edit_password.text.toString().trim())

        editor.putString("key_extraField_1", ExtraField_1)
        editor.putString("key_extraField_2", ExtraField_2)

        editor.apply()
    }
}

