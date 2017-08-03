package org.pskoko.tcpping.org.pskoko.tccping.packets;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is used for sending packets
 * over output stream
 * Created by pskoko on 7/31/17.
 */
public class PacketSender {
    private BufferedOutputStream outputStream;

    /**
     * Constructor of PacketSender
     * @param outputStream outputStream to send packets over
     */
    public PacketSender(OutputStream outputStream) {
        this.outputStream = new BufferedOutputStream(outputStream);
    }

    /**
     * SEnding packet over network
     * @param packet packet to be sent over network
     * @throws IOException
     */
    public void sendPacket(Packet packet) throws IOException {
        outputStream.write(packet.getByteRepresentation());
        outputStream.flush();
    }
}
