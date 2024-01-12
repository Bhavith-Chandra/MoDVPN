package app.modvpn.modvpn.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    val email: String,
    val token: String
)
