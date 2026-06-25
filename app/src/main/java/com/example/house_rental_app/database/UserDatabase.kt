package com.example.house_rental_app.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.house_rental_app.dao.HouseDao
import com.example.house_rental_app.dao.UserDao
import com.example.house_rental_app.entity.HouseEntity
import com.example.house_rental_app.entity.UserEntity

@Database(entities = [UserEntity::class, HouseEntity::class], version = 3, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun houseDao(): HouseDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}