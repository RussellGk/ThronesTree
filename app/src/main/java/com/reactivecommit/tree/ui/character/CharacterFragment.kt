package com.reactivecommit.tree.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.reactivecommit.tree.data.HouseType
import com.reactivecommit.tree.databinding.FragmentCharacterBinding
import com.reactivecommit.tree.ui.RootActivity

class CharacterFragment : Fragment() {
    private val args: CharacterFragmentArgs by navArgs()
    private lateinit var viewModel: CharacterViewModel

    private var _binding: FragmentCharacterBinding? = null
    private val  binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(args.id)
        ).get(CharacterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val houseType = args.house
        val arms = args.house.coastOfArms
        val scrim = args.house.primaryColor
        val scrimDark = args.house.darkColor

        val rootActivity = requireActivity() as RootActivity
        rootActivity.setSupportActionBar(binding.toolbar)
        rootActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = args.title
        }

        binding.ivArms.setImageResource(arms)
        with(binding.collapsingLayout) {
            setBackgroundResource(scrim)
            setContentScrimResource(scrim)
            setStatusBarScrimResource(scrimDark)
        }

        binding.collapsingLayout.post { binding.collapsingLayout.requestLayout() }

        viewModel.getCharacter().observe(viewLifecycleOwner, Observer { character ->
            if (character == null) return@Observer

            val iconColor = requireContext().getColor(houseType.accentColor)

            listOf(binding.tvWords, binding.tvBorn, binding.tvTitles, binding.tvAliases)
                .forEach { it.compoundDrawables.firstOrNull()?.setTint(iconColor) }

            binding.tvWords.text = character.words
            binding.tvBorn.text = character.born
            binding.tvTitles.text = character.titles
                .filter { it.isNotEmpty() }
                .joinToString("\n")
            binding.tvAliases.text = character.aliases
                .filter { it.isNotEmpty() }
                .joinToString("\n")

            character.father?.let {
                binding.groupFather.isVisible = true
                binding.btnFather.text = it.name
                val action = CharacterFragmentDirections.actionCharacterFragmentSelf(
                    it.id,
                    HouseType.fromString(it.house),
                    it.name
                )
                binding.btnFather.setOnClickListener { findNavController().navigate(action) }
            }

            character.mother?.let {
                binding.groupMother.isVisible = true
                binding.btnMother.text = it.name
                val action = CharacterFragmentDirections.actionCharacterFragmentSelf(
                    it.id,
                    HouseType.fromString(it.house),
                    it.name
                )
                binding.btnMother.setOnClickListener { findNavController().navigate(action) }
            }

            if (character.died.isNotBlank()) {
                Snackbar.make(
                    binding.coordinator,
                    "Died in : ${character.died}",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}