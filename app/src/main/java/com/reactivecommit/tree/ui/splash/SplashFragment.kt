package com.reactivecommit.tree.ui.splash

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.reactivecommit.tree.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var animation: AnimatorSet

    private var _binding: FragmentSplashBinding? = null
    private val  binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pulseAnimator = ValueAnimator.ofFloat(1f, 1.2f, 1f).apply {
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            duration = 1000
            addUpdateListener {
                val zoom = it.animatedValue as Float
                binding.splashImage.apply {
                    scaleX = zoom
                    scaleY = zoom
                }
            }
        }

        val tintAnimator = ValueAnimator.ofArgb(
            Color.BLACK,
            Color.WHITE,
            Color.BLACK,
            Color.RED,
            Color.BLACK,
            Color.MAGENTA,
            Color.BLACK,
            Color.YELLOW,
            Color.BLACK,
            Color.CYAN,
            Color.BLACK
        ).apply {
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            duration = 5000
            addUpdateListener {
                val color = it.animatedValue as Int
                binding.splashImage.imageTintList = ColorStateList.valueOf(color)
            }
        }

        animation = AnimatorSet().apply {
            playTogether(tintAnimator, pulseAnimator)
            duration = 10000
            start()
        }
    }

    override fun onDestroyView() {
        animation.cancel()
        super.onDestroyView()
        _binding = null
    }
}