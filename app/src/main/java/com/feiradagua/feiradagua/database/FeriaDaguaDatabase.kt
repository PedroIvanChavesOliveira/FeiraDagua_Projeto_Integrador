package com.feiradagua.feiradagua.database

//import android.content.Context
//import androidx.room.Database
//import androidx.room.*
//import androidx.room.RoomDatabase
//import com.feiradagua.feiradagua.database.entitys.DataDao
//import com.feiradagua.feiradagua.model.roomClass.SaveDataProducer
//import com.feiradagua.feiradagua.model.roomClass.SaveDataUser
//
//
//object FeiraDaguaDatabase {
//    @Database(entities = [SaveDataUser::class, SaveDataProducer::class], version = 1, exportSchema = false)
//
//    abstract class FeiraDaguaRoomDatabase: RoomDatabase(){
//        abstract fun dataDao(): DataDao
//    }
//
//    fun getDatabase(context: Context) : FeiraDaguaRoomDatabase{
//        return Room.databaseBuilder(context, FeiraDaguaRoomDatabase::class.java,"feiraDagua-db"
//        ).build()
//    }
//}