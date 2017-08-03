package org.pskoko.tcpping.execution;

import org.pskoko.tcpping.messages.Message;
import org.pskoko.tcpping.org.pskoko.tccping.packets.NoPacketsException;
import org.pskoko.tcpping.org.pskoko.tccping.packets.Packet;
import org.pskoko.tcpping.org.pskoko.tccping.packets.PacketReceiver;
import org.pskoko.tcpping.org.pskoko.tccping.packets.PacketSender;

import java.io.*;
import java.net.*;

/**
 * This class represents the catcher execution mode in which programs
 * listens for messages and responds appropriately
 * Created by pskoko on 7/31/17.
 */
public class Catcher extends ExectutionMode{
    private int port;
    private InetAddress ipaddress;
    private PrintStream reportsWriter;
    private Socket socket;
    private PacketReceiver packetReceiver;
    private PacketSender packetSender;


    /**
     * Constructor for catcher
     * @param port port for listening
     * @param ipaddress ip addres for pinding
     * @param reportsWriter PrintStream to write error and status messages
     */
    public Catcher(int port, InetAddress ipaddress, PrintStream reportsWriter) {
        this.port = port;
        this.ipaddress = ipaddress;
        this.reportsWriter = reportsWriter;
    }


    /**
     * Executes Catcher mode
     */
    public void execute(){
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ipaddress, port));
            socket = serverSocket.accept();
            packetReceiver = new PacketReceiver(socket.getInputStream(), Message.MAX_BYTE_LENGTH);
            packetSender = new PacketSender(socket.getOutputStream());
            handleMessages();
        } catch (IOException e) {
            reportsWriter.println("Error with connection");
            return;
        }
    }

    private void handleMessages() throws IOException {
        while(true){
            try {
                Packet incomingPacket = packetReceiver.getNextPacket();
                Packet outcomingPacket = new Packet(incomingPacket.getNumber(), incomingPacket.getPacketSize(), System.currentTimeMillis());
                packetSender.sendPacket(outcomingPacket);
            } catch (NoPacketsException e) {
                return;
            }
        }
    }

    /**
     * Return port on which catcher listens
     * @return port on which catcher listens
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns ip address for binding
     * @return ip address for bidning
     */
    public InetAddress getIpaddress() {
        return ipaddress;
    }

    /**
     * Returns print stream on which status and error messages are written
     * @return print stream on which status and error messages are written
     */
    public PrintStream getReportsWriter() {
        return reportsWriter;
    }
}
