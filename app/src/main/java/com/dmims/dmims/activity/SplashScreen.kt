package com.dmims.dmims.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.dashboard.*
import com.dmims.dmims.model.APIResponse
import kotlinx.android.synthetic.main.activity_splash_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : AppCompatActivity() {
    var runnable = Runnable {
        run {
            receiveData()
        }
    }
    val PERMISSION_ALL = 1
    val PERMISSIONS = arrayOf<String>(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_SMS,
        android.Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        GenericPublicVariable.myCustomFont =
            Typeface.createFromAsset(assets, "candyroundbtnbold.ttf")
        tvSubHeading2.typeface = GenericPublicVariable.myCustomFont

//        if (InternetConnection.checkConnection(this@SplashScreen)) {
//            try{
//
//
//                var phpApiInterface: PhpApiInterface =
//                    ApiClientPhp.getClient()
//                        .create(PhpApiInterface::class.java)
//                var call3: Call<ApiVersion> =
//                    phpApiInterface.api_version()
//                call3.enqueue(object :
//                    Callback<ApiVersion> {
//                    override fun onFailure(
//                        call: Call<ApiVersion>,
//                        t: Throwable
//                    ) {
//                        Toast.makeText(
//                            this@SplashScreen,
//                            t.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    override fun onResponse(
//                        call: Call<ApiVersion>,
//                        response: Response<ApiVersion>
//                    ) {
//                        val result3: ApiVersion? =
//                            response.body()
////                        Toast.makeText(
////                            this@SplashScreen,
////                            result3!!.response,
////                            Toast.LENGTH_SHORT
////                        ).show()
//                        try {
//                            var pinfo = packageManager.getPackageInfo(packageName, 0)
//                            var versionNumber = pinfo.versionCode
//                            if (versionNumber.toString() == result3!!.response) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    if (arePermissionsEnabled()) {
//                                        var handler = Handler()
//                                        handler.postDelayed(runnable, 1300)
//                                    } else {
//                                        requestMultiplePermissions()
//                                    }
//                                } else {
//                                    var handler = Handler()
//                                    handler.postDelayed(runnable, 1300)
//                                }
//                            } else {
//                                GenericUserFunction.showSplashNegativePopUp(
//                                    this@SplashScreen,
//                                    getString(R.string.failureUpdateApiVerErr)
//                                )
//                            }
//                            //  }
//                            //                        } else {
//                            //                            GenericUserFunction.showInternetNegativePopUp(
//                            //                                this@SplashScreen,
//                            //                                getString(R.string.failureServerApiVerErr)
//                            //                            )
//                            //                        }
//                        } catch (ex: Exception) {
//                            ex.printStackTrace()
//                            GenericUserFunction.showApiError(
//                                this@SplashScreen,
//                                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
//                            )
//                        }
//                    }
//
//                })
//
//        }catch (ex: Exception) {
//            ex.printStackTrace()
//            GenericUserFunction.showApiError(
//                this,
//                "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
//            )
//       }
//        } else {
//            GenericUserFunction.showInternetNegativePopUp(this@SplashScreen, getString(R.string.failureNoInternetErr))
//        }
//    }
        if (InternetConnection.checkConnection(this@SplashScreen)) {
            try {
                GenericPublicVariable.mServices.AppVersion()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            GenericUserFunction.showInternetNegativePopUp(
                                this@SplashScreen,
                                getString(R.string.failureSSApiVerErr)
                            )
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            try {
                                val result: APIResponse? = response.body()
                                if (result!!.Responsecode == 200 && result.Status == "ok") {
                                    var pinfo = packageManager.getPackageInfo(packageName, 0)
                                    var versionNumber = pinfo.versionCode
                                    if (versionNumber.toString() == result.Data15!!.NEW_VERSION) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (arePermissionsEnabled()) {
                                                var handler = Handler()
                                                handler.postDelayed(runnable, 1300)
                                            } else {
                                                requestMultiplePermissions()
                                            }
                                        } else {
                                            var handler = Handler()
                                            handler.postDelayed(runnable, 1300)
                                        }
                                    } else {
                                        GenericUserFunction.showSplashNegativePopUp(
                                            this@SplashScreen,
                                            getString(R.string.failureUpdateApiVerErr)
                                        )
                                    }
                                } else {
                                    GenericUserFunction.showInternetNegativePopUp(
                                        this@SplashScreen,
                                        getString(R.string.failureServerApiVerErr)
                                    )
                                }
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                                GenericUserFunction.showApiError(
                                    this@SplashScreen,
                                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }
                        }


                    })
            } catch (ex: Exception) {
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvinience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this@SplashScreen,
                getString(R.string.failureNoInternetErr)
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun arePermissionsEnabled(): Boolean {
        for (permission: String in GenericPublicVariable.permission) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestMultiplePermissions() {
        for (permission: String in GenericPublicVariable.permission) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                GenericPublicVariable.remainingPermissions.add(permission)
        }
        println("Remaining permission >>> " + GenericPublicVariable.remainingPermissions.size + " elements >>> " + GenericPublicVariable.remainingPermissions)
        requestPermissions(
            GenericPublicVariable.remainingPermissions.toArray(arrayOf("" + GenericPublicVariable.remainingPermissions.size)),
            101
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        GenericUserFunction.showPerMissNegativePopUp(
                            this@SplashScreen,
                            getString(R.string.failureStorageErr)
                        )
                    }
                    return
                } else {
                    var handler = Handler()
                    handler.postDelayed(runnable, 1300)
                }
            }

        }
    }

    private fun receiveData() {
        GenericPublicVariable.mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        GenericPublicVariable.keyDrawerTitle =
            GenericPublicVariable.mypref!!.getString("key_drawer_title", null)
        GenericPublicVariable.keyEnrollNo =
            GenericPublicVariable.mypref!!.getString("key_enroll_no", null)
        GenericPublicVariable.keyAdminInfo =
            GenericPublicVariable.mypref!!.getString("key_admin_info", null)
        GenericPublicVariable.mobileNo =
            GenericPublicVariable.mypref!!.getString("key_editmob", null)
        GenericPublicVariable.password =
            GenericPublicVariable.mypref!!.getString("key_password", null)
        GenericPublicVariable.userRole =
            GenericPublicVariable.mypref!!.getString("key_userrole", null)
        GenericPublicVariable.DOA = GenericPublicVariable.mypref!!.getString("key_doa", null)
        if (GenericPublicVariable.mobileNo != null || GenericPublicVariable.password != null || GenericPublicVariable.keyDrawerTitle != null || GenericPublicVariable.keyEnrollNo != null || GenericPublicVariable.keyAdminInfo != null) {
            println("User Type >>> " + GenericPublicVariable.userRole)

            if (GenericPublicVariable.userRole.equals(
                    "Student",
                    ignoreCase = true
                ) || GenericPublicVariable.userRole.equals("Parent", ignoreCase = true)
            ) {
                val intent = Intent(applicationContext, StudentDashboard::class.java)
                startActivity(intent)
            }
            if (GenericPublicVariable.userRole.equals("Admin", ignoreCase = true)) {
                val intent = Intent(applicationContext, AdminDashboard::class.java)
                startActivity(intent)
            }
            if (GenericPublicVariable.userRole.equals("Institute", ignoreCase = true)) {
                val intent = Intent(applicationContext, InstituteDashboard::class.java)
                startActivity(intent)
            }
            if (GenericPublicVariable.userRole.equals("Faculty", ignoreCase = true)) {
                val intent = Intent(applicationContext, FacultyDashboard::class.java)
                startActivity(intent)
            }

            if (GenericPublicVariable.userRole.equals("REGISTRAR", ignoreCase = true)) {
                val intent = Intent(applicationContext, RegisterarCellDashboard::class.java)
                startActivity(intent)
            }

            if (GenericPublicVariable.userRole.equals("GREVIANCE_CELL", ignoreCase = true)) {
                val intent = Intent(applicationContext, GreivanceCellDashboard::class.java)
                startActivity(intent)
            }

            if (GenericPublicVariable.userRole.equals("CONVENER", ignoreCase = true)) {
//                val intent = Intent(applicationContext, FacultyDashboard::class.java)
//                startActivity(intent)
            }

            if (GenericPublicVariable.userRole.equals("COCONVENER", ignoreCase = true)) {
//                val intent = Intent(applicationContext, Conve::class.java)
//                startActivity(intent)
            }
            if (GenericPublicVariable.userRole.equals("EXAMINCHARGE", ignoreCase = true)) {
                val intent = Intent(applicationContext, ExamCellDashboard::class.java)
                startActivity(intent)
            }
//            if (userrole.equals("EXAM_FEEDBACK_ADMIN", ignoreCase = true)) {
//                val intent = Intent(applicationContext, Exam_FeedBack_Admin_Dashboard::class.java)
//                startActivity(intent)
//            }
        } else {
            val intent = Intent(applicationContext, RegActivity::class.java)
            startActivity(intent)
        }
    }

}
