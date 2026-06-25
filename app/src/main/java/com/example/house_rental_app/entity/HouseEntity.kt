package com.example.house_rental_app.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "house_table",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HouseEntity(
    @PrimaryKey(autoGenerate = true)
    var houseId: Int = 0,
    var ownerId: Int = 0,
    @Ignore var firebaseHouseId: String = "",
    @Ignore var ownerKey: String = "",
    var price: Int = 0,
    var address: String = "",
    var images: String = "",
    var lease: String = "",
    var description: String = "",
    var isAvailable: Boolean = true,
    var createdAt: String? = null,
    var updatedAt: String? = null
)