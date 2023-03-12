package com.limboooo.contactrecorder.repository.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.limboooo.contactrecorder.activity.ProjectApplication
import com.limboooo.contactrecorder.repository.room.entity.normal.Moneys
import com.limboooo.contactrecorder.repository.room.entity.normal.Names
import com.limboooo.contactrecorder.repository.room.entity.normal.NormalKey
import com.limboooo.contactrecorder.repository.room.entity.normal.Things
import com.limboooo.contactrecorder.repository.room.entity.whole.Emails
import com.limboooo.contactrecorder.repository.room.entity.whole.Exchanges
import com.limboooo.contactrecorder.repository.room.entity.whole.Phones
import com.limboooo.contactrecorder.repository.room.entity.whole.RelativesBaseInfo

@Database(
    entities = [RelativesBaseInfo::class, NormalKey::class, Moneys::class, Names::class, Things::class, Phones::class, Emails::class, Exchanges::class],
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