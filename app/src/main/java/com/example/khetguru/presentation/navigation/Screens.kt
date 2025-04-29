package com.example.khetguru.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object StartScreen : Screens()

    @Serializable
    data object WalkthroughScreen : Screens()

    @Serializable
    data object HomeScreen : Screens()

    @Serializable
    data object WeatherScreen : Screens()

    @Serializable
    data object SoilHealthScreen : Screens()

    @Serializable
    data object DiseaseIdentificationScreen : Screens()

    @Serializable
    data object MarketPricesScreen : Screens()

    @Serializable
    data object ExpertAdvisoryScreen: Screens()

    @Serializable
    data object GovtSchemesScreen: Screens()

    @Serializable
    data object SavedPricesScreen: Screens()

    @Serializable
    data object CropPredictionScreen : Screens()
}