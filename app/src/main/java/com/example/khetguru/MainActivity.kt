package com.example.khetguru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.khetguru.domain.utils.GoogleAuthUiClient
import com.example.khetguru.presentation.navigation.NavGraph
import com.example.khetguru.presentation.viewmodel.AuthViewModel
import com.example.khetguru.presentation.viewmodel.CropRecommendationViewModel
import com.example.khetguru.presentation.viewmodel.CropViewModel
import com.example.khetguru.presentation.viewmodel.DiseaseViewModel
import com.example.khetguru.presentation.viewmodel.MarketPriceViewModel
import com.example.khetguru.presentation.viewmodel.WeatherViewModel
import com.example.khetguru.ui.theme.KhetGuruTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            viewModel = authViewModel,
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val weatherViewModel: WeatherViewModel = hiltViewModel()
            val marketPriceViewModel: MarketPriceViewModel = hiltViewModel()
            val cropViewModel: CropViewModel = hiltViewModel()
            val cropRecommendationViewModel: CropRecommendationViewModel = hiltViewModel()
            val diseaseViewModel: DiseaseViewModel = hiltViewModel()
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult =
                                googleAuthUiClient.signInWithIntent(
                                    result.data ?: return@launch
                                )
                            authViewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )
            KhetGuruTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        navController = navController,
                        authViewModel = authViewModel,
                        googleAuthUiClient = googleAuthUiClient,
                        weatherViewModel = weatherViewModel,
                        marketPriceViewModel = marketPriceViewModel,
                        cropViewModel = cropViewModel,
                        cropRecommendationViewModel = cropRecommendationViewModel,
                        diseaseViewModel = diseaseViewModel,
                    ) {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            signInIntentSender?.let {
                                launcher.launch(
                                    IntentSenderRequest.Builder(it).build()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
