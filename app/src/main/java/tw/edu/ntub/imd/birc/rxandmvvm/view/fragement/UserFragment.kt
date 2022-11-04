package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.AddWaterRecordActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity


class UserFragment : Fragment() {

    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var profileReturn:ImageButton
    private lateinit var profileEmail:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        profileReturn = view.findViewById<ImageButton>(R.id.profile_return)
        profileEmail = view.findViewById<TextView>(R.id.profile_account)

        profileReturn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        profileEmail.text= firebaseAuth.currentUser?.email
        checkUser()

        return view
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser==null){
            requireActivity().run{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }else{
            val email = firebaseUser.email
        }
    }


}