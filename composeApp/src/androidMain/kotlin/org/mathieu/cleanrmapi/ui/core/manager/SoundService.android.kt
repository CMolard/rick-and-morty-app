package org.mathieu.cleanrmapi.ui.core.manager

import android.content.Context
import android.media.MediaPlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * SoundService implementation used on Android platforms.
 */
actual class SoundService : KoinComponent {
    private val context: Context by inject()

    private var mediaPlayer: MediaPlayer? = null

    actual fun playSound(soundName: String) {
        stopSound()

        val resourceId = context.resources.getIdentifier(
            soundName,
            "raw",
            context.packageName
        )

        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.start()
    }

    actual fun stopSound() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }

        mediaPlayer = null;
    }
}