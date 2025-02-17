package cz.frank.rickandmorty.utils.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.frank.rickandmorty.R

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(Space.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(shape = CircleShape) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(Space.medium),
                )
            }

            Spacer(modifier = Modifier.height(Space.xxlarge))

            Text(
                text = stringResource(R.string.error_screen_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Space.small))

            Text(
                text = stringResource(R.string.error_screen_support_message),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Space.xlarge)
            )

            Spacer(modifier = Modifier.height(Space.large))

            Button(onClick = onRetry, shape = MaterialTheme.shapes.medium) {
                Text(text = stringResource(R.string.error_screen_try_again))
            }
        }
    }
}
