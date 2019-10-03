package com.dmims.dmims.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.FeedBackDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.DeptListStudData
import com.dmims.dmims.model.DeptListStudDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class FormativeFeedbackActivity : AppCompatActivity() {
    lateinit var spinner_FeedbackFaculty: Spinner
    private lateinit var selected_spinner_FeedbackFaculty: String

    lateinit var Ex_S1_Rdg1: RadioGroup
    lateinit var Ex_S1_Rdg2: RadioGroup
    lateinit var Ex_S1_Rdg3: RadioGroup
    lateinit var Ex_S1_Rdg4: RadioGroup
    lateinit var Ex_S1_Rdg5: RadioGroup

    lateinit var Ex_S2_Rdg1: RadioGroup
    lateinit var Ex_S2_Rdg2: RadioGroup

    lateinit var Ex_S2_Rdg3: RadioGroup
    lateinit var S2_Rdg3_Btn_Q1_Yes: RadioButton
    lateinit var S2_Rdg3_Btn_Q1_No: RadioButton
    lateinit var Af_Ex_S2_Q3_answer: EditText

    lateinit var Ex_S2_Rdg4: RadioGroup

    lateinit var Ex_S2_Rdg5_q1: RadioGroup
    lateinit var Af_Ex_S2_Q5_q1_answer: EditText

    lateinit var Ex_S2_Rdg5_q2: RadioGroup
    lateinit var Af_Ex_S2_Q5_q2_answer: EditText

    lateinit var Ex_S2_Rdg5_q3: RadioGroup
    lateinit var Af_Ex_S2_Q5_q3_answer: EditText

    lateinit var Ex_S2_Rdg5_q4: RadioGroup
    lateinit var Af_Ex_S2_Q5_q4_answer: EditText

    lateinit var Af_Ex_S3_suggest: EditText

    lateinit var Ex_S1_Rdg1_radioButton_Text: String
    lateinit var Ex_S1_Rdg2_radioButton_Text: String
    lateinit var Ex_S1_Rdg3_radioButton_Text: String
    lateinit var Ex_S1_Rdg4_radioButton_Text: String
    lateinit var Ex_S1_Rdg5_radioButton_Text: String

    lateinit var Ex_S2_Rdg1_radioButton_Text: String
    lateinit var Ex_S2_Rdg2_radioButton_Text: String
    lateinit var Ex_S2_Rdg3_radioButton_Text: String
    lateinit var Ex_S2_Rdg4_radioButton_Text: String

    lateinit var Ex_S2_Rdg5_Rdg5_q1_radioButton_Text: String
    lateinit var Ex_S2_Rdg5_Rdg5_q2_radioButton_Text: String
    lateinit var Ex_S2_Rdg5_Rdg5_q3_radioButton_Text: String
    lateinit var Ex_S2_Rdg5_Rdg5_q4_radioButton_Text: String

    lateinit var Af_Ex_S2_Q3_Text: String
    lateinit var Af_Ex_S2_Q5_q1_Text: String
    lateinit var Af_Ex_S2_Q5_q2_Text: String
    lateinit var Af_Ex_S2_Q5_q3_Text: String
    lateinit var Af_Ex_S2_Q5_q4_Text: String
    lateinit var Af_Ex_S3_suggest_Text: String

    lateinit var btn_Af_Ex_Submit: Button

    lateinit var Course_ID: String
    lateinit var Stud_ID: String
    lateinit var Stud_Name: String
    lateinit var Stud_Roll_No: String
    lateinit var Stud_Institute: String
    lateinit var CurrentDate: String

    lateinit var Course: String
    lateinit var Institute: String

    private var Deptlist: ArrayList<DeptListStudDataRef>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formative_feedback)

//        spinner_FeedbackFaculty=findViewById<Spinner>(R.id.spinner_FeedbackFaculty)
//        Af_Ex_S1_Q1=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S1_Q2=findViewById<TextView>(R.id.Af_Ex_S1_Q2)
//        Af_Ex_S1_Q3=findViewById<TextView>(R.id.Af_Ex_S1_Q3)
//        Af_Ex_S1_Q4=findViewById<TextView>(R.id.Af_Ex_S1_Q4)
//        Af_Ex_S1_Q5=findViewById<TextView>(R.id.Af_Ex_S1_Q5)
//        Af_Ex_S2_Q6=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S2_Q7=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S2_Q8=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S2_Q9=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S3_suggest=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S2_Q11=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
//        Af_Ex_S2_Q12=findViewById<TextView>(R.id.Af_Ex_S1_Q1)
        spinner_FeedbackFaculty = findViewById(R.id.spinner_FeedbackFaculty)
        Ex_S1_Rdg1 = findViewById(R.id.Ex_S1_Rdg1)
        Ex_S1_Rdg2 = findViewById(R.id.Ex_S1_Rdg2)
        Ex_S1_Rdg3 = findViewById(R.id.Ex_S1_Rdg3)
        Ex_S1_Rdg4 = findViewById(R.id.Ex_S1_Rdg4)
        Ex_S1_Rdg5 = findViewById(R.id.Ex_S1_Rdg5)

        Ex_S2_Rdg1 = findViewById(R.id.Ex_S2_Rdg1)
        Ex_S2_Rdg2 = findViewById(R.id.Ex_S2_Rdg2)

        Ex_S2_Rdg3 = findViewById(R.id.Ex_S2_Rdg3)
        S2_Rdg3_Btn_Q1_Yes = findViewById(R.id.S2_Rdg3_Btn_Q1_Yes)
        S2_Rdg3_Btn_Q1_No = findViewById(R.id.S2_Rdg3_Btn_Q1_No)
        Af_Ex_S2_Q3_answer = findViewById(R.id.Af_Ex_S2_Q3_answer)
        Af_Ex_S2_Q3_answer.visibility = View.VISIBLE

        Ex_S2_Rdg4 = findViewById(R.id.Ex_S2_Rdg4)

        Ex_S2_Rdg5_q1 = findViewById(R.id.Ex_S2_Rdg5_q1)
        Ex_S2_Rdg5_q2 = findViewById(R.id.Ex_S2_Rdg5_q2)
        Ex_S2_Rdg5_q3 = findViewById(R.id.Ex_S2_Rdg5_q3)
        Ex_S2_Rdg5_q4 = findViewById(R.id.Ex_S2_Rdg5_q4)

        Af_Ex_S2_Q5_q1_answer = findViewById(R.id.Af_Ex_S2_Q5_q1_answer)
        Af_Ex_S2_Q5_q2_answer = findViewById(R.id.Af_Ex_S2_Q5_q2_answer)
        Af_Ex_S2_Q5_q3_answer = findViewById(R.id.Af_Ex_S2_Q5_q3_answer)
        Af_Ex_S2_Q5_q4_answer = findViewById(R.id.Af_Ex_S2_Q5_q4_answer)

        Af_Ex_S3_suggest = findViewById(R.id.Af_Ex_S3_suggest)

        Af_Ex_S2_Q5_q1_answer.visibility = View.GONE
        Af_Ex_S2_Q5_q2_answer.visibility = View.GONE
        Af_Ex_S2_Q5_q3_answer.visibility = View.GONE
        Af_Ex_S2_Q5_q4_answer.visibility = View.GONE

        var cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        CurrentDate = sdf.format(cal.time).toString()


        Ex_S2_Rdg3.setOnCheckedChangeListener { radioGroup, i ->
            println(radioGroup)
            var Ex_S2_Rdg3_radioButton = findViewById<RadioButton>(i)
            Toast.makeText(this, "" + Ex_S2_Rdg3_radioButton.text, Toast.LENGTH_SHORT).show()
            Af_Ex_S2_Q3_answer.text.clear()
            if (Ex_S2_Rdg3_radioButton.text.equals("Yes")) {
                Af_Ex_S2_Q3_answer.visibility = View.VISIBLE
            } else {
                Af_Ex_S2_Q3_answer.visibility = View.GONE
            }

        }

        Ex_S2_Rdg5_q1.setOnCheckedChangeListener { radioGroup, i ->
            println(radioGroup)
            var Ex_S2_Rdg5_q1_radioButton = findViewById<RadioButton>(i)
            Toast.makeText(this, "" + Ex_S2_Rdg5_q1_radioButton.text, Toast.LENGTH_SHORT).show()
            Af_Ex_S2_Q5_q1_answer.text.clear()
            if (Ex_S2_Rdg5_q1_radioButton.text.equals("Yes")) {
                Af_Ex_S2_Q5_q1_answer.visibility = View.VISIBLE
            } else {
                Af_Ex_S2_Q5_q1_answer.visibility = View.GONE
            }

        }

        Ex_S2_Rdg5_q2.setOnCheckedChangeListener { radioGroup, i ->
            println(radioGroup)
            var Ex_S2_Rdg5_q2_radioButton = findViewById<RadioButton>(i)
            Toast.makeText(this, "" + Ex_S2_Rdg5_q2_radioButton.text, Toast.LENGTH_SHORT).show()
            Af_Ex_S2_Q5_q2_answer.text.clear()
            if (Ex_S2_Rdg5_q2_radioButton.text.equals("Yes")) {
                Af_Ex_S2_Q5_q2_answer.visibility = View.VISIBLE
            } else {
                Af_Ex_S2_Q5_q2_answer.visibility = View.GONE
            }

        }

        Ex_S2_Rdg5_q3.setOnCheckedChangeListener { radioGroup, i ->
            println(radioGroup)
            var Ex_S2_Rdg5_q3_radioButton = findViewById<RadioButton>(i)
            Toast.makeText(this, "" + Ex_S2_Rdg5_q3_radioButton.text, Toast.LENGTH_SHORT).show()
            Af_Ex_S2_Q5_q3_answer.text.clear()
            if (Ex_S2_Rdg5_q3_radioButton.text.equals("Yes")) {
                Af_Ex_S2_Q5_q3_answer.visibility = View.VISIBLE
            } else {
                Af_Ex_S2_Q5_q3_answer.visibility = View.GONE
            }

        }

        Ex_S2_Rdg5_q4.setOnCheckedChangeListener { radioGroup, i ->
            println(radioGroup)
            var Ex_S2_Rdg5_q4_radioButton = findViewById<RadioButton>(i)
            Toast.makeText(this, "" + Ex_S2_Rdg5_q4_radioButton.text, Toast.LENGTH_SHORT).show()
            Af_Ex_S2_Q5_q4_answer.text.clear()
            if (Ex_S2_Rdg5_q4_radioButton.text.equals("Yes")) {
                Af_Ex_S2_Q5_q4_answer.visibility = View.VISIBLE
            } else {
                Af_Ex_S2_Q5_q4_answer.visibility = View.GONE
            }

        }



        btn_Af_Ex_Submit = findViewById(R.id.btn_Af_Ex_Submit)

        var Ex_FacultyAdap: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.FeedbackFaculty)
        )
        spinner_FeedbackFaculty.adapter = Ex_FacultyAdap
        spinner_FeedbackFaculty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected_spinner_FeedbackFaculty = p0!!.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        btn_Af_Ex_Submit.setOnClickListener {

            if (Validate()) {
                val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                Course_ID = mypref.getString("key_stud_course", null)
                Stud_ID = mypref.getString("Stud_id_key", null)
                Stud_Name = mypref.getString("key_drawer_title", null)
                Stud_Roll_No = mypref.getString("key_enroll_no", null)
                Stud_Institute = mypref.getString("key_institute_stud", null)

                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
                var ivNegClose1: ImageView = CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button = CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                tvMsg.text = "Do you want to Submit your Feedback"
//    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                        PhpApiInterface::class.java
                    )
                    var call3: Call<DeptListStudData> = phpApiInterface.InstDetailsStudYear(Course_ID)
                    call3.enqueue(object : Callback<DeptListStudData> {
                        override fun onFailure(call: Call<DeptListStudData>, t: Throwable) {
                            Toast.makeText(this@FormativeFeedbackActivity, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<DeptListStudData>, response: Response<DeptListStudData>) {
                            var users = ArrayList<FeedBackDataC>()

                            if (response.isSuccessful) {
                                users.clear()
                                Deptlist = response.body()!!.Data
                                if (Deptlist!!.size > 0)
                                    Institute = Deptlist!![0].COURSE_INSTITUTE
                                Course = Deptlist!![0].COURSE_NAME

                            }
                            CustDialog.dismiss()
                            Update()
                        }


                    })


                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog.dismiss()
                }
                ivNegClose1.setOnClickListener {
                    CustDialog.dismiss()
                }
                CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog.show()

            }
//            // find the radiobutton by returned id
//            radioButton = (RadioButton) findViewById(selectedId);
//            Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show()
        }

    }

    fun Validate(): Boolean {
        try {
            var Ex_S1_Rdg1_selectedId = Ex_S1_Rdg1.checkedRadioButtonId
            var Ex_S1_Rdg1_radioButton = findViewById<RadioButton>(Ex_S1_Rdg1_selectedId)
            Ex_S1_Rdg1_radioButton_Text = Ex_S1_Rdg1_radioButton.text.toString()

            var Ex_S1_Rdg2_selectedId = Ex_S1_Rdg2.checkedRadioButtonId
            var Ex_S1_Rdg2_radioButton = findViewById<RadioButton>(Ex_S1_Rdg2_selectedId)
            Ex_S1_Rdg2_radioButton_Text = Ex_S1_Rdg2_radioButton.text.toString()

            var Ex_S1_Rdg3_selectedId = Ex_S1_Rdg3.checkedRadioButtonId
            var Ex_S1_Rdg3_radioButton = findViewById<RadioButton>(Ex_S1_Rdg3_selectedId)
            Ex_S1_Rdg3_radioButton_Text = Ex_S1_Rdg3_radioButton.text.toString()

            var Ex_S1_Rdg4_selectedId = Ex_S1_Rdg4.checkedRadioButtonId
            var Ex_S1_Rdg4_radioButton = findViewById<RadioButton>(Ex_S1_Rdg4_selectedId)
            Ex_S1_Rdg4_radioButton_Text = Ex_S1_Rdg4_radioButton.text.toString()


            var Ex_S1_Rdg5_selectedId = Ex_S1_Rdg5.checkedRadioButtonId
            var Ex_S1_Rdg5_radioButton = findViewById<RadioButton>(Ex_S1_Rdg5_selectedId)
            Ex_S1_Rdg5_radioButton_Text = Ex_S1_Rdg5_radioButton.text.toString()

//                    Code start for section 2

            var Ex_S2_Rdg1_selectedId = Ex_S2_Rdg1.checkedRadioButtonId
            var Ex_S2_Rdg1_radioButton = findViewById<RadioButton>(Ex_S2_Rdg1_selectedId)
            Ex_S2_Rdg1_radioButton_Text = Ex_S2_Rdg1_radioButton.text.toString()

            var Ex_S2_Rdg2_selectedId = Ex_S2_Rdg2.checkedRadioButtonId
            var Ex_S2_Rdg2_radioButton = findViewById<RadioButton>(Ex_S2_Rdg2_selectedId)
            Ex_S2_Rdg2_radioButton_Text = Ex_S2_Rdg2_radioButton.text.toString()


            var Ex_S2_Rdg3_selectedId = Ex_S2_Rdg3.checkedRadioButtonId
            var Ex_S2_Rdg3_radioButton = findViewById<RadioButton>(Ex_S2_Rdg3_selectedId)
            Ex_S2_Rdg3_radioButton_Text = Ex_S2_Rdg3_radioButton.text.toString()
            if (Ex_S2_Rdg3_radioButton.text.equals("Yes")) {
                if ((Af_Ex_S2_Q3_answer.text.length > 0) || (!Af_Ex_S2_Q3_answer.text.equals(null))) {
                    Af_Ex_S2_Q3_Text = Af_Ex_S2_Q3_answer.text.toString()

                } else {
                    Af_Ex_S2_Q3_Text = "-"
                }
            } else {
                Af_Ex_S2_Q3_Text = "-"
            }

            var Ex_S2_Rdg4_selectedId = Ex_S2_Rdg4.checkedRadioButtonId
            var Ex_S2_Rdg4radioButton = findViewById<RadioButton>(Ex_S2_Rdg4_selectedId)
            Ex_S2_Rdg4_radioButton_Text = Ex_S2_Rdg4radioButton.text.toString()

            var Ex_S2_Rdg5_q1_selectedId = Ex_S2_Rdg5_q1.checkedRadioButtonId
            var Ex_S2_Rdg5_q1_radioButton = findViewById<RadioButton>(Ex_S2_Rdg5_q1_selectedId)
            Ex_S2_Rdg5_Rdg5_q1_radioButton_Text = Ex_S2_Rdg5_q1_radioButton.text.toString()

            if (Ex_S2_Rdg5_q1_radioButton.text.equals("Yes")) {
                if ((Af_Ex_S2_Q5_q1_answer.text.length > 0) || (!Af_Ex_S2_Q5_q1_answer.text.equals(null))) {
                    Af_Ex_S2_Q5_q1_Text = Af_Ex_S2_Q5_q1_answer.text.toString()

                } else {
                    Af_Ex_S2_Q5_q1_Text = "-"
                }
            } else {
                Af_Ex_S2_Q5_q1_Text = "-"
            }

            var Ex_S2_Rdg5_q2_selectedId = Ex_S2_Rdg5_q2.checkedRadioButtonId
            var Ex_S2_Rdg5_q2_radioButton = findViewById<RadioButton>(Ex_S2_Rdg5_q2_selectedId)
            Ex_S2_Rdg5_Rdg5_q2_radioButton_Text = Ex_S2_Rdg5_q2_radioButton.text.toString()
            if (Ex_S2_Rdg5_q2_radioButton.text.equals("Yes")) {

                if ((Af_Ex_S2_Q5_q2_answer.text.length > 0) || (!Af_Ex_S2_Q5_q2_answer.text.equals(null))) {
                    Af_Ex_S2_Q5_q2_Text = Af_Ex_S2_Q5_q2_answer.text.toString()

                } else {
                    Af_Ex_S2_Q5_q2_Text = "-"
                }
            } else {
                Af_Ex_S2_Q5_q2_Text = "-"
            }

            var Ex_S2_Rdg5_q3_selectedId = Ex_S2_Rdg5_q3.checkedRadioButtonId
            var Ex_S2_Rdg5_q3_radioButton = findViewById<RadioButton>(Ex_S2_Rdg5_q3_selectedId)
            Ex_S2_Rdg5_Rdg5_q3_radioButton_Text = Ex_S2_Rdg5_q3_radioButton.text.toString()
            if (Ex_S2_Rdg5_q3_radioButton.text.equals("Yes")) {

                if ((Af_Ex_S2_Q5_q3_answer.text.length > 0) || (!Af_Ex_S2_Q5_q3_answer.text.equals(null))) {
                    Af_Ex_S2_Q5_q3_Text = Af_Ex_S2_Q5_q3_answer.text.toString()

                } else {
                    Af_Ex_S2_Q5_q3_Text = "-"
                }
            } else {
                Af_Ex_S2_Q5_q3_Text = "-"
            }


            var Ex_S2_Rdg5_q4_selectedId = Ex_S2_Rdg5_q4.checkedRadioButtonId
            var Ex_S2_Rdg5_q4_radioButton = findViewById<RadioButton>(Ex_S2_Rdg5_q4_selectedId)
            Ex_S2_Rdg5_Rdg5_q4_radioButton_Text = Ex_S2_Rdg5_q4_radioButton.text.toString()
            if (Ex_S2_Rdg5_q4_radioButton.text.equals("Yes")) {

                if ((Af_Ex_S2_Q5_q4_answer.text.length > 0) || (!Af_Ex_S2_Q5_q4_answer.text.equals(null))) {
                    Af_Ex_S2_Q5_q4_Text = Af_Ex_S2_Q5_q4_answer.text.toString()

                } else {
                    Af_Ex_S2_Q5_q4_Text = "-"
                }
            } else {
                Af_Ex_S2_Q5_q4_Text = "-"
            }

            if ((Af_Ex_S3_suggest.text.length > 0) || (!Af_Ex_S3_suggest.text.equals(null))) {
                Af_Ex_S3_suggest_Text = Af_Ex_S3_suggest.text.toString()
            } else {
                Af_Ex_S3_suggest_Text = "-"
            }

            if (selected_spinner_FeedbackFaculty.equals("Select Your Faculty")) {
                GenericUserFunction.showOopsError(this, "Please select your Faculty")
                return false
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "" + ex.stackTrace, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    fun Update() {
        Toast.makeText(this, "Update called ", Toast.LENGTH_SHORT).show()
        println(
            "FeedbackType FormativeFeedback"+
            "\n Faculty "+selected_spinner_FeedbackFaculty+
            "\n Course_ID " + Course_ID +
                    "\n Stud_ID " + Stud_ID +
                    "\n Stud_Name " + Stud_Name +
                    "\n Stud_Stud_Roll_No " + Stud_Roll_No +
                    "\n Course " + Course +
                    "\n Institute " + Institute +
                    "\n SecA_Q1 " + Ex_S1_Rdg1_radioButton_Text +
                    "\n SecA_Q2 " + Ex_S1_Rdg2_radioButton_Text +
                    "\n SecA_Q3 " + Ex_S1_Rdg3_radioButton_Text +
                    "\n SecA_Q4 " + Ex_S1_Rdg4_radioButton_Text +
                    "\n SecA_Q5 " + Ex_S1_Rdg5_radioButton_Text +

                    "\n SecB_Q1 " + Ex_S2_Rdg1_radioButton_Text +
                    "\n SecB_Q2 " + Ex_S2_Rdg2_radioButton_Text +
                    "\n SecB_Q3 " + Ex_S2_Rdg3_radioButton_Text +
                    "\n SecB_Q3_Suggest " + Af_Ex_S2_Q3_Text +
                    "\n SecB_Q4 " + Ex_S2_Rdg4_radioButton_Text +

                    "\n SecB_Q5_Q1 " + Ex_S2_Rdg5_Rdg5_q1_radioButton_Text +
                    "\n SecB_Q5_Q2 " + Ex_S2_Rdg5_Rdg5_q2_radioButton_Text +
                    "\n SecB_Q5_Q3 " + Ex_S2_Rdg5_Rdg5_q3_radioButton_Text +
                    "\n SecB_Q5_Q4 " + Ex_S2_Rdg5_Rdg5_q4_radioButton_Text +
                    "\n SecB_Q5_Q1_Suggest " + Af_Ex_S2_Q5_q1_Text +
                    "\n SecB_Q5_Q2_Suggest " + Af_Ex_S2_Q5_q2_Text +
                    "\n SecB_Q5_Q3_Suggest " + Af_Ex_S2_Q5_q3_Text +
                    "\n SecB_Q5_Q4_Suggest " + Af_Ex_S2_Q5_q4_Text +
                    "\n SecC_Suggest " + Af_Ex_S3_suggest_Text
        )
        //Layer 4
        val objFeedFormSectb=JSONObject()

        objFeedFormSectb.put("FB1", Ex_S2_Rdg1_radioButton_Text)
        objFeedFormSectb.put("FB2", Ex_S2_Rdg2_radioButton_Text)
        objFeedFormSectb.put("FB3", Ex_S2_Rdg3_radioButton_Text)
        objFeedFormSectb.put("FB3_DES", Af_Ex_S2_Q3_Text)
        objFeedFormSectb.put("FB4", Ex_S2_Rdg4_radioButton_Text)
        objFeedFormSectb.put("FB5A", Ex_S2_Rdg5_Rdg5_q1_radioButton_Text)
        objFeedFormSectb.put("FB5A_DES", Af_Ex_S2_Q5_q1_Text)
        objFeedFormSectb.put("FB5B", Ex_S2_Rdg5_Rdg5_q2_radioButton_Text)
        objFeedFormSectb.put("FB5B_DES", Af_Ex_S2_Q5_q2_Text)
        objFeedFormSectb.put("FB5C", Ex_S2_Rdg5_Rdg5_q3_radioButton_Text)
        objFeedFormSectb.put("FB5C_DES", Af_Ex_S2_Q5_q3_Text)
        objFeedFormSectb.put("FB5D", Ex_S2_Rdg5_Rdg5_q4_radioButton_Text)
        objFeedFormSectb.put("FB5D_DES", Af_Ex_S2_Q5_q4_Text)

        //Layer 3
        val objFeedFormSecta=JSONObject()
        objFeedFormSecta.put("FA1", Ex_S1_Rdg1_radioButton_Text)
        objFeedFormSecta.put("FA2", Ex_S1_Rdg2_radioButton_Text)
        objFeedFormSecta.put("FA3", Ex_S1_Rdg3_radioButton_Text)
        objFeedFormSecta.put("FA4", Ex_S1_Rdg4_radioButton_Text)
        objFeedFormSecta.put("FA5", Ex_S1_Rdg5_radioButton_Text)
        //Layer 2
        val objFormative=JSONObject()
        objFormative.put("FACULTY_TYPE", selected_spinner_FeedbackFaculty)
        objFormative.put("FEED_SUM_DATE", CurrentDate)
        objFormative.put("FOR_DESC", Af_Ex_S3_suggest_Text)
        objFormative.put("Feed_Form_SectA", objFeedFormSecta)
        objFormative.put("Feed_Form_SectB", objFeedFormSectb)

        //Layer 1
        val rootObject=JSONObject()
        rootObject.put("FEEDBACK_TYPE", "F")
        rootObject.put("COURSE_ID", Course_ID)
        rootObject.put("STUD_ID", Stud_ID)
        rootObject.put("STUD_NAME", Stud_Name)
        rootObject.put("ROLL_NO", Stud_Roll_No)
        rootObject.put("COURSE", Course)
        rootObject.put("INSTITUTE_NAME", Stud_Institute)
       // rootObject.put("Summative", "")
        rootObject.put("Formative", objFormative)

        val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
        try {

            dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
            dialog.setCancelable(false)
            dialog.show()
            //Dialog End
            GenericPublicVariable.mServices.SubmitExamFeedback(rootObject).enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@FormativeFeedbackActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    dialog.dismiss()
                    val result: APIResponse? = response.body()
                    println("Result >>> "+result!!.Responsecode)

//                                        Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()
                    GenericUserFunction.showPositivePopUp(this@FormativeFeedbackActivity, "Formative Feedback Submited Successfully")
                }
            })


    }
        catch (ex:Exception){
            dialog.dismiss()

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                applicationContext,
                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
            )

        }
    }

//    FeedbackType,
//    Faculty,
//    Course_ID,
//    Stud_ID,
//    Stud_Name,
//    Stud_Stud_Roll_No,
//    Course,
//    Institute,
//    SecA_Q1,
//    SecA_Q2,
//    SecA_Q3,
//    SecA_Q4,
//    SecA_Q5,
//    SecB_Q1,
//    SecB_Q2,
//    SecB_Q3,
//    SecB_Q3_Suggest,
//    SecB_Q4,
//    SecB_Q5_Q1,
//    SecB_Q5_Q2,
//    SecB_Q5_Q3,
//    SecB_Q5_Q4,
//    SecB_Q5_Q1_Suggest,
//    SecB_Q5_Q2_Suggest,
//    SecB_Q5_Q3_Suggest,
//    SecB_Q5_Q4_Suggest,
//    SecC_Suggest
}
