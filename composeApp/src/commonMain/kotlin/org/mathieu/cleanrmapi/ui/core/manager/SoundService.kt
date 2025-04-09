package org.mathieu.cleanrmapi.ui.core.manager

/**
 * Service used to play sound.
 */
expect class SoundService() {
    /**
     * Play a specific sound.
     */
    fun playSound(soundName: String)

    /**
     * Stop the playing sound.
     */
    fun stopSound()
}