package org.pskoko.tcpping.org.pskoko.tccping.packets;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * This class is used for receiveng packets
 * over input stream ensuring that packets are properly
 * delmited in stream of bytes
 * Created by pskoko on 7/31/17.
 */
public class PacketReceiver {
    private PushbackInputStream inputStream;
    private byte[] rawPacket;
    private int maxPacketSize;


    /**
     * Constructor of PacketReceiver
     * @param inputStream inputStream to receive bytes from
     * @param maxPacketSize maximum size of packet
     */
    public PacketReceiver(InputStream inputStream, int maxPacketSize) {
        this.maxPacketSize = maxPacketSize;
        this.inputStream = new PushbackInputStream(new BufferedInputStream(inputStream), maxPacketSize);
        this.rawPacket = new byte[maxPacketSize];
    }

    /**
     * Returns the next packet. Throws exception if connection
     * become closed
     * @return the next packet
     * @throws IOException
     * @throws NoPacketsException
     */
    public Packet getNextPacket() throws IOException, NoPacketsException {
        int len = inputStream.read(rawPacket);
        if(len == -1){
            throw new NoPacketsException();
        }

        Packet incomingPacket = new Packet(rawPacket);

        if(len > incomingPacket.getPacketSize()){
            inputStream.unread(rawPacket, incomingPacket.getPacketSize(), len - incomingPacket.getPacketSize());
        }
        return incomingPacket;
    }
}
