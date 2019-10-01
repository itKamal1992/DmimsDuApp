package com.dmims.dmims.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
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

class Common_PDF_Viewer : AppCompatActivity() {
    var pdf: PDFView? = null
    var probar:ProgressBar? = null
    var txt_titile:TextView? = null

    private var time_table_url: String = "-"
    private var trsnsdlist: ArrayList<TimeTableDataRef>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_pdf_viewer_layout)
        setSupportActionBar(toolbar)
        pdf = findViewById<PDFView>(R.id.pdfid)
        probar = findViewById<ProgressBar>(R.id.progressBar7)
        txt_titile = findViewById<TextView>(R.id.txt_titile)
        probar!!.visibility = View.VISIBLE
try{

    var url:String=intent.getStringExtra("url")
    try {
        var actionTitle: String = intent.getStringExtra("actionTitle")
        txt_titile!!.setText(actionTitle)
    }catch (ex:java.lang.Exception)
    {
        txt_titile!!.setText("Nahi Mila")
    }

    downloadpdf(url)
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
                            probar!!.visibility = View.GONE
                            Toast.makeText(
                                this@Common_PDF_Viewer,
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
                    probar!!.visibility = View.GONE

                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    probar!!.visibility = View.GONE
                    Toast.makeText(this@Common_PDF_Viewer, t!!.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
    }

}
