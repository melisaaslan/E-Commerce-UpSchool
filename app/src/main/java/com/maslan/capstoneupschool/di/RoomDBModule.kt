package com.maslan.capstoneupschool.di

import android.content.Context
import androidx.room.Room
import com.maslan.capstoneupschool.data.source.local.ProductRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Singleton
    @Provides
    fun provideProductRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ProductRoomDB::class.java, "products_room").build()

    @Singleton
    @Provides
    fun provideProductDao(roomDB: ProductRoomDB) = roomDB.productDao()

}