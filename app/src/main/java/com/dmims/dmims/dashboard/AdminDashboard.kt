package com.dmims.dmims.dashboard

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import com.dmims.dmims.*
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.faculty_dashboard.*
import java.util.*
import kotlin.system.exitProcess

class AdminDashboard : AppCompatActivity() {
    lateinit var noticeboardgrid: LinearLayout
    lateinit var notification: LinearLayout
    lateinit var noticeInboxGrid: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
    lateinit var academicCalBoard: LinearLayout
    lateinit var drawerTitle: TextView
    lateinit var enrollNo: TextView
    lateinit var user_role: TextView
    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotscount: Int = 0
    private var dots: Array<ImageView?>? = null
    private var id_admin: String? = null
    private var roleadmin: String? = null
    private var dateOfAdmission: String = "-"
    lateinit var txt_Mobile: TextView
    lateinit var txt_Email: TextView
    lateinit var txt_designation: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)
        noticeboardgrid = findViewById<View>(R.id.noticeboardgrid) as LinearLayout
        notification = findViewById<View>(R.id.notification) as LinearLayout
       noticeInboxGrid = findViewById<View>(R.id.noticeInboxGrid) as LinearLayout
        helpdiloadboad = findViewById<View>(R.id.helpdiloadboad) as LinearLayout
        academicCalBoard = findViewById<View>(R.id.academic_cal_board) as LinearLayout








        drawerTitle = findViewById(R.id.drawer_title)

        user_role = findViewById(R.id.user_role)
        txt_Mobile= findViewById(R.id.txt_Mobile)
        txt_Email= findViewById(R.id.txt_Email)
        txt_designation= findViewById(R.id.txt_designation)
        enrollNo = findViewById(R.id.enroll_no)


        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        var drawer_titler = mypref.getString("key_drawer_title", null)
        id_admin = mypref.getString("Stud_id_key", null)
        roleadmin = mypref.getString("key_userrole", null)

        drawerTitle.text = drawer_titler
        user_role.text = "User: "+roleadmin
        txt_designation.text="Designation: "+mypref.getString("key_designation", null)
        txt_Email.text="E-mail: "+mypref.getString("key_email", null)
        txt_Mobile.text="MB No: "+mypref.getString("key_editmob", null)
        enrollNo.text = "ID: "+id_admin
        //Set Event
        setNoticeEvent(noticeboardgrid)
        getNotice(notification)
       setnoticeInboxGridEvent(noticeInboxGrid)
        setHelpalertEvent(helpdiloadboad)
        setacdemicCalEvent(academicCalBoard)
        // Configure action bar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "DMIMS DU"
        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {

        }
        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        // Set navigation view navigation item selected listener
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.action_calender -> {
                    val intent = Intent(this@AdminDashboard, AcademicCalender::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_noticeboard -> {
                    val intent = Intent(this@AdminDashboard, AdminNoticeBoard::class.java)
                    intent.putExtra("roleadmin", roleadmin!!)
                    intent.putExtra("id_admin", id_admin!!)
                    startActivity(intent)
                }
                R.id.action_notification -> {
                    val intent = Intent(this@AdminDashboard, Activity_Notification_Admin::class.java)
                    intent.putExtra("roleadmin", roleadmin!!)
                    intent.putExtra("id_admin", id_admin!!)
                    startActivity(intent)
                }
                R.id.action_inbox -> {
                    val intent = Intent(this@AdminDashboard, Activity_Admin_Inbox_notice::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_help -> {
                    displayhelpalert()
                }
                R.id.action_logout -> {
                    var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var editor = sharepref.edit()
                    editor.clear()
                    editor.commit()
                    val intentlogout = Intent(this@AdminDashboard, com.dmims.dmims.activity.SplashScreen::class.java)
                    startActivity(intentlogout)
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
        dotscount = viewPagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotscount)
        for (i in 0 until dotscount) {
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
                for (i in 0 until dotscount) {
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
    }

    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    //ViewPager - Page Slider
    inner class MyTimerTask : TimerTask() {
        override fun run() {
            this@AdminDashboard.runOnUiThread(Runnable {
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

    private fun setNoticeEvent(noticeboardgrid: LinearLayout) {
        noticeboardgrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminDashboard, AdminNoticeBoard::class.java)
            intent.putExtra("roleadmin", roleadmin)
            intent.putExtra("id_admin", id_admin)
            startActivity(intent)
        })
    }
    private fun getNotice(notification: LinearLayout) {
        notification.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminDashboard, Activity_Notification_Admin::class.java)
            intent.putExtra("roleadmin", roleadmin)
            intent.putExtra("id_admin", id_admin)
            startActivity(intent)
        })
    }

    private fun setacdemicCalEvent(academic_cal_board: LinearLayout) {
        academic_cal_board.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminDashboard, AcademicCalender::class.java)
            intent.putExtra("info", "Notice board")
            startActivity(intent)
        })
    }
    private fun setnoticeInboxGridEvent(noticeInboxGrid: LinearLayout) {
        noticeInboxGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminDashboard, Activity_Admin_Inbox_notice::class.java)
            intent.putExtra("info", "Notice board")
            startActivity(intent)
        })
    }

    private fun displayhelpalert() {
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

    private fun setHelpalertEvent(helpdiloadboad: LinearLayout) {
        helpdiloadboad.setOnClickListener(View.OnClickListener {
            displayhelpalert()
        })
    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        dialogBuilder.setView(dialogView)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                finishAffinity()
                exitProcess(0)

            }
            // negative button text and action
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box

        // show alert dialog
        alert.show()
    }

}