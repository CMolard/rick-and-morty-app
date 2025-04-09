package org.mathieu.cleanrmapi.data.repositories

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.cleanrmapi.common.toList
import org.mathieu.cleanrmapi.data.local.LocationDAO
import org.mathieu.cleanrmapi.data.local.objects.LocationObject
import org.mathieu.cleanrmapi.data.local.objects.toDBObject
import org.mathieu.cleanrmapi.data.local.objects.toModel
import org.mathieu.cleanrmapi.data.remote.CharacterApi
import org.mathieu.cleanrmapi.data.remote.LocationApi
import org.mathieu.cleanrmapi.data.validators.annotations.MustBeCommaSeparatedIds
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.location.LocationRepository
import org.mathieu.cleanrmapi.domain.location.models.Location

internal class LocationRepositoryImpl(
    private val characterApi: CharacterApi
) : LocationRepository {
    override suspend fun getLocationDetailed(id: Int): Location {
        val location = GetLocationObjectIfExists(id)

        return location.toModel(idsToCharacterConverter = ::getResidentsFromIdList)
    }

    /**
     * Retrieves the list of residents from the given list of resident comma-separated IDs.
     *
     * @param residentsIdsList A string containing the IDs of the residents, separated by commas.
     * @return A list of [Character] objects corresponding to the resident IDs.
     */
    private suspend fun getResidentsFromIdList(@MustBeCommaSeparatedIds residentsIdsList: String): List<Character> {

        return if (residentsIdsList.contains(",")) {
            val charactersResponses = characterApi.getCharactersFromIds(ids = residentsIdsList)
            charactersResponses.map { it.toDBObject().toModel() }
        } else {
            val charactersResponse = characterApi.getCharacter(residentsIdsList.toInt())
            charactersResponse?.toDBObject()?.toModel()?.toList() ?: emptyList()
        }
    }
}

/**
 * Helper object used to fetch a location object either from local storage or remotely.
 */
private object GetLocationObjectIfExists : KoinComponent {

    private val locationApi: LocationApi by inject()
    private val locationLocal: LocationDAO by inject()

    /**
     * Tries to fetch a location locally, if not found fetch remotely.
     *
     * @param locationId The unique ID of the location to fetch.
     * @return The [LocationObject] corresponding to the given ID.
     * @throws Exception if the location cannot be found either locally or remotely.
     */
    suspend operator fun invoke(locationId: Int): LocationObject =
        tryToGetLocationLocally(locationId)
            .fetchRemotelyIfNotFound(locationId)
            .throwIfWeCannotFindIt()

    private suspend fun tryToGetLocationLocally(id: Int) = locationLocal.getLocation(id)

    /**
     * Fetch a specific location from the remote API and store it locally.
     *
     * @param id The unique ID of the location to fetch.
     * @return The [LocationObject] corresponding to the given ID, or null if not found.
     */
    private suspend fun LocationObject?.fetchRemotelyIfNotFound(id: Int): LocationObject? {
        if (this != null) return this

        return locationApi.getLocation(id = id)
            ?.toDBObject()
            ?.also { obj ->
                locationLocal.insert(obj)
            }
    }

    private fun LocationObject?.throwIfWeCannotFindIt(): LocationObject {
        if (this != null) return this
        throw Exception("Could not find the location neither locally nor remotely.")
    }

}