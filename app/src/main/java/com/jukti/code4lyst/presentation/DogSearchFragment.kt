package com.jukti.code4lyst.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.jukti.code4lyst.databinding.FragmentDogSearchBinding
import com.jukti.code4lyst.domain.model.BreedDomainModel
import com.jukti.code4lyst.domain.model.ImageDomainModel
import com.jukti.unrd.utilities.CREDIT_ITEM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DogSearchFragment : Fragment() {

    lateinit var dogSearchViewModel: DogSearchViewModel
    lateinit var breedAdapter: BreedAdapter
    @Inject
    lateinit var imageAdapter: ImageAdapter
    private var _binding: FragmentDogSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dogSearchViewModel = ViewModelProvider(this).get(DogSearchViewModel::class.java)
        breedAdapter = BreedAdapter(requireActivity())
        _binding = FragmentDogSearchBinding.inflate(inflater, container, false)

        binding.breedSpinner.apply {
            setAdapter(breedAdapter)
        }
        binding.breedSpinner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id->
                // do something with the available information
                Log.e(TAG,"Position: $position")
                val selectedBreed = breedAdapter.collection[position]
                binding.breed = selectedBreed
                dogSearchViewModel.setSelectedBreedId(selectedBreed.id)
            }

        binding.viewPager2.adapter = imageAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->

        }.attach()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

    }

    private fun initObservers() {
        dogSearchViewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { it -> processResponse(it)}
            .launchIn(lifecycleScope)

        dogSearchViewModel.images
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { it -> processResponse(it)}
            .launchIn(lifecycleScope)
    }


    private fun processResponse(state: DogSearchViewState) {
        when(state){
            is DogSearchViewState.Init -> Unit
            is DogSearchViewState.ErrorResponse -> handleError(state.message)
            is DogSearchViewState.SuccessResponseBreed -> handleSuccessResponseBreed(state.breedList)
            is DogSearchViewState.ShowToast -> showToast(state.message)
            is DogSearchViewState.IsLoading -> handleLoading(state.isLoading)
            is DogSearchViewState.SuccessResponseImage -> handleSuccessResponseImage(state.imageList)
        }
    }

    private fun handleSuccessResponseImage(imageList: List<ImageDomainModel>) {
        imageList.map { it.url?.let { it1 -> Log.e(TAG, it1) } }
        imageAdapter.collection = imageList.toMutableList()
        binding.viewPager2.setCurrentItem(0,true)
    }

    private fun handleError(rawResponse: String) {
        Log.e(TAG,"handleError")
        activity?.let {
            Snackbar.make(it.findViewById(android.R.id.content), rawResponse, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun handleSuccessResponseBreed(breedList:List<BreedDomainModel>) {
        breedAdapter.collection = breedList.toMutableList()
    }

    private fun showToast(message: String) {
        Log.e(TAG,"showToast")
        activity?.let {
            Snackbar.make(it.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun handleLoading(loading: Boolean) {
        Log.e(TAG,"handleLoading")
        if(loading) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else
            binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TAG = "DogSearchFragment"
    }
}