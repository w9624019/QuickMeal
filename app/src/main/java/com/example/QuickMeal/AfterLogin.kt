package com.example.QuickMeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.example.QuickMeal.databinding.ActivityAfterLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AfterLogin : AppCompatActivity() {
    private var backPressedTime: Long = 0
    private lateinit var binding:ActivityAfterLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAfterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.bottomBar.setOnItemSelectedListener{
            when(it){
                0->{
                   findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment)
                }
                1->{
                    findNavController(R.id.fragmentContainerView).navigate(R.id.cartFragment)
                }
                2->{
                    findNavController(R.id.fragmentContainerView).navigate(R.id.profileFragment)
                }
            }
        }
    }

    private fun navigate(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        // Replace the current fragment with the new one
        transaction.replace(R.id.fragmentContainerView, fragment)

        // Optionally, add the transaction to the back stack


        // Commit the transaction
        transaction.commit()
    }
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime > 2000) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backPressedTime = currentTime
        } else {
            super.onBackPressed()
            // This is optional, you may want to use this depending on your needs.
            finishAffinity()

        }
    }
}