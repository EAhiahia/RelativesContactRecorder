package com.limboooo.contactrecorder.repository.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.limboooo.contactrecorder.activity.ProjectApplication
import com.limboooo.contactrecorder.repository.room.entity.*

@Database(
    entities = [RelativesInfoWhole::class, RelativesBaseInfo::class, Phones::class, Emails::class, Exchanges::class, NormalDataSet::class],
    version = 1
)
abstract class ProjectDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    companion object {

        @Volatile
        private var INSTANCE: ProjectDatabase? = null

        fun getDatabase(): ProjectDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ProjectApplication.context,
                    ProjectDatabase::class.java,
                    "relatives_information"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}