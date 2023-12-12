package com.example.QuickMeal.Ordering_Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.QuickMeal.Adapters.RestroShowerAdapter
import com.example.QuickMeal.Adapters.SearchAdapter
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.DataModels.Firebase.DishesInALocation
import com.example.QuickMeal.R
import com.example.QuickMeal.Util.Rsource
import com.example.QuickMeal.ViewModel.BestProductViewModel
import com.example.QuickMeal.ViewModel.GetLocationRestroViewModel
import com.example.QuickMeal.databinding.HomeFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private val TAG="HomeFragment"
@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private val navArgs by navArgs<HomeFragmentArgs>()
    private val SearchAdapter by lazy { SearchAdapter() }
    private lateinit var RealTimeFirebase:FirebaseDatabase
    private val bestProductAdapter by lazy { RestroShowerAdapter() }
    private val viewModel by viewModels<GetLocationRestroViewModel>()
    private val viewModel2 by viewModels<BestProductViewModel>()
    private val firebase by lazy { FirebaseDatabase.getInstance() }
    private lateinit var datalist:ArrayList<Dish>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=HomeFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datalist=ArrayList()
RealTimeFirebase=FirebaseDatabase.getInstance()
        val imageList = ArrayList<SlideModel>()


        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        Log.d(TAG, imageList.toString())

        binding.imageSlider.setImageList(imageList)

   binding.powerSpinnerView2.setOnClickListener {
       findNavController().navigate(R.id.action_homeFragment_to_changeLocationFragment)
   }


getData(navArgs.Location)

        if (navArgs.Location != null) {
            viewModel.getdata(navArgs.Location)
            bestProductAdapter.onClick = { selectedItem ->
                val bundle = Bundle()
                bundle.putParcelable("restro", selectedItem)

                // Assuming Location is a string argument from navigation graph
                val location = arguments?.getString("Location")
                bundle.putString("location", location)

                findNavController().navigate(R.id.action_homeFragment_to_restrodetails_with_its_food_fragment, bundle)
            }

        } else {
            viewModel.getdata("London")
            bestProductAdapter.onClick={
                val b=Bundle()
                b.putParcelable("restro",it)
                val location = arguments?.getString("Location")
                b.putString("location", location)
                findNavController().navigate(R.id.action_homeFragment_to_restrodetails_with_its_food_fragment,b)

            }
                }

binding.textView22.text="in ${navArgs.Location}"

        setupBestProductRv()
        setupSearchRV()
        // ... (previous code)

        lifecycleScope.launchWhenStarted {
            viewModel2.productData.collectLatest {
                when (it) {
                    is Rsource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is Rsource.Success -> {
                        for (data in it.data!!) {
                            datalist.add(data)
                        }

                        // Log the datalist here, inside the collectLatest block
                        Log.d(TAG, "datalist size: ${datalist.size}")
                    }
                    else -> Unit
                }
            }
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that characters within `charSequence` are about to be replaced with new text
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within `charSequence` changes have occurred
                val currentText = charSequence.toString()
                // Do something with the current text, e.g., log it or update a variable
                Log.d("TextChanged", "Current Text: $currentText")
                if (currentText.isEmpty()) {
                    binding.SearchRecycleViewer.visibility = View.GONE
                } else {
                    binding.SearchRecycleViewer.visibility = View.VISIBLE
                    binding.progressBar5.visibility = View.GONE

                    Log.d(TAG, "datalst size: ${datalist.size}")
                    // Assuming you want to update the RecyclerView when the text is not empty
                    searchList(currentText, datalist)
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                // This method is called to notify you that the characters within `editable` have been changed
            }
        })

        Log.d(TAG, datalist.toString())


        lifecycleScope.launchWhenStarted {
            viewModel.restroData.collectLatest {
                when(it){
                    is Rsource.Error -> {
                        binding.progressBar.visibility=View.GONE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                        Log.d(TAG, it.message.toString())
                    }
                    is Rsource.Loading -> {
                        binding.progressBar.visibility=View.VISIBLE
                    }
                    is Rsource.Success -> {   binding.progressBar.visibility=View.GONE

                        Log.d(TAG, it.data.toString())
                        bestProductAdapter.differ.submitList(it.data)
                    }
                    else -> Unit

                }
            }
        }




//        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
//                // This method is called to notify you that characters within `charSequence` are about to be replaced with new text
//            }
//
//            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
//                // This method is called to notify you that somewhere within `charSequence` changes have occurred
//                val currentText = charSequence.toString()
//                // Do something with the current text, e.g., log it or update a variable
//                Log.d("TextChanged", "Current Text: $currentText")
//                if (currentText.isEmpty()) {
//                    binding.SearchRecycleViewer.visibility = View.GONE
//                } else {
//                    binding.SearchRecycleViewer.visibility = View.VISIBLE
//                    binding.progressBar5.visibility=View.GONE
//
//                    Log.d(TAG, "datalst size"  +datalist.size.toString())
//                    // Assuming you want to update the RecyclerView when the text is not empty
//                    searchList(currentText,datalist)
//                }
//
//
//            }
//
//            override fun afterTextChanged(editable: Editable?) {
//                // This method is called to notify you that the characters within `editable` have been changed
//            }
//        })



        SearchAdapter.onClick={
            navigate(it,navArgs.Location)
        }

    }

    private fun setupBestProductRv() {
       binding.bestProductRV.apply {
           layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
           adapter=bestProductAdapter
       }
    }
    private fun navigate(product: Dish, location: String) {

        val intent= Intent(requireContext(), Dish_Details::class.java).apply {
            putExtra("id",product.id)
            putExtra("location",location)
            putExtra("Name",product.name)
            putExtra("img",product.img)

            putExtra("prices",product.price.toString())
            putExtra("category",product.category)
            putExtra("restaurant_id",product.restaurant_id.toString())

        }
       startActivity(intent)

    }

    fun searchList(text: String, datalist2: ArrayList<Dish>){
        Log.d(TAG, "dcjks"+datalist.toString())
        binding.progressBar.visibility=View.VISIBLE
        binding.SearchRecycleViewer.visibility=View.VISIBLE
        val searchList=ArrayList<Dish>()
        for(dataClass in datalist2){


                if(dataClass.name?.toLowerCase()?.contains(text.lowercase())==true){
                    searchList.add(dataClass)

            }


        }
        Log.d("Search Fragment", searchList.toString())

        if(searchList.isEmpty()){

            binding.SearchRecycleViewer.visibility=View.GONE
            binding.progressBar5.visibility=View.GONE
            return
        }

        SearchAdapter.differ.submitList(searchList)
        SearchAdapter.notifyDataSetChanged()
        binding.progressBar5.visibility=View.GONE
    }

//    fun getData(){
//        binding.progressBar5.visibility=View.VISIBLE
//        firebase.getReference("dishes").addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                datalist.clear()
//                for (item in snapshot.children)  {
//
//                    val dataClass=item.getValue(Dish::class.java)
//                    if(dataClass!==null){
//                        datalist.add(dataClass)
//                    }
//                }
//
//                Log.d("Search Fragment", datalist.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//        binding.progressBar5.visibility=View.GONE
//    }


    fun getData(location: String){
      firebase.getReference("dishes").orderByChild("Location_name").equalTo(location).addValueEventListener(object :
          ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              if(snapshot.exists()){
                  for (data in snapshot.children){
                      Log.d("bestPRoduct", snapshot.children.toString())

                      val data=data.getValue(DishesInALocation::class.java)
                      Log.d("bestPRoduct", data!!.Location_name.toString())
                      for (food in data!!.Available_Dishes){
                          datalist.add(food)
                      }

                  }
              }
          }

          override fun onCancelled(error: DatabaseError) {
              TODO("Not yet implemented")
          }
      })
    }
    private fun setupSearchRV() {
        binding.SearchRecycleViewer.apply {
            layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter=SearchAdapter
            itemDecorationCount
        }
    }
}