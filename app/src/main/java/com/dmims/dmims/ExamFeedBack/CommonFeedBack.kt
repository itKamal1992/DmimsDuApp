package com.dmims.dmims.ExamFeedBack

import com.dmims.dmims.activity.Feed_Form_SectA
import com.dmims.dmims.activity.Feed_Form_SectB

class CommonFeedBack {
    private var FEEDBACK_TYPE: String
    private var COURSE_ID: String
    private var STUD_ID: String
    private var STUD_NAME: String
    private var ROLL_NO: String
    private var COURSE: String
    private var INSTITUTE_NAME: String
    private var Formative: Formative

    constructor(
        FEEDBACK_TYPE: String,
        COURSE_ID: String,
        STUD_ID: String,
        STUD_NAME: String,
        ROLL_NO: String,
        COURSE: String,
        INSTITUTE_NAME: String,
        Formative: Formative
    ) {
        this.FEEDBACK_TYPE = FEEDBACK_TYPE
        this.COURSE_ID = COURSE_ID
        this.STUD_ID = STUD_ID
        this.STUD_NAME = STUD_NAME
        this.ROLL_NO = ROLL_NO
        this.COURSE = COURSE
        this.INSTITUTE_NAME = INSTITUTE_NAME
        this.Formative = Formative
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

class Formative (
    FACULTY_TYPE: String,
    FEED_SUM_DATE: String,
    FOR_DESC: String,
    Feed_Form_SectA: Feed_Form_SectA,
    Feed_Form_SectB: Feed_Form_SectB
) {
    private var FACULTY_TYPE: String
    private var FEED_SUM_DATE: String
    private var FOR_DESC: String
    private var Feed_Form_SectA: Feed_Form_SectA
    private var Feed_Form_SectB: Feed_Form_SectB
    init {
        this.FACULTY_TYPE = FACULTY_TYPE
        this.FEED_SUM_DATE = FEED_SUM_DATE
        this.FOR_DESC = FOR_DESC
        this.Feed_Form_SectA=Feed_Form_SectA
        this.Feed_Form_SectB =Feed_Form_SectB
    }
}
