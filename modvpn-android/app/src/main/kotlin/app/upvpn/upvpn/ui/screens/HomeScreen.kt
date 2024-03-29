package app.modvpn.modvpn.ui.screens

import android.app.Activity
import android.content.res.Configuration
import android.net.VpnService
import android.os.SystemClock
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.layout.DisplayFeature
import app.modvpn.modvpn.R
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.model.random
import app.modvpn.modvpn.ui.VPNScreen
import app.modvpn.modvpn.ui.components.AllLocations
import app.modvpn.modvpn.ui.components.LocationComponent
import app.modvpn.modvpn.ui.components.LocationSelector
import app.modvpn.modvpn.ui.components.VPNLayout
import app.modvpn.modvpn.ui.state.HomeUiState
import app.modvpn.modvpn.ui.state.LocationState
import app.modvpn.modvpn.ui.state.VpnUiState
import app.modvpn.modvpn.ui.state.isVpnSessionActivityInProgress
import app.modvpn.modvpn.ui.state.progress
import app.modvpn.modvpn.ui.state.shieldResourceId
import app.modvpn.modvpn.ui.state.switchChecked
import app.modvpn.modvpn.ui.state.switchEnabled
import app.modvpn.modvpn.ui.state.vpnDisplayText
import app.modvpn.modvpn.ui.theme.modvpnTheme
import app.modvpn.modvpn.util.locationForPreview
import app.modvpn.modvpn.util.msTimerString
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    val locations = listOf<Location>().random(5)
    VPNLayout(windowSize = WindowSizeClass.calculateFromSize(DpSize(300.dp, 720.dp)),
        currentVPNScreen = VPNScreen.Home,
        onNavItemPressed = {}) {
        HomeScreen(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(300.dp, 720.dp)),
            displayFeatures = listOf(),
            selectedLocation = locationForPreview(),
            locationState = LocationState.Loading,
            uiState =
            HomeUiState(),
            recentLocations = locations,
            connectPreVpnPermission = {},
            connectPostVpnPermission = { _, _ -> {} },
            disconnect = {},
            openLocationScreen = {},
            reloadLocations = {},
            isSelectedLocation = { it == locations.first() },
            onLocationSelected = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = Devices.DEFAULT, widthDp = 300, heightDp = 600)
@Composable
fun PreviewSmallHomeScreen() {
    val locations = listOf<Location>().random(5)
    VPNLayout(windowSize = WindowSizeClass.calculateFromSize(DpSize(240.dp, 400.dp)),
        currentVPNScreen = VPNScreen.Home,
        onNavItemPressed = {}) {
        HomeScreen(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(240.dp, 400.dp)),
            displayFeatures = listOf(),
            selectedLocation = locationForPreview(),
            locationState = LocationState.Loading,
            uiState =
            HomeUiState(),
            recentLocations = locations,
            connectPreVpnPermission = {},
            connectPostVpnPermission = { _, _ -> {} },
            disconnect = {},
            openLocationScreen = {},
            reloadLocations = {},
            isSelectedLocation = { it == locations.first() },
            onLocationSelected = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 360)
@Composable
fun PreviewHomeScreenCompactWidthMediumHeight() {
    val locations = listOf<Location>().random(5)
    VPNLayout(windowSize = WindowSizeClass.calculateFromSize(DpSize(720.dp, 320.dp)),
        currentVPNScreen = VPNScreen.Home,
        onNavItemPressed = {}) {
        HomeScreen(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(720.dp, 320.dp)),
            displayFeatures = listOf(),
            selectedLocation = locationForPreview(),
            locationState = LocationState.Loading,
            uiState =
            HomeUiState(),
            recentLocations = locations,
            connectPreVpnPermission = {},
            connectPostVpnPermission = { _, _ -> {} },
            disconnect = {},
            openLocationScreen = {},
            reloadLocations = {},
            isSelectedLocation = { it == locations.first() },
            onLocationSelected = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(device = Devices.TABLET)
@Composable
fun PreviewHomeScreenMedium() {
    modvpnTheme(darkTheme = false) {
        val locations = listOf<Location>().random(5)
        VPNLayout(windowSize = WindowSizeClass.calculateFromSize(DpSize(720.dp, 720.dp)),
            currentVPNScreen = VPNScreen.Home,
            onNavItemPressed = {}) {
            HomeScreen(
                windowSize = WindowSizeClass.calculateFromSize(DpSize(720.dp, 720.dp)),
                displayFeatures = listOf(),
                selectedLocation = locationForPreview(),
                locationState = LocationState.Locations(locations),
                uiState =
                HomeUiState(),
                recentLocations = listOf<Location>().random(5),
                connectPreVpnPermission = {},
                connectPostVpnPermission = { _, _ -> {} },
                disconnect = {},
                openLocationScreen = {},
                reloadLocations = {},
                isSelectedLocation = { it == locations.first() },
                onLocationSelected = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showSystemUi = true, device = Devices.TV_1080p)
@Composable
fun PreviewHomeScreenLarge() {
    val locations = listOf<Location>().random(5)
    VPNLayout(windowSize = WindowSizeClass.calculateFromSize(DpSize(1080.dp, 1024.dp)),
        currentVPNScreen = VPNScreen.Home,
        onNavItemPressed = {}) {
        HomeScreen(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(1080.dp, 1024.dp)),
            displayFeatures = listOf(),
            selectedLocation = locationForPreview(),
            locationState = LocationState.Locations(locations),
            uiState =
            HomeUiState(
                vpnUiState = VpnUiState.Connected(
                    locations.first(),
                    SystemClock.currentThreadTimeMillis()
                )
            ),
            recentLocations = listOf<Location>().random(5),
            connectPreVpnPermission = {},
            connectPostVpnPermission = { _, _ -> {} },
            disconnect = {},
            openLocationScreen = {},
            reloadLocations = {},
            isSelectedLocation = { it == locations.first() },
            onLocationSelected = {}
        )
    }
}

@Composable
fun HomeCardAndRecentRow(
    homeCardModifier: Modifier,
    recentLocationsModifier: Modifier,
    selectedLocation: Location?,
    locationState: LocationState,
    uiState: HomeUiState,
    recentLocations: List<Location>,
    connectPreVpnPermission: (Location?) -> Unit,
    connectPostVpnPermission: (Boolean, Location?) -> Unit,
    disconnect: () -> Unit,
    openLocationScreen: () -> Unit,
    reloadLocations: () -> Unit,
    isSelectedLocation: (Location) -> Boolean,
    onLocationSelected: (Location) -> Unit,
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            HomeCard(
                selectedLocation,
                locationState,
                uiState.vpnUiState,
                connectPreVpnPermission,
                connectPostVpnPermission,
                disconnect,
                openLocationScreen,
                reloadLocations,
                homeCardModifier.weight(1f)
            )
            RecentLocationsCard(
                uiState.vpnUiState.isVpnSessionActivityInProgress(),
                recentLocations,
                isSelectedLocation,
                onLocationSelected,
                recentLocationsModifier.weight(1f)
            )
        }
    }
}

@Composable
fun HomeCardAndRecentColumn(
    windowSize: WindowSizeClass,
    homeCardModifier: Modifier,
    recentLocationsModifier: Modifier,
    selectedLocation: Location?,
    locationState: LocationState,
    uiState: HomeUiState,
    recentLocations: List<Location>,
    connectPreVpnPermission: (Location?) -> Unit,
    connectPostVpnPermission: (Boolean, Location?) -> Unit,
    disconnect: () -> Unit,
    openLocationScreen: () -> Unit,
    reloadLocations: () -> Unit,
    isSelectedLocation: (Location) -> Boolean,
    onLocationSelected: (Location) -> Unit,
) {
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            HomeCard(
                selectedLocation,
                locationState,
                uiState.vpnUiState,
                connectPreVpnPermission,
                connectPostVpnPermission,
                disconnect,
                openLocationScreen,
                reloadLocations,
                homeCardModifier.weight(0.65f)
            )
            // on large/long screen if height is compact dont show recent card
            if (windowSize.heightSizeClass != WindowHeightSizeClass.Compact) {
                RecentLocationsCard(
                    uiState.vpnUiState.isVpnSessionActivityInProgress(),
                    recentLocations, isSelectedLocation, onLocationSelected,
                    recentLocationsModifier.weight(0.35f)
                )
            } else {
                // still have spacedBy work for one home card from bottom of screen
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}

@Composable
fun HomeScreen(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    selectedLocation: Location?,
    locationState: LocationState,
    uiState: HomeUiState,
    recentLocations: List<Location>,
    connectPreVpnPermission: (Location?) -> Unit,
    connectPostVpnPermission: (Boolean, Location?) -> Unit,
    disconnect: () -> Unit,
    openLocationScreen: () -> Unit,
    reloadLocations: () -> Unit,
    isSelectedLocation: (Location) -> Boolean,
    onLocationSelected: (Location) -> Unit,
) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    val homeCardModifier = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)
        WindowWidthSizeClass.Medium -> Modifier.padding(15.dp, 15.dp, 0.dp, 15.dp)
        else -> if (isPortrait.not()) {
            Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)
        } else {
            Modifier.padding(15.dp, 15.dp, 0.dp, 15.dp)
        }
    }

    val recentLocationsModifier = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> Modifier.padding(15.dp, 0.dp, 15.dp, 15.dp)
        WindowWidthSizeClass.Medium -> Modifier.padding(0.dp, 15.dp, 15.dp, 15.dp)
        else -> if (isPortrait.not()) {
            Modifier.padding(15.dp, 0.dp, 15.dp, 15.dp)
        } else {
            Modifier.padding(0.dp, 15.dp, 15.dp, 15.dp)
        }
    }

    val allLocationsModifier =
        if (windowSize.widthSizeClass == WindowWidthSizeClass.Medium || isPortrait) {
            Modifier.padding(15.dp, 0.dp, 15.dp, 15.dp)
        } else {
            Modifier.padding(0.dp, 15.dp, 15.dp, 15.dp)
        }

    TwoPane(
        first = {
            when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Compact -> HomeCard(
                    selectedLocation,
                    locationState,
                    uiState.vpnUiState,
                    connectPreVpnPermission,
                    connectPostVpnPermission,
                    disconnect,
                    openLocationScreen,
                    reloadLocations,
                    homeCardModifier
                )

                WindowWidthSizeClass.Medium -> HomeCardAndRecentRow(
                    homeCardModifier,
                    recentLocationsModifier,
                    selectedLocation,
                    locationState,
                    uiState,
                    recentLocations,
                    connectPreVpnPermission,
                    connectPostVpnPermission,
                    disconnect,
                    openLocationScreen,
                    reloadLocations,
                    isSelectedLocation,
                    onLocationSelected
                )

                else ->
                    if (isPortrait.not()) {
                        HomeCardAndRecentColumn(
                            windowSize,
                            homeCardModifier,
                            recentLocationsModifier,
                            selectedLocation,
                            locationState,
                            uiState,
                            recentLocations,
                            connectPreVpnPermission,
                            connectPostVpnPermission,
                            disconnect,
                            openLocationScreen,
                            reloadLocations,
                            isSelectedLocation,
                            onLocationSelected
                        )
                    } else {
                        // when both w & h are expanded, but table is in portrait mode
                        // avoid showing thin layer of home card + recent in column
                        HomeCardAndRecentRow(
                            homeCardModifier,
                            recentLocationsModifier,
                            selectedLocation,
                            locationState,
                            uiState,
                            recentLocations,
                            connectPreVpnPermission,
                            connectPostVpnPermission,
                            disconnect,
                            openLocationScreen,
                            reloadLocations,
                            isSelectedLocation,
                            onLocationSelected
                        )
                    }

            }
        },
        second = {
            when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Compact -> RecentLocationsCard(
                    uiState.vpnUiState.isVpnSessionActivityInProgress(),
                    recentLocations, isSelectedLocation, onLocationSelected,
                    recentLocationsModifier
                )

                WindowWidthSizeClass.Medium -> AllLocationsCard(
                    uiState.vpnUiState.isVpnSessionActivityInProgress(),
                    locationState,
                    reloadLocations,
                    isSelectedLocation,
                    onLocationSelected,
                    modifier = allLocationsModifier
                )

                else -> AllLocationsCard(
                    uiState.vpnUiState.isVpnSessionActivityInProgress(),
                    locationState,
                    reloadLocations,
                    isSelectedLocation,
                    onLocationSelected,
                    modifier = allLocationsModifier
                )
            }
        },
        strategy = when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                if (windowSize.heightSizeClass == WindowHeightSizeClass.Compact) {
                    // both width and height are small do not display recent card
                    // gap for bottom of the screen
                    VerticalTwoPaneStrategy(1f, 25.dp)
                } else {
                    VerticalTwoPaneStrategy(0.65f, 15.dp)
                }
            }

            WindowWidthSizeClass.Medium -> VerticalTwoPaneStrategy(
                if (windowSize.heightSizeClass == WindowHeightSizeClass.Compact) {
                    1f
                } else if (windowSize.heightSizeClass == WindowHeightSizeClass.Medium) {
                    0.5f
                } else {
                    0.4f
                }
            )

            else -> {
                if (isPortrait.not()) {
                    HorizontalTwoPaneStrategy(0.38f)
                } else {
                    // when both w & h are expanded, but table is in portrait mode
                    // avoid showing thin layer of home card + recent in column
                    VerticalTwoPaneStrategy(0.4f)
                }
            }
        },
        displayFeatures = displayFeatures
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCardDivider(vpnUiState: VpnUiState) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        when (vpnUiState) {
            is VpnUiState.Checking,
            is VpnUiState.Disconnecting,
            is VpnUiState.Disconnected -> Divider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
            )

            is VpnUiState.Requesting,
            is VpnUiState.Accepted,
            is VpnUiState.ServerCreated,
            is VpnUiState.ServerRunning,
            is VpnUiState.ServerReady,
            is VpnUiState.Connecting -> LinearProgressIndicator(
                progress = vpnUiState.progress(),
                trackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 35.dp)
            )

            is VpnUiState.Connected -> {
                var elapsedTimeMs by remember {
                    mutableLongStateOf(SystemClock.elapsedRealtime() - vpnUiState.time)
                }
                LaunchedEffect(key1 = vpnUiState.time) {
                    while (true) {
                        delay(1000)
                        elapsedTimeMs = SystemClock.elapsedRealtime() - vpnUiState.time
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                ) {

                    Divider(thickness = 2.dp, modifier = Modifier.weight(1f))

                    SuggestionChip(
                        onClick = { },
                        label = { Text(text = elapsedTimeMs.msTimerString(), fontSize = 13.sp) },
                        shape = RoundedCornerShape(15.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )

                    Divider(thickness = 2.dp, modifier = Modifier.weight(1f))
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCard(
    selectedLocation: Location?,
    locationState: LocationState,
    vpnUiState: VpnUiState,
    connectPreVpnPermission: (Location?) -> Unit,
    connectPostVpnPermission: (Boolean, Location?) -> Unit,
    disconnect: () -> Unit,
    openLocationScreen: () -> Unit,
    reloadLocations: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            connectPostVpnPermission(it.resultCode == Activity.RESULT_OK, selectedLocation)
        })

    val onCheckedChange: (Boolean) -> Unit = when (vpnUiState.switchChecked()) {
        false -> {
            {
                connectPreVpnPermission(selectedLocation)
                val intent = VpnService.prepare(context)
                if (intent == null) {
                    // already have permission
                    connectPostVpnPermission(true, selectedLocation)
                } else {
                    vpnPermissionLauncher.launch(intent)
                }
            }
        }

        else -> {
            {
                disconnect()
            }
        }
    }

    Card(
        modifier = modifier
            .heightIn(400.dp, 600.dp)
            .widthIn(400.dp, 600.dp)
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = vpnUiState.shieldResourceId()),
                    contentDescription = "Shield",
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .aspectRatio(1f)
                )
                SuggestionChip(
                    onClick = { },
                    label = { Text(text = vpnUiState.vpnDisplayText(), fontSize = 13.sp) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxHeight(0.1f)
                )
                HomeCardDivider(vpnUiState)
                LocationSelector(
                    selectedLocation = selectedLocation,
                    locationState = locationState,
                    vpnUiState = vpnUiState,
                    openLocationScreen = openLocationScreen,
                    reloadLocations = reloadLocations
                )
                Switch(
                    enabled = vpnUiState.switchEnabled(),
                    checked = vpnUiState.switchChecked(),
                    onCheckedChange = onCheckedChange
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentLocationsCard(
    isVpnSessionActivityInProgress: Boolean,
    recentLocations: List<Location>, isSelectedLocation: (Location) -> Boolean,
    onLocationSelected: (Location) -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(400.dp, 600.dp)
            .widthIn(400.dp, 600.dp)
            .fillMaxSize()
    ) {
        if (recentLocations.isEmpty()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Image(
                    painterResource(R.drawable.modvpn),
                    contentDescription = "modvpn Logo",
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(10.dp, 15.dp, 10.dp, 10.dp)
            ) {
                Text(
                    text = "Recent Locations",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(12.dp, 0.dp, 0.dp, 0.dp)
                )
                LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(recentLocations.size) {
                        LocationComponent(
                            isVpnSessionActivityInProgress = isVpnSessionActivityInProgress,
                            location = recentLocations[it],
                            isSelectedLocation = isSelectedLocation,
                            onLocationSelected = onLocationSelected,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllLocationsCard(
    isVpnSessionActivityInProgress: Boolean,
    locationState: LocationState,
    onRefresh: () -> Unit,
    isSelectedLocation: (Location) -> Boolean,
    onLocationSelected: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            AllLocations(
                isVpnSessionActivityInProgress = isVpnSessionActivityInProgress,
                locationState = locationState,
                verticalCountrySpacing = 0.dp,
                onRefresh = onRefresh,
                isSelectedLocation = isSelectedLocation,
                onLocationSelected = onLocationSelected
            )
        }
    }
}
