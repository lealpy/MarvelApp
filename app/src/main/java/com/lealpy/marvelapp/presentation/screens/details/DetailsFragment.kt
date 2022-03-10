package com.lealpy.marvelapp.presentation.screens.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.lealpy.marvelapp.R
import com.lealpy.marvelapp.databinding.FragmentDetailsBinding
import com.lealpy.marvelapp.presentation.models.CharacterUi
import com.lealpy.marvelapp.presentation.utils.Const
import com.lealpy.marvelapp.presentation.utils.Const.CHARACTER_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        getArgs()
        initObservers()
    }

    private fun getArgs() {
        arguments?.getParcelable<CharacterUi>(CHARACTER_KEY)?.let { characterUi ->
            viewModel.onArgsReceived(characterUi)
        }
    }

    private fun initObservers() {
        viewModel.characterUi.observe(viewLifecycleOwner) { characterUi ->
            binding.characterName.text = characterUi.name
            binding.characterDescription.text = characterUi.description

            try {
                Glide.with(requireContext())
                    .load(characterUi.imageURL)
                    .placeholder(R.drawable.ic_baseline_sentiment_dissatisfied_24)
                    .error(R.drawable.ic_baseline_sentiment_dissatisfied_24)
                    .into(binding.characterImage)
            } catch (e: Exception) {
                Log.e(Const.APP_LOG_TAG, e.message.toString())
                binding.characterImage.setImageResource(
                    R.drawable.ic_baseline_sentiment_dissatisfied_24
                )
            }
        }
    }

}
