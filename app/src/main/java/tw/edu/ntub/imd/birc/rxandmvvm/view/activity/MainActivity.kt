package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*


class MainActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth


    companion object {
//        val homeFragment = HomeFragment()
//
//        //原本是val recordFragment = RecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
//        val recordFragment by lazy { RecordFragment() }
//        val userFragemnt = UserFragment()
//        val addFragment = AddFragment()
//        val otherFragment = OtherFragment()
//
//        //原本是val createDietRecordFragment = CreateDietRecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
//        val createDietRecordFragment by lazy { CreateDietRecordFragment() }

        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
        private const val CLIENT_ID =
            "1097925309910-jj36pi7mi31hhqov4va9gn3mtshujp7t.apps.googleusercontent.com"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


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

        val googleSignInButton = findViewById<ImageView>(R.id.loginGoogleSignIn)

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


    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!=null){
            startActivity(Intent(this, HomeActivity::class.java));
        }
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

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    finish()
//                }
//                return
//            }
//        }
//    }
//


    //新版button.setOnClickListener的實作方法(歷史測試用按鈕)
    fun btn_onClik(view: View) {
        //val button: Button = findViewById(R.id.button1),好像不用
        //button.setOnClickListener {  },好像不用了刪掉也沒報錯
        startActivity(Intent(this, FoodDetailActivity::class.java))

    }

    //切到添加飲食紀錄的畫面
    fun goBtn1(view: View) {
        startActivity(Intent(this, AddDietRecordActivity::class.java))
    }

    fun goRecord(view: View) {
        startActivity(Intent(this, HomeActivity::class.java))
    }


}