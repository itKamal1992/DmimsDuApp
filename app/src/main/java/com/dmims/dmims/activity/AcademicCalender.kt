package com.dmims.dmims.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.TimeTableDataC
import com.dmims.dmims.model.TimeTableData
import com.dmims.dmims.model.TimeTableDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import com.github.barteksc.pdfviewer.PDFView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import kotlinx.android.synthetic.main.activity_academic__calender.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.math.log

class AcademicCalender : AppCompatActivity() {
    var pdf: PDFView? = null
    private var time_table_url: String = "-"
    private var trsnsdlist: ArrayList<TimeTableDataRef>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_academic__calender)
        setSupportActionBar(toolbar)
        pdf = findViewById<PDFView>(R.id.pdfid)
try{
        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
        var call1: Call<TimeTableData> =
            phpApiInterface.timetable_details_url(
                "AC",
                "ALL",
                "2019-20",
                "ALL"
            )
        call1.enqueue(object : Callback<TimeTableData> {
            override fun onFailure(call1: Call<TimeTableData>, t: Throwable) {
                Toast.makeText(this@AcademicCalender, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call1: Call<TimeTableData>, response: Response<TimeTableData>) {
                var users = ArrayList<TimeTableDataC>()
                if (response.isSuccessful) {
                    users.clear()
                    trsnsdlist = response.body()!!.Data
                    if (trsnsdlist!![0].ID == "error") {
                        Toast.makeText(
                            this@AcademicCalender,
                            "No Data in Time Table Master.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        users.clear()
                        var listSize = trsnsdlist!!.size
                        for (i in 0..listSize - 1) {
                            if (trsnsdlist!![i].INSTITUTE == "AC") {
                                if (trsnsdlist!![i].TYPE == "ALL") {
                                    if (trsnsdlist!![i].YEAR == "2019-20") {
                                        if (trsnsdlist!![i].SEMESTER == "ALL") {
                                            time_table_url = trsnsdlist!![i].URL
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                downloadpdf(time_table_url)
            }
        })
    }catch (ex: Exception) {

        ex.printStackTrace()
        GenericUserFunction.showApiError(this,"Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time.")
    }
    }

    fun downloadpdf(time_table_url1: String) {
        FileLoader.with(this)
            .load(time_table_url1)
            .fromDirectory("PDFFiles", FileLoader.DIR_EXTERNAL_PUBLIC)
            .asFile(object : com.krishna.fileloader.listener.FileRequestListener<File> {
                override fun onLoad(request: FileLoadRequest?, response: FileResponse<File>?) {
                    var pdfFile: File = response!!.body
                    pdf!!.fromFile(pdfFile)
                        .password(null)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .enableDoubletap(true)
                        .swipeHorizontal(false)
                        .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                            println(canvas)
                            println(pageWidth)
                            println(pageHeight)
                            println(displayedPage)
                        }
                        .onDrawAll { canvas, pageWidth, pageHeight, displayedPage ->
                            println(canvas)
                            println(pageWidth)
                            println(pageHeight)
                            println(displayedPage)

                        }
                        .onPageError { page, t ->
                            println(page)
                            println(t)
                            Toast.makeText(
                                this@AcademicCalender,
                                "Error while open page",
                                Toast.LENGTH_SHORT
                            )
                        }
                        .onPageChange { page, pageCount ->

                        }
                        .onTap {
                            return@onTap true
                        }.onRender { nbPages, pageWidth, pageHeight ->
                            println(nbPages)
                            println(pageWidth)
                            println(pageHeight)
                            pdf!!.fitToWidth()
                        }
                        .enableAnnotationRendering(true)
                        .invalidPageColor(Color.WHITE)
                        .load()

                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    Toast.makeText(this@AcademicCalender, t!!.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
    }

}
