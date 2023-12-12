package com.example.QuickMeal.Di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.QuickMeal.Di.Constants.INTRODUCTION_SP
import com.example.QuickMeal.Firebase.FirebaseCommon
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Appmodule {

    @Provides
    @Singleton
    fun provideFirebaseApp(application: Application): FirebaseApp {
        FirebaseApp.initializeApp(application)
        return FirebaseApp.getInstance()
    }

    @Provides
    @Singleton
    fun getFirebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth = FirebaseAuth.getInstance(firebaseApp)

    @Provides
    @Singleton
    fun getFirebaseDatabase(firebaseApp: FirebaseApp): FirebaseDatabase = FirebaseDatabase.getInstance(firebaseApp)

    @Provides
    @Singleton
    fun getFirebaseFirestore(firebaseApp: FirebaseApp): FirebaseFirestore = Firebase.firestore(firebaseApp)

    @Provides
    fun provideIntroductionSp(application: Application): SharedPreferences =
        application.getSharedPreferences(INTRODUCTION_SP, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesFirebaseCommon(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    )= FirebaseCommon(firestore,firebaseAuth)

    }


