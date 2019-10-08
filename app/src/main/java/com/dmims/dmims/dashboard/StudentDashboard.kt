@file:Suppress("DEPRECATION")

package com.dmims.dmims.dashboard

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mypref
import com.dmims.dmims.R
import com.dmims.dmims.R.string
import com.dmims.dmims.R.string.Enrol_No
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import com.dmims.dmims.dataclass.FeedBackDataC
import com.dmims.dmims.model.DeptListStudData
import com.dmims.dmims.model.DeptListStudDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_dashboard.drawer_layout
import kotlinx.android.synthetic.main.activity_dashboard.navigation_view
import kotlinx.android.synthetic.main.activity_dashboard.toolbar
import kotlinx.android.synthetic.main.activity_student_dashboard.*
import java.util.*
import kotlin.system.exitProcess

class StudentDashboard : AppCompatActivity(), View.OnClickListener {

    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotsCount: Int = 0
    private var dateOfAdmission: String? = "-"
    var COURSE_ID: String? = null
    private var dots: Array<ImageView?>? = null
    private lateinit var progressDag: ProgressDialog
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)
        attendanceGrid.setOnClickListener(this)
        time_table_grid.setOnClickListener(this)
        exam_grid.setOnClickListener(this)
        appraisal_grid.setOnClickListener(this)
        greviancegrid.setOnClickListener(this)
        noticeboardgrid.setOnClickListener(this)
        notification.setOnClickListener(this)
        emergencygrid.setOnClickListener(this)
        helpdiloadboad.setOnClickListener(this)
        feedback_grid.setOnClickListener(this)
        academic_cal_board.setOnClickListener(this)
        mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        COURSE_ID = mypref!!.getString("course_id", null)
        var drawerTiler = intent.getStringExtra("NAME")
        var enrollNor = intent.getStringExtra("STUD_INFO")
        if (drawerTiler == null || enrollNor == null) {
            drawerTiler = mypref!!.getString("key_drawer_title", null)
            enrollNor = mypref!!.getString("key_enroll_no", null)
            dateOfAdmission = mypref!!.getString("key_doa", null)
        }
        drawer_title.text = drawerTiler
        enroll_no.text = getString(Enrol_No)+enrollNor
        txt_Mobile.text= "MB No: "+mypref!!.getString("key_editmob", null)
        //Set Event
        // Configure action bar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "DMIMS DU"

        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            string.drawer_open,
            string.drawer_close
        ) {

        }

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Set navigation view navigation item selected listener
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_atten -> {
                    val intent = Intent(this@StudentDashboard, Attendance::class.java)
                    val studentIdKey = mypref!!.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", studentIdKey!!.toString())
                    intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
                    startActivity(intent)
                }
                R.id.action_time_table -> {
                    val intent = Intent(this@StudentDashboard, Activity_time_table_student::class.java)
                    val studentIdKey22 = mypref!!.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", studentIdKey22?.toString())
                    startActivity(intent)
                }
                R.id.action_noticeboard -> {
                    val intent = Intent(this@StudentDashboard, Activity_student_notice::class.java)
                    val studentIdKey12 = mypref!!.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", studentIdKey12?.toString())
                    intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
                    startActivity(intent)
                }
                R.id.action_calender -> {
                    val intent = Intent(this@StudentDashboard, AcademicCalender::class.java)
                    startActivity(intent)
                }
                R.id.action_notification -> {
                    val intent = Intent(this@StudentDashboard, Activity_Notification_Student::class.java)
                    val studentIdKey13 = mypref!!.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", studentIdKey13?.toString())
                    intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
                    startActivity(intent)
                }//
                R.id.action_emergency -> {
                    val intent = Intent(this@StudentDashboard, EmergencyContact::class.java)
                    startActivity(intent)
                }
                 R.id.action_academic -> {
                     this@StudentDashboard.startActivity(
                         Intent(
                             Intent.ACTION_VIEW,
                             Uri.parse("http://103.68.25.22:81/aap")
                         )
                     )
                }
                R.id.action_exam -> {
                    val intent = Intent(this@StudentDashboard, Student_GET_UploadMCQ::class.java)
                    startActivity(intent)
                }
                R.id.action_feedback -> {
                    val intent = Intent(this@StudentDashboard, FeedbackOptionType::class.java)
                    var studentIdKey14 = mypref!!.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", studentIdKey14?.toString())
                    startActivity(intent)
                }

                R.id.action_greviance -> {
                    val intent = Intent(this@StudentDashboard, GrievanceCellBoard::class.java)
                    var studentIdKey15 = mypref!!.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", studentIdKey15?.toString())
                    startActivity(intent)
                }
                R.id.action_help -> {
                    displayHelpAlert()
                }
                R.id.action_logout -> {
                    val editor = mypref!!.edit()
                    editor.clear()
                    editor.apply()
                    val logoutIntent = Intent(this@StudentDashboard, SplashScreen::class.java)
                    startActivity(logoutIntent)
                }
            }
            // Close the drawer
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }


        //ViewPager
        viewPager = findViewById<ViewPager>(R.id.viewPager) as ViewPager
        sliderDotsPanel = findViewById<LinearLayout>(R.id.SliderDots) as LinearLayout
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        dotsCount = viewPagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotsCount)

        for (i in 0 until dotsCount) {
            dots!![i] = ImageView(this)
            dots!![i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.nonactive_dots
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            sliderDotsPanel.addView(dots!![i], params)
            dots!![i]?.setOnClickListener { viewPager.currentItem = i }

        }

        dots!![0]?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.active_dots
            )
        )
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots!![i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.nonactive_dots
                        )
                    )
                }
                dots!![position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dots
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        val timer = Timer()
        timer.scheduleAtFixedRate(MyTimerTask(), 2000, 4000)
        //ViewPager
        progressDag = ProgressDialog(this)
        progressDag.setTitle("Please wait!!")
        progressDag.setMessage("Loading....")
        progressDag.show()
        progressDag.setCancelable(false)
        getInsDetails()
    }

    private fun getInsDetails()
    {
        val phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java)
        val call3: Call<DeptListStudDataRef> = phpApiInterface.InstDetails(COURSE_ID!!)
        call3.enqueue(object : Callback<DeptListStudDataRef> {
            override fun onFailure(call: Call<DeptListStudDataRef>, t: Throwable) {
                Toast.makeText(this@StudentDashboard, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<DeptListStudDataRef>, response: Response<DeptListStudDataRef>) {
                if (response.isSuccessful) {
                    val result: DeptListStudDataRef? = response.body()
                    if (result!!.toString().isNotEmpty()) {
                        txt_Institute.text = "Ins Name: " + result.COURSE_INSTITUTE
                        txt_Course.text = "Course Name: " + result.COURSE_NAME
                        val editor = mypref!!.edit()
                        editor.putString("key_institute_stud", result.COURSE_INSTITUTE)
                        editor.putString("key_stud_course", result.COURSE_INSTITUTE)
                        editor.apply()
                    }
                }
            }
        })
        progressDag.dismiss()
    }

    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    //ViewPager - Page Slider
    inner class MyTimerTask : TimerTask() {
        override fun run() {
            this@StudentDashboard.runOnUiThread(Runnable {
                if (viewPager.currentItem == 0) {
                    viewPager.currentItem = 1
                } else if (viewPager.currentItem == 1) {
                    viewPager.currentItem = 2
                } else if (viewPager.currentItem == 2) {
                    viewPager.currentItem = 3
                } else if (viewPager.currentItem == 3) {
                    viewPager.currentItem = 4
                } else if (viewPager.currentItem == 4) {
                    viewPager.currentItem = 5
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem = 0
                }
            })
        }
    }

    //ViewPager end
       private fun displayHelpAlert() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
        }
        dialog.show()
    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        dialogBuilder.setView(dialogView)
            // set message of alert dialog
            // dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes") { dialog, id ->
                println(dialog)
                println(id)
                finishAffinity()
                exitProcess(0)
            }
            // negative button text and action
            .setNegativeButton("No") { dialog, id ->
                println(dialog)
                println(id)
                dialog.cancel()
            }
        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        // show alert dialog
        alert.show()
    }

    private fun dialogCall(Title:String ) {
        val builder = android.app.AlertDialog.Builder(this@StudentDashboard)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        var txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl)
        builder.setView(dialogView)
        //builder.setTitle("An Update is Available")
        txtviewlbl.text = Title
        builder.setPositiveButton("Ok") { dialog, which ->
            //Click button action
                       dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.attendanceGrid -> {
                val intent = Intent(this@StudentDashboard, Attendance::class.java)
                val studentIdKey = mypref!!.getString("Stud_id_key", null)
                intent.putExtra("stud_k", studentIdKey?.toString())
                intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
                startActivity(intent)
            }
            R.id.time_table_grid -> {
                val intent = Intent(this@StudentDashboard, Activity_time_table_student::class.java)
                val studentIdKey = mypref!!.getString("Stud_id_key", null)
                intent.putExtra("stud_k", studentIdKey?.toString())
                startActivity(intent)
            }
            R.id.feedback_grid -> {
                val intent = Intent(this@StudentDashboard, FeedbackOptionActivity::class.java)
                val studentIdKey = mypref!!.getString("Stud_id_key", null)
                intent.putExtra("stud_k", studentIdKey?.toString())
                startActivity(intent)
            }
            R.id.greviancegrid -> {
                val intent = Intent(this@StudentDashboard, GrievanceCellBoard::class.java)
                val studentIdKey = mypref!!.getString("Stud_id_key", null)
                intent.putExtra("stud_k", studentIdKey?.toString())
                startActivity(intent)
            }
            R.id.academic_cal_board -> {
                val intent = Intent(this@StudentDashboard, AcademicCalender::class.java)
                startActivity(intent)
            }
            R.id.helpdiloadboad -> {
                displayHelpAlert()
            }
            R.id.appraisal_grid -> {
                this@StudentDashboard.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://103.68.25.22:81/aap")
                    )
                )
            }
            R.id.emergencygrid -> {
                val intent = Intent(this@StudentDashboard, EmergencyContact::class.java)
                startActivity(intent)
            }
            R.id.exam_grid -> {
                val intent = Intent(this@StudentDashboard, Student_GET_UploadMCQ::class.java)
                startActivity(intent)
            }
            R.id.notification -> {
                val intent = Intent(this@StudentDashboard, Activity_Notification_Student::class.java)
                val studentIdKey = mypref!!.getString("Stud_id_key", null)
                intent.putExtra("stud_k", studentIdKey?.toString())
                intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
                startActivity(intent)
            }
            R.id.noticeboardgrid -> {
                val intent = Intent(this@StudentDashboard, Activity_student_notice::class.java)
                val studentIdKey = mypref!!.getString("Stud_id_key", null)
                intent.putExtra("stud_k", studentIdKey?.toString())
                intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
                startActivity(intent)
            }
            else -> {
                // else condition
            }
        }
    }
}