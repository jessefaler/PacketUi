package com.protoxon.promenu.packets;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;

public class SoundPackets {

    /**
     * Plays an ambient sound at the origin (0, 0, 0) with maximum volume.
     *
     * @param sound the sound to play
     * @param user  the user to whom the sound will be played
     */
    public void play(Sound sound, User user) {
        SoundCategory category = SoundCategory.AMBIENT;
        Vector3i position = new Vector3i(0, 0, 0);
        WrapperPlayServerSoundEffect packet = new WrapperPlayServerSoundEffect(
                sound,
                category,
                position,
                99999999999999999999999999999999999999.0f, // volume
                1.0f  // pitch
        );

        user.sendPacket(packet);
    }

}
