package ru.divarteam.franimal.presentation.auth

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.ActivityAuthBinding
import ru.divarteam.franimal.domain.repository.PreferenceRepository
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.loading.observe(this) {
            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            TransitionManager.beginDelayedTransition(binding.root, sharedAxis)

            binding.loading.visibility = if (it)
                View.VISIBLE
            else
                View.GONE
        }
    }
}