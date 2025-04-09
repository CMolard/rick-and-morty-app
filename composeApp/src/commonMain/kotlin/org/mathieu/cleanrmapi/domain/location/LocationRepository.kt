package org.mathieu.cleanrmapi.domain.location

import org.mathieu.cleanrmapi.domain.location.models.Location

interface LocationRepository {

    /**
     * Fetches the details of a specific location based on the provided unique identifier.
     *
     * @param id The unique identifier of the location to be fetched.
     * @return Details of the location retrieved.
     */
    suspend fun getLocationDetailed(id: Int): Location
}