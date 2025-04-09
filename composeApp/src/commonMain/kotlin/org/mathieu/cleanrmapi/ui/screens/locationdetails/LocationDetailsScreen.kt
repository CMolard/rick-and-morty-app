package org.mathieu.cleanrmapi.ui.screens.locationdetails

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SquareFoot
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.ui.core.composables.BackArrow
import org.mathieu.cleanrmapi.ui.core.composables.IconWithImage
import org.mathieu.cleanrmapi.ui.core.composables.Screen
import org.mathieu.cleanrmapi.ui.core.theme.PrimaryColor
import org.mathieu.cleanrmapi.ui.core.theme.SurfaceColor

@Composable
fun LocationDetailsScreen(
    navController: NavController,
    locationId: Int,
) {
    Screen(
        viewModel = LocationDetailsViewModel(),
        navController = navController,
    ) {state, viewModel ->

        viewModel.init(locationId)

        // Render the content of the screen
        Content(
            state = state,
            onAction = viewModel::handleAction,
            onClickBack = navController::popBackStack
        )
    }
}

@Composable
private fun Content(
    state: LocationDetailsState,
    onAction: (LocationDetailsAction) -> Unit = { },
    onClickBack: () -> Unit = { }
)
{
    Box(
        modifier = Modifier
        .fillMaxSize()
        .padding(),
        contentAlignment = Alignment.Center
    ) {
        BackArrow(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f),
            onClick = onClickBack
        )

        Crossfade(targetState = state) {
            when (it) {
                is LocationDetailsState.Error -> ErrorView(error = it.message)
                is LocationDetailsState.Loaded -> LocationDetailsContent(
                    state = it,
                    onAction = onAction
                )
                LocationDetailsState.Loading -> {
                    /** TODO: Could display a Loading Animation */
                }
            }
        }
    }
}

@Composable
private fun ErrorView(error: String) {
    Text(
        modifier = Modifier.padding(16.dp),
        text = error,
        textAlign = TextAlign.Center,
        color = PrimaryColor,
        fontSize = 32.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 36.sp
    )
}

private object LocationDetailsContent {

    @Composable
    operator fun invoke(
        state: LocationDetailsState.Loaded,
        onAction: (LocationDetailsAction) -> Unit
    ) {

        var offsetY by remember {
            mutableFloatStateOf(0f)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Header(
                state = state,
                offsetY = offsetY
            )

            LazyColumn {
                itemsIndexed(state.residents) { index, resident ->
                    if (index == 0) {
                        Box(modifier = Modifier.onGloballyPositioned {
                            offsetY = it.positionInParent().y
                        })
                    }


                    ResidentCard(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onAction(LocationDetailsAction.SelectedResident(resident.id))
                            },
                        resident = resident
                    )
                }
            }
        }
    }

    @Composable
    private fun Header(
        state: LocationDetailsState.Loaded,
        offsetY: Float
    ) {

        val density = LocalDensity.current

        val additionalHeight: Dp = with(density) { offsetY.toDp() }

        val animatedHeight by animateDpAsState(targetValue = 200.dp + additionalHeight)

        Box(
            modifier = Modifier.height(animatedHeight)
        ) {
            Column(
                modifier = Modifier
                    .background(SurfaceColor.copy(alpha = 0.3f))
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                Text(
                    modifier = Modifier
                        .background(SurfaceColor, RoundedCornerShape(4.dp))
                        .basicMarquee(iterations = Int.MAX_VALUE)
                        .padding(8.dp),
                    text = state.name,
                    fontSize = 21.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )

                AdditionalInfo(
                    type = state.type,
                    dimension = state.dimension,
                )

            }
        }
    }

    @Composable
    private fun AdditionalInfo(
        type: String,
        dimension: String
    ) = Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.width(8.dp))

        IconWithImage(
            modifier = Modifier.weight(1f),
            imageVector = Icons.Rounded.TravelExplore, text = type
        )

        Spacer(Modifier.width(16.dp))

        IconWithImage(
            modifier = Modifier.weight(1f),
            imageVector = Icons.Rounded.SquareFoot, text = dimension
        )

        Spacer(Modifier.width(8.dp))
    }

    @Composable
    private fun ResidentCard(
        modifier: Modifier, resident: Character
    ) =
        Column(
            modifier = modifier
                .shadow(1.dp, spotColor = PrimaryColor)
                .background(SurfaceColor)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Text(text = resident.name, fontSize = 11.sp)

            Text(
                text = resident.species,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, fontSize = 13.sp
            )

        }
}