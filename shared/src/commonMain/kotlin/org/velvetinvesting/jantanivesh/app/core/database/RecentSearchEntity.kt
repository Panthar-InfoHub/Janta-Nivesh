package org.velvetinvesting.jantanivesh.app.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/**
 * One term the user has searched for.
 *
 * The query itself is the primary key, so searching the same term twice updates its timestamp
 * instead of adding a duplicate row — which is exactly the "move it back to the top" behaviour a
 * recents list needs, without a separate de-duplication pass.
 */
@Entity(tableName = RecentSearchEntity.TABLE_NAME)
data class RecentSearchEntity(
    @PrimaryKey val query: String,
    /** Epoch milliseconds; the list is ordered on this and nothing else. */
    val searchedAt: Long
) {
    companion object {
        const val TABLE_NAME = "recent_searches"
    }
}
