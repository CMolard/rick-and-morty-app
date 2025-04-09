package org.mathieu.cleanrmapi.ui

import org.koin.dsl.module
import org.mathieu.cleanrmapi.ui.core.manager.SoundService

/**
 * Injected module for UI dependencies.
 */
val uiModule = module {
    single { SoundService() }
}