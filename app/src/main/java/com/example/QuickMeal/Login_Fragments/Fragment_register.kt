package com.example.QuickMeal.Login_Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.QuickMeal.DataModels.Users
import com.example.QuickMeal.R
import com.example.QuickMeal.databinding.FragmentRegisterBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.Executor

private val TAG="Fragment_register"
class Fragment_register : Fragment(R.layout.fragment_register) {

private lateinit var binding: FragmentRegisterBinding
private lateinit var auth:FirebaseAuth
private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth=FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()
        binding.gotologinPageRegi.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_register_to_fragment_login)
        }
        binding.submit.isClickable = false
        binding.submit.visibility=View.GONE



        binding.captchaButton.setOnClickListener {
            if (!isPasswordValid(binding.Password.text.toString()) && binding.Password.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please follow password format",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            isCaptchaPassed { isPassed ->
                if (isPassed) {
                    binding.captchaButton.visibility=View.GONE
                    binding.submit.visibility=View.VISIBLE
                    Toast.makeText(requireContext(), "Captcha passed", Toast.LENGTH_SHORT).show()
                } else {
                    // Captcha not passed, do something else
                    Toast.makeText(requireContext(), "Captcha not passed", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.submit.setOnClickListener {
            binding.submit.startAnimation()


            // Retrieve email and password when  the button is clicked
            val email = binding.email.text.toString()
            val password = binding.Password.text.toString()

            if (!isPasswordValid(password)) {

                binding.submit.revertAnimation()
                Toast.makeText(
                    requireContext(),
                    "Password should be alphanumeric",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Check if email or password is empty
            if (email.isEmpty() || password.isEmpty()||binding.FirstName.text.isEmpty()||binding.LastName.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Fill all the Feilds",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            else if(Patterns.EMAIL_ADDRESS.equals(email)){
                Toast.makeText(
                    requireContext(),
                    "Wrong Email Format",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }



            // Log the details
            Log.d("logindetails", "login Details: $email $password")

            // Perform user creation with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->

                if (task.isSuccessful) {

                    val userInfo:Users= Users(binding.FirstName.text.toString(),binding.LastName.text.toString(),email,""
                    )


                     addData(userInfo)
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail: success")
                    val user = auth.currentUser
                    Log.d("user", "onCreateView: ${user!!.uid}")

                    Toast.makeText(requireContext(), "Account Created SuccessFull", Toast.LENGTH_SHORT).show()
                    binding.submit.revertAnimation()
                    navigate()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("fail", "createUserWithEmail: failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("Register", task.exception?.message.toString())
                    binding.submit.revertAnimation()
                }
            }
        }
    }

    private fun navigate() {
        findNavController().navigate(R.id.action_fragment_register_to_fragment_login)

    }

    private fun addData(userInfo: Users) {

        firestore.collection("users").document(auth.uid.toString()).set(userInfo)


    }
    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$".toRegex()
        return password.matches(passwordRegex)
    }

//    fun onClick(view: View): Boolean {
//        SafetyNet.getClient(requireContext()).verifyWithRecaptcha("6Lef9S0pAAAAAEkoany3mC_eBOSA3UYr1uyvwkd5")
//            .addOnSuccessListener(this as Executor, OnSuccessListener { response ->
//                // Indicates communication with reCAPTCHA service was
//                // successful.
//                val userResponseToken = response.tokenResult
//                // CAPTCHA passed
//                // You can perform any additional actions here if needed
//                // ...
//                // Return true to indicate success
//                if (response.tokenResult?.isNotEmpty() == true) return@OnSuccessListener true
//            })
//            .addOnFailureListener(this as Executor, OnFailureListener { e ->
//                if (e is ApiException) {
//                    // An error occurred when communicating with the
//                    // reCAPTCHA service. Refer to the status code to
//                    // handle the error appropriately.
//                    Log.d(TAG, "Error: ${CommonStatusCodes.getStatusCodeString(e.statusCode)}")
//                } else {
//                    // A different, unknown type of error occurred.
//                    Log.d(TAG, "Error: ${e.message}")
//                }
//
//                // CAPTCHA failed, return false
//                return@OnFailureListener false
//            })
//
//        // Default case, return false
//        return false
//    }

    fun isCaptchaPassed(callback: (Boolean) -> Unit) {
        SafetyNet.getClient(requireContext()).verifyWithRecaptcha("6Lef9S0pAAAAAEkoany3mC_eBOSA3UYr1uyvwkd5")
            .addOnSuccessListener(requireActivity(), OnSuccessListener { response ->
                // Indicates communication with reCAPTCHA service was successful.
                val userResponseToken = response.tokenResult
                val isPassed = response.tokenResult?.isNotEmpty() == true
                callback(isPassed)
            })
            .addOnFailureListener(requireActivity(), OnFailureListener { e ->
                if (e is ApiException) {
                    // An error occurred when communicating with the reCAPTCHA service.
                    Log.d(TAG, "Error: ${CommonStatusCodes.getStatusCodeString(e.statusCode)}")
                } else {
                    // A different, unknown type of error occurred.
                    Log.d(TAG, "Error: ${e.message}")
                }
                callback(false)
            })
    }



}