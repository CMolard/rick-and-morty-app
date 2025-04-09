package org.mathieu.cleanrmapi.ui.core.manager

import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent

/**
 * SoundService implementation used on desktop platforms.
 */
actual class SoundService {
    private var clip: Clip? = null

    actual fun playSound(soundName: String) {
        stopSound()

        try {
            val soundStream = javaClass.getResourceAsStream("/sounds/$soundName.wav")
            val audioStream = AudioSystem.getAudioInputStream(BufferedInputStream(soundStream))

            clip = AudioSystem.getClip()
            clip?.open(audioStream)
            clip?.start()

            clip?.addLineListener { event ->
                if (event.type == LineEvent.Type.STOP) {
                    clip?.close()
                }
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it)
            println("Error playing sound: ${e.message}")
        }
    }

    actual fun stopSound() {
        clip?.apply {
            if (isRunning) {
                stop()
            }

            close()
        }

        clip = null
    }
}