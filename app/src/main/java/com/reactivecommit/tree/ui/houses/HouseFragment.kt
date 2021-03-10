package com.reactivecommit.tree.ui.houses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.reactivecommit.tree.R
import com.reactivecommit.tree.data.HouseType
import com.reactivecommit.tree.databinding.FragmentHouseBinding
import com.reactivecommit.tree.ui.ItemDivider

class HouseFragment : Fragment() {
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var viewModel: HouseViewModel

    private var _binding: FragmentHouseBinding? = null
    private val  binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val house = HouseType.valueOf(arguments?.getString(HOUSE_NAME) ?: HouseType.STARK.name)
        val vmFactory = HouseViewModelFactory(house)
        characterAdapter = CharacterAdapter {
            val action =
                HousesFragmentDirections.actionNavHousesToCharacterFragment(it.id, house, it.name)
            findNavController().navigate(action)
        }

        viewModel = ViewModelProvider(this, vmFactory).get(HouseViewModel::class.java)
        viewModel.getCharacters().observe(this, Observer {
            characterAdapter.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerCharacters) {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(ItemDivider())
            adapter = characterAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        with(menu.findItem(R.id.action_search).actionView as SearchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.handleSearchQuery(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.handleSearchQuery(newText)
                    return true
                }
            })
        }
        super.onPrepareOptionsMenu(menu)
    }

    companion object {
        private const val HOUSE_NAME = "HOUSE_NAME"

        @JvmStatic
        fun newInstance(houseType: HouseType) =
            HouseFragment().apply {
                arguments = bundleOf(HOUSE_NAME to houseType.name)
            }
    }
}