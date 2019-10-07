package com.dmims.dmims.remote

import com.dmims.dmims.model.APIResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface IMyAPI {

    @FormUrlEncoded
    @POST("Login/GetOtp")
    fun GetOtp(@Field("MobileNo") mobileno: String): Call<APIResponse>

    @GET("Feedback/GetGrievanceReport")
    fun GetRegisteredGreivance(): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/StudentSearch")
    fun StudentSearch(@Field("student_id") STUDENTID: Int): Call<APIResponse>

    @FormUrlEncoded
    @POST("Attendance/GetAttendance")
    fun GetAttendance(@Field("STUDENTID") STUDENTID: Int, @Field("FROMDATE") FROMDATE: String, @Field("TODATE") TODATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Attendance/GetProgressiveAttend")
    fun GetProgressiveAttend(@Field("STUDENTID") STUDENTID: Int, @Field("FROMDATE") FROMDATE: String, @Field("TODATE") TODATE: String, @Field("COURSE_ID")COURSE_ID:String): Call<APIResponse>


    @FormUrlEncoded
    @POST("Login/VerifyOtp")
    fun VerifyOtp(@Field("MobileNo") mobileno: String, @Field("Otp") password: String): Call<APIResponse>


    @GET("Login/GetInstituteData")
    fun GetInstituteData(): Call<APIResponse>

    @GET("EmergencyServices/GetEmergencyServices")
    fun GetEmergencyServices(): Call<APIResponse>

    @GET("AppVersion/AppVersion")
    fun AppVersion(): Call<APIResponse>

//    @Multipart
//    @POST("Upload/PostFormData")
//    //fun PostFormData(@Part file: MultipartBody.Part): Call<APIResponse>
//    fun PostFormData(@Part file: MultipartBody.Part,@Part("NOTICE_DATE")notice_date:String,@Part("NOTICE_TITLE")notice_title:String,@Part("NOTICE_DESC")notice_desc:String,@Part("INSTITUE_NAME")selectedInstituteName:String,@Part("COURSE_NAME")selectedcourselist:String,@Part("DEPT_NAME")selecteddeptlist:String,@Part("NOTICE_TYPE")selectedNoticeType:String,@Part("USER_TYPE")selectedFacultyStud:String,@Part("RESOU_FLAG")RESOU_FLAG:String,@Part("USER_ROLE")roleadmin:String,@Part("USER_ID")id_admin:String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Upload/UploadNotice")
    fun UploadNotice(
        @Field("NOTICE_DATE") notice_date: String, @Field("NOTICE_TITLE") notice_title: String, @Field("NOTICE_DESC") notice_desc: String, @Field(
            "INSTITUTE_NAME"
        ) selectedInstituteName: String, @Field("COURSE_NAME") selectedcourselist: String, @Field("DEPT_NAME") selecteddeptlist: String, @Field(
            "NOTICE_TYPE"
        ) selectedNoticeType: String, @Field("USER_TYPE") selectedFacultyStud: String, @Field("RESOU_FLAG") RESOU_FLAG: String, @Field(
            "USER_ROLE"
        ) roleadmin: String, @Field("USER_ID") id_admin: String, @Field("FILENAME") filename: String, @Field("COURSE_ID") course_id: String, @Field(
            "DEPT_ID"
        ) dept_id: String, @Field("STUDENT_FLAG") student_flag: String, @Field("FACULTY_FLAG") faculty_flag: String, @Field(
            "ADMIN_FLAG"
        ) admin_flag: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/InsertGrievanceReport")
    fun InsertGrievanceReport(
        @Field("INFORMATION") sel_information: String, @Field("NAME_GRIE") sel_name: String, @Field("PHONE_NO") sel_mobileno: String, @Field(
            "EMAIL_ID"
        ) sel_emailid: String, @Field("ADDRESS") sel_address: String, @Field("PLACE_WORK") sel_workplace: String, @Field(
            "JOB_TITLE"
        ) sel_jobtitle: String, @Field("TYPE_GRIE") selectedgreviancetype: String, @Field("DT_TIME_PLACE") sel_datetime: String, @Field(
            "DETAIL_DESC"
        ) sel_detaildesc: String, @Field("OTHER_INFO") sel_proposedsol: String, @Field(
            "PRO_SOL_GRIE"
        ) course_id: String, @Field(
            "OTHER_DETAILS"
        ) sel_providername: String,
        @Field("STUDENT_ID") stud_k: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/GetInsertNotice")
    fun GetNotice(@Field("FROM_NOTICE_DATE") FROM_NOTICE_DATE: String, @Field("TO_NOTICE_DATE") TO_NOTICE_DATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/DeleteNotice")
    fun DeleteNotice(@Field("ID") ID: Int): Call<APIResponse>



    @GET("Feedback/GetFeedBackScheduler")
    fun GetFeedBackScheduler(): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/InsertFeedBack")
    fun InsertFeedBack(@Field("COURSE_NAME") COURSE_NAME: String, @Field("DEPT_NAME") DEPT_NAME: String, @Field("FEEDBACK_NAME") FEEDBACK_NAME: String, @Field("SCHEDULE_DATE") SCHEDULE_DATE: String, @Field("START_DATE") START_DATE: String, @Field("END_DATE") END_DATE: String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Login/StudentSearchByRollNo")
    fun StudentSearchByRollNo(@Field("roll_no") roll_no: String, @Field("course_id")course_id:String): Call<APIResponse>

    @FormUrlEncoded
    @POST("Feedback/Feedback_Form_Summ")
    fun SubmitExamFeedback(@Field ("jsonstring")jsonstring: String): Call<APIResponse>




}

