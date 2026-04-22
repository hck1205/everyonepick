package com.everyonepick.core.data.remote

import com.everyonepick.core.data.network.ApiFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VoteApiModule {
    @Provides
    @Singleton
    fun provideVoteApi(
        apiFactory: ApiFactory,
    ): VoteApi = apiFactory.create(VoteApi::class.java)
}

