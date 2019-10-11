package com.dmims.dmims.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.model.FeedBackSchedule
import com.dmims.dmims.model.FeedBackScheduleField
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class FeedbackOptionActivity : AppCompatActivity() {
    private lateinit var btn_survey_submit: Button
    private lateinit var txt_Note: TextView
    private lateinit var tv_endDate: TextView
    private lateinit var tv_startDate: TextView

    private lateinit var spinner_FeedbackType: Spinner
    private lateinit var selected_Af_SS_Q1_Answer: String
    var feedbacknames = ArrayList<String>()
    var feedbacdsates = ArrayList<String>()
    var feedbacedates = ArrayList<String>()
    var cal = Calendar.getInstance()
    var current_date: String = "-"
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_option)
        feedbacknames.add("Select Your Choice")
        feedbacdsates.add("Select Start Date")
        feedbacedates.add("Select End date")
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        current_date = sdf.format(cal.time)

        GetCurrentDate(current_date)

        txt_Note=findViewById(R.id.txt_Note)
        spinner_FeedbackType=findViewById<Spinner>(R.id.spinner_FeedbackType)
        btn_survey_submit=findViewById<Button>(R.id.btn_survey_submit)
        tv_startDate=findViewById<TextView>(R.id.tv_startDate)
        tv_endDate=findViewById<TextView>(R.id.tv_endDate)





        var Af_SS_Q1_AnswerAdap: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, feedbacknames)
        spinner_FeedbackType.adapter = Af_SS_Q1_AnswerAdap
        spinner_FeedbackType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected_Af_SS_Q1_Answer = p0!!.getItemAtPosition(p2) as String
                if(p2 != 0) {

                    tv_startDate.text = feedbacdsates[p2]
                    tv_endDate.text = feedbacedates[p2]
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?)
            {
            }
        }


        btn_survey_submit.setOnClickListener {

            /*  val intent=Intent(this@FeedbackOptionActivity,FeedbackOptionType::class.java)
              startActivity(intent)
  */
            var feedbackSelected= spinner_FeedbackType.selectedItem.toString()
            if (feedbackSelected.equals("Select Your Choice"))
            {
                Toast.makeText(this,feedbackSelected,Toast.LENGTH_LONG).show()

            }
            else if(selected_Af_SS_Q1_Answer.equals("EXAM FEEDBACK FORMATIVE",ignoreCase = true))
            {
            val intentStudentSurvey = Intent(this@FeedbackOptionActivity, FormativeFeedbackActivity::class.java)
            startActivity(intentStudentSurvey)
            finish()
        }
        else if(selected_Af_SS_Q1_Answer.equals("EXAM FEEDBACK SUMMATIVE",ignoreCase = true))
            {
                val intentStudentSurvey = Intent(this@FeedbackOptionActivity, Student_Feedback_SummativeExam::class.java)
                startActivity(intentStudentSurvey)
                finish()
            }
            else  if(selected_Af_SS_Q1_Answer.equals("Select Your Choice",ignoreCase = true))
                {
                    GenericUserFunction.DisplayToast(this@FeedbackOptionActivity,"Please select available feedback name to proceed further.")
                }
            println("feedbackSelected  "+feedbackSelected)




        }

    }
    fun GetCurrentDate(date:String) {
        val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
        try {

            dialog.setMessage("Please Wait!!! \nwhile we are getting available feedback")
            dialog.setCancelable(false)
            dialog.show()


        println("c" + current_date)

        var phpApiInterface: PhpApiInterface =
            ApiClientPhp.getClient().create(PhpApiInterface::class.java)
        var calldate: Call<FeedBackSchedule> = phpApiInterface.CurrentDateSubmit(date)
        calldate.enqueue(object : Callback<FeedBackSchedule> {
            override fun onFailure(call: Call<FeedBackSchedule>, t: Throwable) {
                dialog.dismiss()

            }

            override fun onResponse(
                call: Call<FeedBackSchedule>,
                response: Response<FeedBackSchedule>
            ) {
                dialog.dismiss()
                val result: List<FeedBackScheduleField>? = response.body()!!.Data
                println("Response1 >> " + result!![0].id)
                if (result!![0].id == "error") {
                    Toast.makeText(
                        this@FeedbackOptionActivity,
                        "No Data in faculty Master.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    var listSize = result!!.size
                    for (i in 0..listSize - 1) {

                        feedbacknames.add(result!![i].FEEDBACK_NAME)
                        feedbacdsates.add(result!![i].START_DATE)
                        feedbacedates.add(result!![i].END_DATE)
                    }
                }
            }
        })
    }
        catch (ex : Exception)
        {
            dialog.dismiss()
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                applicationContext,
                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time.")
        }
    }
}
