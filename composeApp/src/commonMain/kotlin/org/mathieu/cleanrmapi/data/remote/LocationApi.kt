package org.mathieu.cleanrmapi.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.mathieu.cleanrmapi.data.remote.responses.LocationResponse

/**
 * API client for fetching location data from the Rick and Morty API.
 */
internal class LocationApi(private val client: HttpClient)
{
    /**
     * Fetches a location from the API.
     *
     * @param id The unique id of the location to fetch.
     * @return A paginated response containing a list of [LocationResponse] for the specified page.
     * @throws HttpException if the request fails or if the status code is not a success.
     */
    suspend fun getLocation(id: Int): LocationResponse? = client
        .get("location/$id")
        .accept(HttpStatusCode.OK)
        .body()
}
