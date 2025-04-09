package org.mathieu.cleanrmapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.mathieu.cleanrmapi.data.local.objects.LocationObject

/**
 * Data Access Object (DAO) for managing location data in the local database.
 *
 * This interface defines methods for querying and inserting location data.
 */
@Dao
interface LocationDAO {
    @Query("SELECT * FROM ${RMDatabase.LOCATION_TABLE} WHERE id = :id")
    suspend fun getLocation(id: Int): LocationObject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationObject)
}