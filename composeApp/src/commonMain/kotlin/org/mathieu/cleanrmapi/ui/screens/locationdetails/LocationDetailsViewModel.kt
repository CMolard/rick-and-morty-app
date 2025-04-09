package org.mathieu.cleanrmapi.ui.screens.locationdetails

import org.koin.core.component.inject
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.location.LocationRepository
import org.mathieu.cleanrmapi.ui.core.Destination
import org.mathieu.cleanrmapi.ui.core.ViewModel

class LocationDetailsViewModel : ViewModel<LocationDetailsState>(LocationDetailsState.Loading) {

    private val locationRepository: LocationRepository by inject()

    /**
     * First method to call to initialize the view model with the expected location data.
     *
     * @param locationId Unique id of the location to view.
     */
    fun init(locationId: Int) {
        fetchData(
            source = { locationRepository.getLocationDetailed(id = locationId) }
        ) {
            onSuccess { details ->
                updateState {
                    LocationDetailsState.Loaded(
                        name = details.name,
                        type = details.type,
                        dimension = details.dimension,
                        residents = details.residents
                    )
                }
            }

            onFailure {
                updateState {
                    LocationDetailsState.Error(message = it.message ?: it.toString())
                }
            }
        }
    }

    /**
     * Handle the action triggered by the user.
     *
     * @param action The action to handle.
     */
    fun handleAction(action: LocationDetailsAction) {
        when (action) {
            is LocationDetailsAction.SelectedResident ->
                sendEvent(Destination.CharacterDetails(action.characterId.toString()))
        }
    }
}

sealed interface LocationDetailsState {
    data object Loading : LocationDetailsState
    data class Loaded(
        val name: String,
        val type: String,
        val dimension: String,
        val residents: List<Character>,
    ) : LocationDetailsState

    data class Error(val message: String) : LocationDetailsState
}

sealed interface LocationDetailsAction {
    data class SelectedResident(val characterId: Int) : LocationDetailsAction
}