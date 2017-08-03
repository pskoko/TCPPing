package org.pskoko.tcpping.org.pskoko.tccping.packets;

import java.nio.ByteBuffer;

/**
 * Represents one packet which is going to be send from
 * Pitcher to Cather or opposite
 * Created by pskoko on 7/28/17.
 */
public class Packet {
    /**
     * Minimum length of packet
     */
    public static final int MIN_BYTE_LENGTH = 16;
    private int number;
    private int packetSize;
    private long timeProcessed;

    /**
     * Construcotr of Packet
     * @param number number of packet
     * @param packetSize size of packet
     * @param timeProcessed time at which packet was processed and sent
     */
    public Packet(int number, int packetSize, long timeProcessed){
        this.number = number;
        this.packetSize = packetSize;
        this.timeProcessed = timeProcessed;
    }

    /**
     * Constructor for packet
     * @param byteArray packet represented as array of bytes
     */
    public Packet(byte[] byteArray){
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        this.number = buffer.getInt();
        this.packetSize = buffer.getInt();
        this.timeProcessed = buffer.getLong();
    }

    /**
     * Returns packet representet as array of bytest prepared to be sent
     * over network
     * @return packet representet as array of bytest prepared to be sent
     * over network
     */
    public byte[] getByteRepresentation(){
        ByteBuffer buffer = ByteBuffer.allocate(packetSize);
        buffer.putInt(number);
        buffer.putInt(packetSize);
        buffer.putLong(timeProcessed);

        return buffer.array();
    }

    /**
     * Returns number of packet
     * @return number of packet
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets number of packet
     * @param number number of packet
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Return time at which packet was processed and sent
     * @return time at which packet was processed and sent
     */
    public long getTimeProcessed() {
        return timeProcessed;
    }

    /**
     * Sets time at which packet was processed and sent
     * @param timeProcessed time at which packet was processed and sent
     */
    public void setTimeProcessed(long timeProcessed) {
        this.timeProcessed = timeProcessed;
    }

    /**
     * Returns packet size
     * @return packet size
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * Sets the packet size
     * @param packetSize the size of packet
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }
}
