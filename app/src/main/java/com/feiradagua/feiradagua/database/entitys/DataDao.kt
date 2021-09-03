package com.feiradagua.feiradagua.database.entitys

//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.google.android.material.circularreveal.CircularRevealHelper
//import com.google.firebase.Timestamp
//import com.google.firebase.firestore.FieldValue
//import java.util.*
//
//@Dao
//interface DataDao {
//    @Query("SELECT last_login_data FROM userData WHERE user_id=:id")
//    suspend fun getLastloginDateUser(id: String): Date
//
//    @Query("SELECT last_login_data FROM producerData WHERE producer_id=:id")
//    suspend fun getLastloginDateProducer(id: String): Timestamp
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertLastLoginDateUser(lastLoginDate: Date)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertLastLoginDateProducer(lastLoginDate: Timestamp)
//}