package dev.xixil.navigation.di

import dagger.Component


@Component(
    modules = [DataModule::class]
)
interface ApplicationComponent {
}