package io.github.dsheirer.module.decode.dmr.message.voice;

import io.github.dsheirer.bits.CorrectedBinaryMessage;
import io.github.dsheirer.identifier.Identifier;
import io.github.dsheirer.module.decode.dmr.DMRSyncPattern;
import io.github.dsheirer.module.decode.dmr.message.DMRMessage;

import java.util.ArrayList;
import java.util.List;

public class VoiceMessage extends DMRMessage {

    /**
     * DMR message frame.  This message is comprised of a 24-bit prefix and a 264-bit message frame.  Outbound base
     * station frames transmit a Common Announcement Channel (CACH) in the 24-bit prefix, whereas Mobile inbound frames
     * do not use the 24-bit prefix.
     *
     * @param syncPattern
     * @param message     containing 288-bit DMR message with preliminary bit corrections indicated.
     */
    public VoiceMessage(DMRSyncPattern syncPattern, CorrectedBinaryMessage message) {
        super(syncPattern, message);
    }

    public List<byte[]> getAMBEFrames()
    {
        List<byte[]> frames = new ArrayList<byte[]>();
        frames.add(mMessage.get(24, 24 + 108 - 1).toByteArray());
        frames.add(mMessage.get(180, 180 + 108 - 1).toByteArray());
        return frames;
    }
    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public List<Identifier> getIdentifiers() {
        return null;
    }
}
