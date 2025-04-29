package com.example.khetguru.presentation.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.khetguru.R
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun WalkthroughScreen(
    onSignInClick: () -> Unit = {}
) {
    val pages = listOf(
        WalkthroughPageData(
            title = "Welcome to KhetGuru",
            description = "We provide intelligent crop recommendations to optimize your farming experience.",
            imageRes = R.drawable.farm1
        ),
        WalkthroughPageData(
            title = "Analyze Soil Health",
            description = "Use our soil health analyzer to improve your farm's productivity.",
            imageRes = R.drawable.farm2
        ),
        WalkthroughPageData(
            title = "Get Crop Recommendations",
            description = "Our app helps you choose the best crops based on your soil and climate.",
            imageRes = R.drawable.farm1
        )
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        pages.size
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        // Top Skip Button
        if (pagerState.currentPage < pages.lastIndex) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { scope.launch { pagerState.animateScrollToPage(pages.lastIndex) } }
                ) {
                    Text(
                        "Skip",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Pager with smooth transitions
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            key = { pages[it].hashCode() } // Add key for better recomposition
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

            WalkthroughPage(
                title = pages[page].title,
                description = pages[page].description,
                imageRes = pages[page].imageRes,
                modifier = Modifier
                    .graphicsLayer {
                        // Add smooth transition effects
                        val transformation = lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                        )
                        alpha = transformation
                        scaleX = transformation
                        scaleY = transformation
                    }
            )
        }

        // Page Indicators
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(horizontal = 4.dp)
                        .background(
                            color = if (pagerState.currentPage == index) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.LightGray.copy(alpha = 0.5f)
                            },
                            shape = CircleShape
                        )
                )
            }
        }

        // Button with smooth state changes
        Button(
            onClick = {
                if (pagerState.currentPage < pages.lastIndex) {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(300)
                        )
                    }
                } else {
                    onSignInClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 16.dp)
            ,
            shape = CircleShape
        ) {
            if (pagerState.currentPage < pages.lastIndex) {
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google logo",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Continue with Google",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun WalkthroughPage(
    title: String,
    description: String,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 32.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

data class WalkthroughPageData(
    val title: String,
    val description: String,
    val imageRes: Int
)