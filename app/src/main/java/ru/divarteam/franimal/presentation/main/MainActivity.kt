package ru.divarteam.franimal.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.ActivityMainBinding
import ru.divarteam.franimal.domain.usecase.main.IsUserAuthorizedUseCase
import ru.divarteam.franimal.presentation.auth.AuthActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var isUserAuthorizedUseCase: IsUserAuthorizedUseCase

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        if (isUserAuthorizedUseCase().not()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}