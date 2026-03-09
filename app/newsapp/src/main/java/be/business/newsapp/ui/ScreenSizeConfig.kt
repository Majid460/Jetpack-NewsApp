package be.business.newsapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Screen size categories based on Material Design breakpoints
 */
enum class ScreenSize {
    COMPACT,        // < 600dp (phones in portrait)
    MEDIUM,         // 600-840dp (tablets in portrait, phones in landscape)
    EXPANDED,       // > 840dp (tablets in landscape, desktops)

    // Granular phone categories
    SMALL_PHONE,    // < 360dp
    MEDIUM_PHONE,   // 360-400dp
    LARGE_PHONE     // 400-600dp
}

/**
 * Window size class for responsive design
 */
data class WindowSize(
    val width: Dp,
    val height: Dp,
    val screenSize: ScreenSize,
    val isTablet: Boolean,
    val isLandscape: Boolean
)

/**
 * Responsive dimensions configuration
 */
data class ResponsiveDimensions(
    // Padding
    val screenHorizontalPadding: Dp,
    val screenVerticalPadding: Dp,
    val contentPadding: Dp,
    val smallPadding: Dp,
    val mediumPadding: Dp,
    val largePadding: Dp,

    // Bottom Navigation
    val bottomBarHeight: Dp,
    val bottomBarHorizontalPadding: Dp,
    val bottomBarBottomPadding: Dp,
    val bottomBarCornerRadius: Dp,
    val bottomBarMaxWidth: Dp,

    // Cards & Components
    val cardCornerRadius: Dp,
    val cardElevation: Dp,
    val buttonHeight: Dp,
    val iconSize: Dp,
    val smallIconSize: Dp,
    val largeIconSize: Dp,

    // Typography Scale
    val headlineLarge: Dp,
    val headlineMedium: Dp,
    val bodyLarge: Dp,
    val bodyMedium: Dp,

    // Columns & Grid
    val gridColumns: Int,
    val cardWidth: Dp,
    val imageHeight: Dp
)

/**
 * Singleton object for screen size configuration
 */
object ScreenSizeConfig {

    /**
     * Get current window size information
     */
    @Composable
    fun rememberWindowSize(): WindowSize {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current

        return remember(configuration) {
            val width = configuration.screenWidthDp.dp
            val height = configuration.screenHeightDp.dp
            val isLandscape = width > height

            val screenSize = when {
                width < 360.dp -> ScreenSize.SMALL_PHONE
                width < 400.dp -> ScreenSize.MEDIUM_PHONE
                width < 600.dp -> ScreenSize.LARGE_PHONE
                width < 840.dp -> ScreenSize.MEDIUM
                else -> ScreenSize.EXPANDED
            }

            val isTablet = width >= 600.dp

            WindowSize(
                width = width,
                height = height,
                screenSize = screenSize,
                isTablet = isTablet,
                isLandscape = isLandscape
            )
        }
    }

    /**
     * Get responsive dimensions based on screen size
     */
    @Composable
    fun getDimensions(windowSize: WindowSize = rememberWindowSize()): ResponsiveDimensions {
        return when (windowSize.screenSize) {
            ScreenSize.SMALL_PHONE -> getSmallPhoneDimensions()
            ScreenSize.MEDIUM_PHONE -> getMediumPhoneDimensions()
            ScreenSize.LARGE_PHONE -> getLargePhoneDimensions()
            ScreenSize.MEDIUM -> getTabletPortraitDimensions()
            ScreenSize.EXPANDED -> getTabletLandscapeDimensions()
            else -> getLargePhoneDimensions()
        }
    }

    /**
     * Small Phone Dimensions (< 360dp)
     */
    private fun getSmallPhoneDimensions() = ResponsiveDimensions(
        screenHorizontalPadding = 12.dp,
        screenVerticalPadding = 12.dp,
        contentPadding = 12.dp,
        smallPadding = 4.dp,
        mediumPadding = 8.dp,
        largePadding = 12.dp,

        bottomBarHeight = 64.dp,
        bottomBarHorizontalPadding = 16.dp,
        bottomBarBottomPadding = 8.dp,
        bottomBarCornerRadius = 20.dp,
        bottomBarMaxWidth = 600.dp,

        cardCornerRadius = 12.dp,
        cardElevation = 2.dp,
        buttonHeight = 44.dp,
        iconSize = 20.dp,
        smallIconSize = 16.dp,
        largeIconSize = 28.dp,

        headlineLarge = 24.dp,
        headlineMedium = 20.dp,
        bodyLarge = 16.dp,
        bodyMedium = 14.dp,

        gridColumns = 1,
        cardWidth = 320.dp,
        imageHeight = 180.dp
    )

    /**
     * Medium Phone Dimensions (360-400dp)
     */
    private fun getMediumPhoneDimensions() = ResponsiveDimensions(
        screenHorizontalPadding = 16.dp,
        screenVerticalPadding = 16.dp,
        contentPadding = 16.dp,
        smallPadding = 6.dp,
        mediumPadding = 12.dp,
        largePadding = 16.dp,

        bottomBarHeight = 68.dp,
        bottomBarHorizontalPadding = 24.dp,
        bottomBarBottomPadding = 10.dp,
        bottomBarCornerRadius = 22.dp,
        bottomBarMaxWidth = 600.dp,

        cardCornerRadius = 14.dp,
        cardElevation = 3.dp,
        buttonHeight = 48.dp,
        iconSize = 22.dp,
        smallIconSize = 18.dp,
        largeIconSize = 32.dp,

        headlineLarge = 28.dp,
        headlineMedium = 22.dp,
        bodyLarge = 16.dp,
        bodyMedium = 14.dp,

        gridColumns = 1,
        cardWidth = 340.dp,
        imageHeight = 280.dp
    )

    /**
     * Large Phone Dimensions (400-600dp)
     */
    private fun getLargePhoneDimensions() = ResponsiveDimensions(
        screenHorizontalPadding = 20.dp,
        screenVerticalPadding = 20.dp,
        contentPadding = 20.dp,
        smallPadding = 8.dp,
        mediumPadding = 16.dp,
        largePadding = 24.dp,

        bottomBarHeight = 72.dp,
        bottomBarHorizontalPadding = 32.dp,
        bottomBarBottomPadding = 12.dp,
        bottomBarCornerRadius = 24.dp,
        bottomBarMaxWidth = 600.dp,

        cardCornerRadius = 16.dp,
        cardElevation = 4.dp,
        buttonHeight = 52.dp,
        iconSize = 24.dp,
        smallIconSize = 20.dp,
        largeIconSize = 36.dp,

        headlineLarge = 32.dp,
        headlineMedium = 24.dp,
        bodyLarge = 18.dp,
        bodyMedium = 16.dp,

        gridColumns = 2,
        cardWidth = 360.dp,
        imageHeight = 280.dp
    )

    /**
     * Tablet Portrait Dimensions (600-840dp)
     */
    private fun getTabletPortraitDimensions() = ResponsiveDimensions(
        screenHorizontalPadding = 32.dp,
        screenVerticalPadding = 24.dp,
        contentPadding = 24.dp,
        smallPadding = 12.dp,
        mediumPadding = 20.dp,
        largePadding = 32.dp,

        bottomBarHeight = 76.dp,
        bottomBarHorizontalPadding = 40.dp,
        bottomBarBottomPadding = 14.dp,
        bottomBarCornerRadius = 26.dp,
        bottomBarMaxWidth = 700.dp,

        cardCornerRadius = 18.dp,
        cardElevation = 6.dp,
        buttonHeight = 56.dp,
        iconSize = 28.dp,
        smallIconSize = 22.dp,
        largeIconSize = 40.dp,

        headlineLarge = 36.dp,
        headlineMedium = 28.dp,
        bodyLarge = 20.dp,
        bodyMedium = 18.dp,

        gridColumns = 2,
        cardWidth = 280.dp,
        imageHeight = 300.dp
    )

    /**
     * Tablet Landscape Dimensions (> 840dp)
     */
    private fun getTabletLandscapeDimensions() = ResponsiveDimensions(
        screenHorizontalPadding = 48.dp,
        screenVerticalPadding = 32.dp,
        contentPadding = 32.dp,
        smallPadding = 16.dp,
        mediumPadding = 24.dp,
        largePadding = 40.dp,

        bottomBarHeight = 80.dp,
        bottomBarHorizontalPadding = 48.dp,
        bottomBarBottomPadding = 16.dp,
        bottomBarCornerRadius = 28.dp,
        bottomBarMaxWidth = 800.dp,

        cardCornerRadius = 20.dp,
        cardElevation = 8.dp,
        buttonHeight = 60.dp,
        iconSize = 32.dp,
        smallIconSize = 24.dp,
        largeIconSize = 48.dp,

        headlineLarge = 40.dp,
        headlineMedium = 32.dp,
        bodyLarge = 22.dp,
        bodyMedium = 20.dp,

        gridColumns = 3,
        cardWidth = 300.dp,
        imageHeight = 300.dp
    )
}

/**
 * Composable helper to get dimensions directly
 */
@Composable
fun rememberResponsiveDimensions(): ResponsiveDimensions {
    val windowSize = ScreenSizeConfig.rememberWindowSize()
    return ScreenSizeConfig.getDimensions(windowSize)
}

/**
 * Extension functions for common checks
 */
@Composable
fun isSmallPhone(): Boolean {
    return ScreenSizeConfig.rememberWindowSize().screenSize == ScreenSize.SMALL_PHONE
}

@Composable
fun isTablet(): Boolean {
    return ScreenSizeConfig.rememberWindowSize().isTablet
}

@Composable
fun isLandscape(): Boolean {
    return ScreenSizeConfig.rememberWindowSize().isLandscape
}