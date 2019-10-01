package com.dmims.dmims.activity

import android.content.Context
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.dmims.dmims.R
import com.dmims.dmims.model.FeedBackInsert
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import kotlinx.android.synthetic.main.student__feedback__summative_exam.*
import retrofit2.Call

class Student_Feedback_SummativeExam : AppCompatActivity()
{
    lateinit var spinner_SummaNameFaculty:Spinner
    lateinit var spinner_SummaExamination:Spinner
    lateinit var spinner_SummaYear:Spinner

    lateinit var rg_SummAQ1:RadioGroup
    lateinit var rg_SummAQ2:RadioGroup
    lateinit var rg_SummAQ3:RadioGroup
    lateinit var rg_SummAQ4:RadioGroup

    lateinit var rg_SummBQ1:RadioGroup
    lateinit var rg_SummBQ2:RadioGroup
    lateinit var rg_SummBQ3:RadioGroup
    lateinit var rg_SummBQ4:RadioGroup
    lateinit var rg_SummBQ5:RadioGroup

    lateinit var rg_SummCQ1:RadioGroup
    lateinit var rg_SummCQ2:RadioGroup
    lateinit var rg_SummCQ3:RadioGroup
    lateinit var rg_SummCQ4:RadioGroup

    lateinit var rg_SummDQ1:RadioGroup
    lateinit var rg_SummDQ2:RadioGroup
    lateinit var rg_SummDQ3:RadioGroup
    lateinit var rg_SummDQ4:RadioGroup
    lateinit var rg_SummDQ5:RadioGroup
    lateinit var rg_SummDQ6:RadioGroup

    lateinit var str_summNameFaculty:String
    lateinit var str_summExamination:String
    lateinit var str_summYear:String

    var str_SummAQ1:String?=""
    var str_SummAQ2:String?=""
    var str_SummAQ3:String?=""
    var str_SummAQ4:String?=""

    var str_SummBQ1:String?=""
    var str_SummBQ2:String?=""
    var str_SummBQ3:String?=""
    var str_SummBQ4:String?=""
    var str_SummBQ5:String?=""

    var str_SummCQ1:String?=""
    var str_SummCQ2:String?=""
    var str_SummCQ3:String?=""
    var str_SummCQ4:String?=""

    var str_SummDQ1:String?=""
    var str_SummDQ2:String?=""
    var str_SummDQ3:String?=""
    var str_SummDQ4:String?=""
    var str_SummDQ5:String?=""
    var str_SummDQ6:String?=""

    var str_etAQ1:String?=""
    var str_etAQ2:String?=""
    var str_etAQ3:String?=""
    var str_etAQ4:String?=""

    var str_etBQ1:String?=""
    var str_etBQ2:String?=""
    var str_etBQ3:String?=""
    var str_etBQ4:String?=""
    var str_etBQ5:String?=""

    var str_etCQ1:String?=""
    var str_etCQ2:String?=""
    var str_etCQ3:String?=""
    var str_etCQ4:String?=""

    var str_etDQ1:String?=""
    var str_etDQ2:String?=""
    var str_etDQ3:String?=""
    var str_etDQ4:String?=""
    var str_etDQ5:String?=""
    var str_etDQ6:String?=""

    private lateinit var institute_name: String

    lateinit var  btnSubmit:Button

    var COURSE_ID: String = "-"
    var stud_kstr: String = "-"
    var stud_namestr: String = "-"
    var stud_k: Int = 0

    var array = intArrayOf(1, 99, 10000, 84849, 111, 212, 314, 21, 442, 455, 244, 554, 22, 22, 211)
    lateinit var a:Array<String>
    lateinit var c:Array<String>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student__feedback__summative_exam)

        //  println("subSumm  "+" "+array.size+"  a "+a[1])
        et_Summative_PreexamQ1.visibility=View.GONE
        et_Summative_PreexamQ2.visibility=View.GONE
        et_Summative_PreexamQ3.visibility=View.GONE
        et_Summative_PreexamQ4.visibility=View.GONE

        et_Summative_structuringpaperQ1.visibility=View.GONE
        et_Summative_structuringpaperQ2.visibility=View.GONE
        et_Summative_structuringpaperQ3.visibility=View.GONE
        et_Summative_structuringpaperQ4.visibility=View.GONE
        et_Summative_structuringpaperQ5.visibility=View.GONE

        et_Summative_customizedasQ1.visibility=View.GONE
        et_Summative_customizedasQ2.visibility=View.GONE
        et_Summative_customizedasQ3.visibility=View.GONE
        et_Summative_customizedasQ4.visibility=View.GONE

        et_Summative_conductPractiexamQ1.visibility=View.GONE
        et_Summative_conductPractiexamQ2.visibility=View.GONE
        et_Summative_conductPractiexamQ3.visibility=View.GONE
        et_Summative_conductPractiexamQ4.visibility=View.GONE
        et_Summative_conductPractiexamQ5.visibility=View.GONE
        et_Summative_conductPractiexamQ6.visibility=View.GONE




        rg_SummAQ1=findViewById(R.id.rg_Summative_PreexamQ1)
        rg_SummAQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            Toast.makeText(applicationContext," On checked change : ${radio.text}",
                Toast.LENGTH_SHORT).show()

            str_SummAQ1=radio.text.toString()



            println("Text 1 "+str_SummAQ1)
            if (radio.text.equals("No"))
            {
                et_Summative_PreexamQ1.visibility=View.VISIBLE
            }else
            {
                et_Summative_PreexamQ1.visibility=View.GONE
            }


        }




        rg_SummAQ2=findViewById(R.id.rg_Summative_PreexamQ2)
        rg_SummAQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            if (radio.text.equals("No"))
            {
                et_Summative_PreexamQ2.visibility=View.VISIBLE
            }else
            {
                et_Summative_PreexamQ2.visibility=View.GONE
            }

        }

        rg_SummAQ3=findViewById(R.id.rg_Summative_PreexamQ3)
        rg_SummAQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            if (radio.text.equals("No"))
            {
                et_Summative_PreexamQ3.visibility=View.VISIBLE
            }else
            {
                et_Summative_PreexamQ3.visibility=View.GONE
            }

        }

        rg_SummAQ4=findViewById(R.id.rg_Summative_PreexamQ4)
        rg_SummAQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            if (radio.text.equals("No"))
            {
                et_Summative_PreexamQ4.visibility=View.VISIBLE
            }else
            {
                et_Summative_PreexamQ4.visibility=View.GONE
            }

        }

        rg_SummBQ1=findViewById(R.id.rg_Summative_structuringpaperQ1)
        rg_SummBQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_structuringpaperQ1.visibility=View.VISIBLE
            }else
            {
                et_Summative_structuringpaperQ1.visibility=View.GONE
            }

        }


        rg_SummBQ2=findViewById(R.id.rg_Summative_structuringpaperQ2)
        rg_SummBQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_structuringpaperQ2.visibility=View.VISIBLE
            }else
            {
                et_Summative_structuringpaperQ2.visibility=View.GONE
            }

        }

        rg_SummBQ3=findViewById(R.id.rg_Summative_structuringpaperQ3)
        rg_SummBQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_structuringpaperQ3.visibility=View.VISIBLE
            }else
            {
                et_Summative_structuringpaperQ3.visibility=View.GONE
            }

        }


        rg_SummBQ4=findViewById(R.id.rg_Summative_structuringpaperQ4)
        rg_SummBQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_structuringpaperQ4.visibility=View.VISIBLE
            }else
            {
                et_Summative_structuringpaperQ4.visibility=View.GONE
            }

        }


        rg_SummBQ5=findViewById(R.id.rg_Summative_structuringpaperQ5)
        rg_SummBQ5.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_structuringpaperQ5.visibility=View.VISIBLE
            }else
            {
                et_Summative_structuringpaperQ5.visibility=View.GONE
            }

        }

        rg_SummCQ1=findViewById(R.id.rg_Summative_customizedasQ1)
        rg_SummCQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)

            if (radio.text.equals("No"))
            {
                et_Summative_customizedasQ1.visibility=View.VISIBLE
            }else
            {
                et_Summative_customizedasQ1.visibility=View.GONE
            }

        }

        rg_SummCQ2=findViewById(R.id.rg_Summative_customizedasQ2)
        rg_SummCQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_customizedasQ2.visibility=View.VISIBLE
            }else
            {
                et_Summative_customizedasQ2.visibility=View.GONE
            }

        }

        rg_SummCQ3=findViewById(R.id.rg_Summative_customizedasQ3)
        rg_SummCQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_customizedasQ3.visibility=View.VISIBLE
            }else
            {
                et_Summative_customizedasQ3.visibility=View.GONE
            }

        }

        rg_SummCQ4=findViewById(R.id.rg_Summative_customizedasQ4)
        rg_SummCQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_customizedasQ4.visibility=View.VISIBLE
            }else
            {
                et_Summative_customizedasQ4.visibility=View.GONE
            }

        }


        rg_SummDQ1=findViewById(R.id.rg_Summative_conductPractiexamQ1)
        rg_SummDQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_conductPractiexamQ1.visibility=View.VISIBLE
            }else
            {
                et_Summative_conductPractiexamQ1.visibility=View.GONE
            }

        }


        rg_SummDQ2=findViewById(R.id.rg_Summative_conductPractiexamQ2)
        rg_SummDQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_conductPractiexamQ2.visibility=View.VISIBLE
            }else
            {
                et_Summative_conductPractiexamQ2.visibility=View.GONE
            }

        }

        rg_SummDQ3=findViewById(R.id.rg_Summative_conductPractiexamQ3)
        rg_SummDQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_conductPractiexamQ3.visibility=View.VISIBLE
            }else
            {
                et_Summative_conductPractiexamQ3.visibility=View.GONE
            }

        }

        rg_SummDQ4=findViewById(R.id.rg_Summative_conductPractiexamQ4)
        rg_SummDQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_conductPractiexamQ4.visibility=View.VISIBLE
            }else
            {
                et_Summative_conductPractiexamQ4.visibility=View.GONE
            }

        }


        rg_SummDQ5=findViewById(R.id.rg_Summative_conductPractiexamQ5)
        rg_SummDQ5.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No"))
            {
                et_Summative_conductPractiexamQ5.visibility=View.VISIBLE
            }else
            {
                et_Summative_conductPractiexamQ5.visibility=View.GONE
            }

        }



        rg_SummDQ6=findViewById(R.id.rg_Summative_conductPractiexamQ6)
        rg_SummDQ6.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if(radio.text.equals("No"))
            {
                et_Summative_conductPractiexamQ6.visibility=View.VISIBLE
            }else
            {
                et_Summative_conductPractiexamQ6.visibility=View.GONE
            }

        }

        spinner_SummaNameFaculty=findViewById(R.id.spinner_facultysummativeexam)
        spinner_SummaExamination=findViewById(R.id.spinner_examsummativeexam)
        spinner_SummaYear=findViewById(R.id.spinner_yearsummativeexam)


        btnSubmit=findViewById(R.id.btn_summFeedSubmit)
        btnSubmit.setOnClickListener {

            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            COURSE_ID = mypref.getString("course_id", null)
            stud_kstr = mypref.getString("Stud_id_key", null)
            stud_namestr = mypref.getString("key_drawer_title", null)
            stud_k = Integer.parseInt(stud_kstr)

            str_summNameFaculty=spinner_SummaNameFaculty.selectedItem.toString()
            str_summExamination=spinner_SummaExamination.selectedItem.toString()
            str_summYear=spinner_SummaYear.selectedItem.toString()

            println("str_summNameFaculty "+str_summNameFaculty+" "+str_summExamination+" "+str_summYear)

            if (str_summNameFaculty.equals("Select Faculty"))
            {
                Toast.makeText(applicationContext,"Select faculty",Toast.LENGTH_LONG).show()
            }else if(str_summExamination.equals("Select Examination"))
            {
                Toast.makeText(applicationContext,"Select exam",Toast.LENGTH_LONG).show()
            }else if(str_summYear.equals("Select Year"))
            {
                Toast.makeText(applicationContext,"Select year",Toast.LENGTH_LONG).show()
            }else
            {
                str_summNameFaculty=spinner_SummaNameFaculty.selectedItem.toString()
                str_summExamination=spinner_SummaExamination.selectedItem.toString()
                str_summYear=spinner_SummaYear.selectedItem.toString()

                var selectedAQ1= rg_SummAQ1.checkedRadioButtonId
                str_SummAQ1= findViewById<RadioButton>(selectedAQ1).text.toString()
                str_etAQ1=findViewById<EditText>(R.id.et_Summative_PreexamQ1).text.toString()
                if (str_etAQ1.equals(""))
                {
                    str_etAQ1="-"
                }
                var selectedAQ2= rg_SummAQ2.checkedRadioButtonId
                str_SummAQ2= findViewById<RadioButton>(selectedAQ2).text.toString()
                str_etAQ2=findViewById<EditText>(R.id.et_Summative_PreexamQ2).text.toString()
                if (str_etAQ2.equals(""))
                {
                    str_etAQ2="-"
                }
                var selectedAQ3= rg_SummAQ3.checkedRadioButtonId
                str_SummAQ3= findViewById<RadioButton>(selectedAQ3).text.toString()
                str_etAQ3=findViewById<EditText>(R.id.et_Summative_PreexamQ3).text.toString()
                if (str_etAQ3.equals(""))
                {
                    str_etAQ3="-"
                }
                var selectedAQ4= rg_SummAQ4.checkedRadioButtonId
                str_SummAQ4= findViewById<RadioButton>(selectedAQ4).text.toString()
                str_etAQ4=findViewById<EditText>(R.id.et_Summative_PreexamQ4).text.toString()
                if (str_etAQ4.equals(""))
                {
                    str_etAQ4="-"
                }


                var selectedBQ1= rg_SummBQ1.checkedRadioButtonId
                str_SummBQ1= findViewById<RadioButton>(selectedBQ1).text.toString()
                str_etBQ1=findViewById<EditText>(R.id.et_Summative_structuringpaperQ1).text.toString()
                if (str_etBQ1.equals(""))
                {
                    str_etBQ1="-"
                }
                var selectedBQ2= rg_SummBQ2.checkedRadioButtonId
                str_SummBQ2= findViewById<RadioButton>(selectedBQ2).text.toString()
                str_etBQ2=findViewById<EditText>(R.id.et_Summative_structuringpaperQ2).text.toString()
                if (str_etBQ2.equals(""))
                {
                    str_etBQ2="-"
                }
                var selectedBQ3= rg_SummBQ3.checkedRadioButtonId
                str_SummBQ3= findViewById<RadioButton>(selectedBQ3).text.toString()
                str_etBQ3=findViewById<EditText>(R.id.et_Summative_structuringpaperQ3).text.toString()
                if (str_etBQ3.equals(""))
                {
                    str_etBQ3="-"
                }
                var selectedBQ4= rg_SummBQ4.checkedRadioButtonId
                str_SummBQ4= findViewById<RadioButton>(selectedBQ4).text.toString()
                str_etBQ4=findViewById<EditText>(R.id.et_Summative_structuringpaperQ4).text.toString()
                if (str_etBQ4.equals(""))
                {
                    str_etBQ4="-"
                }
                var selectedBQ5= rg_SummBQ5.checkedRadioButtonId
                str_SummBQ5= findViewById<RadioButton>(selectedBQ5).text.toString()
                str_etBQ5=findViewById<EditText>(R.id.et_Summative_structuringpaperQ5).text.toString()
                if (str_etBQ5.equals(""))
                {
                    str_etBQ5="-"
                }


                var selectedCQ1= rg_SummCQ1.checkedRadioButtonId
                str_SummCQ1= findViewById<RadioButton>(selectedCQ1).text.toString()
                str_etCQ1=findViewById<EditText>(R.id.et_Summative_customizedasQ1).text.toString()
                if (str_etCQ1.equals(""))
                {
                    str_etCQ1="-"
                }
                var selectedCQ2= rg_SummCQ2.checkedRadioButtonId
                str_SummCQ2= findViewById<RadioButton>(selectedCQ2).text.toString()
                str_etCQ2=findViewById<EditText>(R.id.et_Summative_customizedasQ2).text.toString()
                if (str_etCQ2.equals(""))
                {
                    str_etCQ2="-"
                }
                var selectedCQ3= rg_SummCQ3.checkedRadioButtonId
                str_SummCQ3= findViewById<RadioButton>(selectedCQ3).text.toString()
                str_etCQ3=findViewById<EditText>(R.id.et_Summative_customizedasQ3).text.toString()
                if (str_etCQ3.equals(""))
                {
                    str_etCQ3="-"
                }
                var selectedCQ4= rg_SummCQ4.checkedRadioButtonId
                str_SummCQ4= findViewById<RadioButton>(selectedCQ4).text.toString()
                str_etCQ4=findViewById<EditText>(R.id.et_Summative_customizedasQ4).text.toString()
                if (str_etCQ4.equals(""))
                {
                    str_etCQ4="-"
                }

                var selectedDQ1= rg_SummDQ1.checkedRadioButtonId
                str_SummDQ1= findViewById<RadioButton>(selectedDQ1).text.toString()
                str_etDQ1=findViewById<EditText>(R.id.et_Summative_conductPractiexamQ1).text.toString()
                if (str_etDQ1.equals(""))
                {
                    str_etDQ1="-"
                }
                var selectedDQ2= rg_SummDQ2.checkedRadioButtonId
                str_SummDQ2= findViewById<RadioButton>(selectedDQ2).text.toString()
                str_etDQ2=findViewById<EditText>(R.id.et_Summative_conductPractiexamQ2).text.toString()
                if (str_etDQ2.equals(""))
                {
                    str_etDQ2="-"
                }
                var selectedDQ3= rg_SummDQ3.checkedRadioButtonId
                str_SummDQ3= findViewById<RadioButton>(selectedDQ3).text.toString()
                str_etDQ3=findViewById<EditText>(R.id.et_Summative_conductPractiexamQ3).text.toString()
                if (str_etDQ3.equals(""))
                {
                    str_etDQ3="-"
                }
                var selectedDQ4= rg_SummDQ4.checkedRadioButtonId
                str_SummDQ4= findViewById<RadioButton>(selectedDQ4).text.toString()
                str_etDQ4=findViewById<EditText>(R.id.et_Summative_conductPractiexamQ4).text.toString()
                if (str_etDQ4.equals(""))
                {
                    str_etDQ4="-"
                }
                var selectedDQ5= rg_SummDQ5.checkedRadioButtonId
                str_SummDQ5= findViewById<RadioButton>(selectedDQ5).text.toString()
                str_etDQ5=findViewById<EditText>(R.id.et_Summative_conductPractiexamQ5).text.toString()
                if (str_etDQ5.equals(""))
                {
                    str_etDQ5="-"
                }
                var selectedDQ6= rg_SummDQ6.checkedRadioButtonId
                str_SummDQ6= findViewById<RadioButton>(selectedDQ6).text.toString()
                str_etDQ6=findViewById<EditText>(R.id.et_Summative_conductPractiexamQ6).text.toString()
                if (str_etDQ6.equals(""))
                {
                    str_etDQ6="-"
                }

                var phpApiInterface:PhpApiInterface=ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                var call:Call<FeedBackInsert> =phpApiInterface.feedbackTeacherInsrt(stud_kstr,stud_namestr,institute_name,"STUDENT SURVEY","SEC_SS","TEACHING LEARNING AND EVALUATION","--","--","--",
                    "1","2","3","4","5","6","7","8","9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "--",
                    str_SummAQ1!!,
                    str_SummAQ2!!,
                    str_SummAQ3!!,
                    str_SummAQ4!!,
                    str_SummBQ1!!,
                    str_SummBQ2!!,
                    str_SummBQ3!!,
                    str_SummBQ4!!,
                    str_SummBQ5!!,
                    str_SummCQ1!!,
                    str_SummCQ2!!,
                    str_SummCQ3!!,
                    str_SummCQ4!!,
                    str_SummDQ1!!,
                    str_SummDQ2!!,
                    str_SummDQ3!!,
                    str_SummDQ4!!,
                    str_SummDQ5!!,
                    str_SummDQ6!!,
                    "--",
                    str_summNameFaculty,str_summYear,str_summYear)







                println("here text q1 "+str_summNameFaculty+" n "+str_summExamination+" n "+str_summYear+" n "+str_etAQ1+"/"+str_etAQ2+"/"+str_etAQ3+"/"+str_etAQ4+"/"+str_etBQ1+"/"+str_etBQ2+"/"+str_etBQ3+"/"+str_etBQ4+"/"+str_etBQ5+"/"
                        +str_etCQ1+"/"+str_etCQ2+"/"+str_etCQ3+"/"+str_etCQ4+"/"+str_etDQ1+"/"+str_etDQ2+"/"+str_etDQ3+"/"+str_etDQ4+"/"+str_etDQ5+"/"+str_etDQ6)
                println("here q1"+str_SummAQ1+"-"+str_SummAQ2+"-"+str_SummAQ3+"-"+str_SummAQ4+"-"+str_SummBQ1+"-"+str_SummBQ2+"-"+str_SummBQ3+"-"+str_SummBQ4+"-"+str_SummBQ5+"-"
                        +str_SummCQ1+"-"+str_SummCQ2+"-"+str_SummCQ3+"-"+str_SummCQ4+"-"+str_SummDQ1+"-"+str_SummDQ2+"-"+str_SummDQ3+"-"+str_SummDQ4+"-"+str_SummDQ5+"-"+str_SummDQ6+"-")
            }







        }
    }
}
