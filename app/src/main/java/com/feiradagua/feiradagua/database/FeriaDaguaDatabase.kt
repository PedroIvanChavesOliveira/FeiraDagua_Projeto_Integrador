package com.feiradagua.feiradagua.database

import android.content.Context
//import androidx.room.Database
//import androidx.room.*
//import androidx.room.RoomDatabase
//import com.feiradagua.feiradagua.database.entitys.DataDao
//import com.feiradagua.feiradagua.model.roomClass.SaveDataProducer
//import com.feiradagua.feiradagua.model.roomClass.SaveDataUser


//@Database(entities = [SaveDataUser::class, SaveDataProducer::class], version = 0, exportSchema = false)
//abstract class FeiraDaguaRoomDatabase: RoomDatabase() {
//    abstract fun dataDao(): DataDao
//
//    companion object {
//        // Singleton prevents multiple instances of database opening at the
//        // same time.
//        @Volatile
//        private var INSTANCE: FeiraDaguaRoomDatabase? = null
//
//        fun getDatabase(context: Context): FeiraDaguaRoomDatabase {
//            // if the INSTANCE is not null, then return it,
//            // if it is, then create the database
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        FeiraDaguaRoomDatabase::class.java,
//                        "feiraDagua-db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

//    fun getDatabase(context: Context) : FeiraDaguaRoomDatabase{
//        return Room.databaseBuilder(context, FeiraDaguaRoomDatabase::class.java,"feiraDagua-db"
//        ).build()
//    }