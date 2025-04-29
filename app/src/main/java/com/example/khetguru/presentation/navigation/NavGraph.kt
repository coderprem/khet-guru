package com.example.khetguru.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.khetguru.domain.utils.GoogleAuthUiClient
import com.example.khetguru.presentation.screens.DiseaseIdentificationScreen
import com.example.khetguru.presentation.screens.ExpertAdvisoryScreen
import com.example.khetguru.presentation.screens.GovtSchemesScreen
import com.example.khetguru.presentation.screens.HomeScreen
import com.example.khetguru.presentation.screens.MarketPricesScreen
import com.example.khetguru.presentation.screens.SavedPricesScreen
import com.example.khetguru.presentation.screens.SoilHealthScreen
import com.example.khetguru.presentation.screens.WalkthroughScreen
import com.example.khetguru.presentation.screens.WeatherScreen
import com.example.khetguru.presentation.viewmodel.AuthViewModel
import com.example.khetguru.presentation.viewmodel.CropRecommendationViewModel
import com.example.khetguru.presentation.viewmodel.CropViewModel
import com.example.khetguru.presentation.viewmodel.DiseaseViewModel
import com.example.khetguru.presentation.viewmodel.MarketPriceViewModel
import com.example.khetguru.presentation.viewmodel.WeatherViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    marketPriceViewModel: MarketPriceViewModel,
    googleAuthUiClient: GoogleAuthUiClient,
    weatherViewModel: WeatherViewModel,
    cropRecommendationViewModel: CropRecommendationViewModel,
    diseaseViewModel: DiseaseViewModel,
    cropViewModel: CropViewModel,
    onSignInSuccess: () -> Unit = {},
) {
    val state = authViewModel.state.collectAsState()
    NavHost(
        navController = navController,
        startDestination = Screens.StartScreen
    ) {
        composable<Screens.StartScreen> {
            LaunchedEffect(key1 = Unit) {
                val userData = googleAuthUiClient.getSignedInUserData()
                if (userData != null) {
                    authViewModel.getUserData(userData.userId.toString())
                    navController.navigate(Screens.HomeScreen) {
                        popUpTo(Screens.WalkthroughScreen) { inclusive = true } // Clear back stack
                    }
                } else {
                    navController.navigate(Screens.WalkthroughScreen) {
                        popUpTo(Screens.WalkthroughScreen) { inclusive = true } // Clear back stack
                    }
                }
            }
        }

        composable<Screens.WalkthroughScreen> {
            LaunchedEffect(key1 = state.value.isSignedIn) {
                if (state.value.isSignedIn) {
                    val userData = googleAuthUiClient.getSignedInUserData()
                    userData?.run {
                        authViewModel.addUserToFireStore(userData)
                        authViewModel.getUserData(userData.userId.toString())
                        navController.navigate(Screens.HomeScreen) {
                            popUpTo(Screens.WalkthroughScreen) {
                                inclusive = true
                            } // Clear back stack
                        }
                    }
                }
            }
            WalkthroughScreen() {
                onSignInSuccess()
            }
        }

        composable<Screens.HomeScreen> {
            HomeScreen(
                modifier = modifier,
                weatherViewModel = weatherViewModel,
                navigateToWeather = { navController.navigate(Screens.WeatherScreen) },
                navigateToSoilHealth = { navController.navigate(Screens.SoilHealthScreen) },
                navigateToMarketPrices = { navController.navigate(Screens.MarketPricesScreen) },
                navigateToDiseaseIdentification = { navController.navigate(Screens.DiseaseIdentificationScreen) },
                navigateToExpertAdvisory = { navController.navigate(Screens.ExpertAdvisoryScreen) },
                navigateToGovtSchemes = { navController.navigate(Screens.GovtSchemesScreen) }
            )
        }

        composable<Screens.WeatherScreen> {
            WeatherScreen(
                modifier = modifier,
                weatherViewModel = weatherViewModel
            )
        }

        composable<Screens.SoilHealthScreen> {
            SoilHealthScreen(cropRecommendationViewModel = cropRecommendationViewModel)
        }

        composable<Screens.DiseaseIdentificationScreen> {
            DiseaseIdentificationScreen(modifier = modifier, diseaseViewModel = diseaseViewModel)
        }

        composable<Screens.MarketPricesScreen> {
            MarketPricesScreen(
                modifier = modifier,
                marketPriceViewModel = marketPriceViewModel,
                cropViewModel = cropViewModel
            ) {
                navController.navigate(Screens.SavedPricesScreen)
            }
        }

        composable<Screens.ExpertAdvisoryScreen> {
            ExpertAdvisoryScreen(modifier = modifier)
        }

        composable<Screens.GovtSchemesScreen> {
            GovtSchemesScreen(modifier = modifier)
        }

        composable<Screens.SavedPricesScreen> {
            SavedPricesScreen(modifier = modifier, cropViewModel = cropViewModel) {
                navController.popBackStack()
            }
        }
    }
}