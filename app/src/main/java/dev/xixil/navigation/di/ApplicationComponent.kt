package dev.xixil.navigation.di

import dagger.Component


@Component(
    modules = [DataModule::class, AuthModule::class]
)
interface ApplicationComponent