package com.dmims.dmims.ExamFeedBack

class CommonFeedBack {
    private var FEEDBACK_TYPE: String
    private var COURSE_ID: String
    private var STUD_ID: String
    private var STUD_NAME: String
    private var ROLL_NO: String
    private var COURSE: String
    private var INSTITUTE_NAME: String
    private var formativeSub: FormativeSub

    constructor(
        FEEDBACK_TYPE: String,
        COURSE_ID: String,
        STUD_ID: String,
        STUD_NAME: String,
        ROLL_NO: String,
        COURSE: String,
        INSTITUTE_NAME: String,
        formativeSub: FormativeSub
    ) {
        this.FEEDBACK_TYPE = FEEDBACK_TYPE
        this.COURSE_ID = COURSE_ID
        this.STUD_ID = STUD_ID
        this.STUD_NAME = STUD_NAME
        this.ROLL_NO = ROLL_NO
        this.COURSE = COURSE
        this.INSTITUTE_NAME = INSTITUTE_NAME
        this.formativeSub = formativeSub
    }

//    init {
//        this.FEEDBACK_TYPE = FEEDBACK_TYPE
//        this.COURSE_ID = COURSE_ID
//        this.STUD_ID = STUD_ID
//        this.STUD_NAME = STUD_NAME
//        this.ROLL_NO = ROLL_NO
//        this.COURSE = COURSE
//        this.INSTITUTE_NAME = INSTITUTE_NAME
//        this.formativeSub = formativeSub
//    }

}

class FormativeSub(
    FACULTY_TYPE: String,
    FEED_SUM_DATE: String,
    FOR_DESC: String,
    feedFormSecta: Feed_Form_SectA,
    feedFormSectb: Feed_Form_SectB
) {
    private var FACULTY_TYPE: String
    private var FEED_SUM_DATE: String
    private var FOR_DESC: String
    private var feedFormSecta: Feed_Form_SectA
    private var feedFormSectb: Feed_Form_SectB
    init {
        this.FACULTY_TYPE = FACULTY_TYPE
        this.FEED_SUM_DATE = FEED_SUM_DATE
        this.FOR_DESC = FOR_DESC
        this.feedFormSecta=feedFormSecta
        this.feedFormSectb=feedFormSectb
    }
}

class Feed_Form_SectA(
    FA1: String,
    FA2: String,
    FA3: String,
    FA4: String,
    FA5: String
) {
    private var FA1: String
    private var FA2: String
    private var FA3: String
    private var FA4: String
    private var FA5: String

    init {
        this.FA1 = FA1
        this.FA2 = FA2
        this.FA3 = FA3
        this.FA4 = FA4
        this.FA5 = FA5
    }
}

class Feed_Form_SectB(
    FB1:String,
    FB2:String,
    FB3:String,
    FB3_DES:String,
    FB4:String,
    FB5A:String,
    FB5A_DES:String,
    FB5B:String,
    FB5B_DES:String,
    FB5C:String,
    FB5C_DES:String,
    FB5D:String,
    FB5D_DES:String
)
{
    private var FB1:String
    private var FB2:String
    private var FB3:String
    private var FB3_DES:String
    private var FB4:String
    private var FB5A:String
    private var FB5A_DES:String
    private var FB5B:String
    private var FB5B_DES:String
    private var FB5C:String
    private var FB5C_DES:String
    private var FB5D:String
    private var FB5D_DES:String
    init {
        this.FB1=FB1
        this.FB2=FB2
        this.FB3=FB3
        this.FB3_DES=FB3_DES
        this.FB4=FB4
        this.FB5A=FB5A
        this.FB5A_DES=FB5A_DES
        this.FB5B=FB5B
        this.FB5B_DES=FB5B_DES
        this.FB5C=FB5C
        this.FB5C_DES=FB5C_DES
        this.FB5D=FB5D
        this.FB5D_DES=FB5D_DES
    }

}
//
//class SummativeSub(

//){
//    "FACULTY_TYPE": "",
//    "EXAM_TYPE": "",
//    "EXAM_YEAR": "",
//    "FEED_SUM_DATE": "",
//    "SUM_DESC": "",
//}