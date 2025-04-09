package org.mathieu.cleanrmapi.data.local.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mathieu.cleanrmapi.data.extensions.extractIdsFromUrls
import org.mathieu.cleanrmapi.data.local.RMDatabase
import org.mathieu.cleanrmapi.data.remote.responses.LocationResponse
import org.mathieu.cleanrmapi.data.validators.annotations.MustBeCommaSeparatedIds
import org.mathieu.cleanrmapi.domain.character.models.Character
import org.mathieu.cleanrmapi.domain.location.models.Location

/**
 * Data class representing a location object.
 */
@Entity(tableName = RMDatabase.LOCATION_TABLE)
class LocationObject(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    @MustBeCommaSeparatedIds
    val residentsIds: String,
    val created: String
)

/**
 * Extension function to convert a [LocationResponse] to a [LocationObject].
 */
internal fun LocationResponse.toDBObject() = LocationObject(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    residentsIds = residents.extractIdsFromUrls(),
    created = created
)

/**
 * Extension function to convert a [LocationObject] to a [Location].
 *
 * @param idsToCharacterConverter A suspend function that converts the residents IDs to a list of [Character].
 */
internal suspend fun LocationObject.toModel(
    idsToCharacterConverter: suspend (residentsIds: String) -> List<Character> = { emptyList() }
) = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    residents = idsToCharacterConverter(residentsIds)
)
