package com.reactivecommit.tree.ui.houses

import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.ColorInt
import androidx.appcompat.widget.SearchView
import androidx.core.animation.doOnEnd
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.reactivecommit.tree.R
import com.reactivecommit.tree.data.HouseType
import com.reactivecommit.tree.databinding.FragmentHousesBinding
import com.reactivecommit.tree.ui.RootActivity
import kotlin.math.hypot
import kotlin.math.max

class HousesFragment : Fragment() {
    private lateinit var colors: Array<Int>
    private lateinit var housesPagerAdapter: HousesPagerAdapter

    private var _binding: FragmentHousesBinding? = null
    private val  binding get() = _binding!!

    @ColorInt
    private var currentColor: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        housesPagerAdapter = HousesPagerAdapter(childFragmentManager)
        colors = requireContext().run {
            arrayOf(
                getColor(R.color.stark_primary),
                getColor(R.color.lannister_primary),
                getColor(R.color.targaryen_primary),
                getColor(R.color.baratheon_primary),
                getColor(R.color.greyjoy_primary),
                getColor(R.color.martel_primary),
                getColor(R.color.tyrel_primary)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        with(menu.findItem(R.id.action_search)?.actionView as SearchView) {
            queryHint = "Search character"
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHousesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as RootActivity).setSupportActionBar(binding.toolbar)

        if (currentColor != -1) binding.appbar.setBackgroundColor(currentColor)

        binding.viewPager.adapter = housesPagerAdapter

        with(binding.tabs) {
            setupWithViewPager(binding.viewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) = Unit
                override fun onTabUnselected(p0: TabLayout.Tab?) = Unit
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val position = tab.position

                    if ((binding.toolbar.background as ColorDrawable).color != colors[position]) {
                        val rect = Rect()

                        @Suppress("INACCESSIBLE_TYPE")
                        val tabView = tab.view as View

                        tabView.postDelayed({
                            tabView.getGlobalVisibleRect(rect)
                            animateAppbarReveal(
                                position,
                                rect.centerX(),
                                rect.centerY(),
                                binding.appbar,
                                binding.revealView
                            )
                        }, 300) //default animation in ms
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun animateAppbarReveal(
        position: Int,
        centerX: Int,
        centerY: Int,
        appbar: AppBarLayout,
        revealView: View
    ) {
        val endRadius = max(
            hypot(centerX.toDouble(), centerY.toDouble()),
            hypot(appbar.width.toDouble() - centerX.toDouble(), centerY.toDouble())
        )

        with(revealView) {
            isVisible = true
            setBackgroundColor(colors[position])
        }

        ViewAnimationUtils.createCircularReveal(
            revealView,
            centerX,
            centerY,
            0f,
            endRadius.toFloat()
        ).apply {
            doOnEnd {
                appbar.setBackgroundColor(colors[position])
                revealView.isInvisible = true
            }
        }.start()
    }

    class HousesPagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return HouseFragment.newInstance(HouseType.values()[position])
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return HouseType.values()[position].title
        }

        override fun getCount(): Int = HouseType.values().size
    }
}

