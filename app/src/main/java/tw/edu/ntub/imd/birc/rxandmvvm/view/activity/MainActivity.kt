package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.HomeDietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.UserItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.MainViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var loginRecyclerInvisibility: RecyclerView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var account: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button


    companion object {
        val createDietRecordFragment by lazy { CreateDietRecordFragment() }
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
        private const val CLIENT_ID =
            "1097925309910-jj36pi7mi31hhqov4va9gn3mtshujp7t.apps.googleusercontent.com"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val googleSignInButton = findViewById<ImageView>(R.id.loginGoogleSignIn)
        account = findViewById(R.id.loginEditAccount)
        password = findViewById(R.id.loginEditPassword)
        loginRecyclerInvisibility = findViewById(R.id.login_recycler_invisibility)
        loginBtn = findViewById(R.id.login_login_btn)
        loginRecyclerInvisibility.visibility = View.INVISIBLE

        loginBtn.setOnClickListener {
            val adapter = ObservableAdapter(
                viewModel.login(account.text.toString(), password.text.toString())
                    .mapSourceState {
                        it.data.map { user ->
                            when (user.account) {
                                "nAccount" -> {
                                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                                        Toast.makeText(
                                            applicationContext,
                                            "帳號尚未註冊",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                                }
                                "eAccount" -> {
                                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                                        Toast.makeText(
                                            applicationContext,
                                            "密碼錯誤",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                                }
                                else -> {
                                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                                        Toast.makeText(
                                            applicationContext,
                                            "登入成功",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                                    val sharedPreference =
                                        getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                                    val editor = sharedPreference.edit()
                                    editor.putString("account", user.account)
                                    editor.putString("password", password.text.toString())
                                    editor.putString("isEmailLogin","0")
                                    editor.putString("isLogin","1")
                                    editor.apply()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    account.setText("")
                                    password.setText("")
                                }
                            }
                        }
                        it.data.map { user ->
                            UserItem(user)
                        }
                    }
            )
            adapter.attachToRecyclerView(loginRecyclerInvisibility)

        }

        val register = findViewById<TextView>(R.id.login_text_register)
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent);
        }

        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        googleSignInButton.setOnClickListener {
            Log.d(TAG, "onCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }


        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("本APP需要相機權限才可以運作, 請開啟權限")
                    .setPositiveButton("確定") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    .setNegativeButton("拒絕") { _, _ -> finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        } else if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    READ_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("本APP需要存取權限才可以運作, 請開啟權限")
                    .setPositiveButton("確定") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    .setNegativeButton("拒絕") { _, _ -> finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }

    }

    private fun checkUser() {
//        val firebaseUser = firebaseAuth.currentUser
//        if (firebaseUser != null) {
//            startActivity(Intent(this, HomeActivity::class.java));
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)

            } catch (e: Exception) {
                Log.d(TAG, "onActivityResult: ${e.stackTraceToString()}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Login")

                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                if (authResult.additionalUserInfo!!.isNewUser) {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created... \n$email")
                    Toast.makeText(
                        this@MainActivity,
                        "Account created... \n$email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user... \n$email")
                    Toast.makeText(this@MainActivity, "LoggedIn... \n$email", Toast.LENGTH_SHORT)
                        .show()
                }
                val sharedPreference =
                    getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("account", email)
                editor.putString("isEmailLogin","1")
                editor.putString("isLogin","1")
                editor.apply()
                val adapter = ObservableAdapter(
                    viewModel.login(email.toString(), password.text.toString())
                        .mapSourceState {
                            it.data.map { user ->
                                when (user.account) {
                                    "nAccount" -> {
                                        val account: String = email.toString()
                                        val jsonObject = JSONObject()
                                        jsonObject.put("account", account)
                                        jsonObject.put("password", "password")
                                        jsonObject.put("isEmailLogin", "1")
                                        val jsonObjectString = jsonObject.toString()
                                        val requestBody =
                                            jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                                        viewModel.register(requestBody)
                                            .enqueue(object : Callback<User> {
                                                override fun onResponse(
                                                    call: Call<User>,
                                                    response: Response<User>
                                                ) {
                                                    if (response.isSuccessful) {
                                                        Log.d("Retrofit", "Success")
                                                    }
                                                }

                                                override fun onFailure(call: Call<User>, t: Throwable) {
                                                }

                                            })
                                    }
                                }
                            }
                            it.data.map { user ->
                                UserItem(user)
                            }
                        }
                )
                adapter.attachToRecyclerView(loginRecyclerInvisibility)
                startActivity(Intent(this, HomeActivity::class.java));
                finish()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Loggin Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    //切到添加飲食紀錄的畫面
    fun goBtn1(view: View) {
        startActivity(Intent(this, AddDietRecordActivity::class.java))
    }


}