package com.feiradagua.feiradagua.database.entitys

//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
import com.google.android.material.circularreveal.CircularRevealHelper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

//@Dao
//interface DataDao {
//    @Query("SELECT last_login_data FROM userData WHERE user_id=:id")
//    fun getLastloginDateUser(id: String): FieldValue
//
//    @Query("SELECT last_login_data FROM producerData WHERE producer_id=:id")
//    fun getLastloginDateProducer(id: String): Timestamp
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertLastLoginDateUser(lastLoginDate: FieldValue)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertLastLoginDateProducer(lastLoginDate: Timestamp)
//}