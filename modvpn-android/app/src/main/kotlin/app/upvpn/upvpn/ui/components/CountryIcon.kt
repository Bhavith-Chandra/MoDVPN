package app.modvpn.modvpn.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.modvpn.modvpn.R
import app.modvpn.modvpn.data.CountryMap

@Composable
fun CountryIcon(countryCode: String, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(
            id = CountryMap.resource.getOrDefault(
                countryCode.lowercase(),
                R.drawable.modvpn
            )
        ),
        contentDescription = countryCode,
        tint = Color.Unspecified,
        modifier = modifier
            .clip(shape = RoundedCornerShape(2.dp))
    )
}
