package org.pskoko.tcpping.execution;

import org.pskoko.tcpping.messages.Message;
import org.pskoko.tcpping.messages.MessageStatistics;
import org.pskoko.tcpping.messages.NonExistentMessageException;
import org.pskoko.tcpping.org.pskoko.tccping.packets.NoPacketsException;
import org.pskoko.tcpping.org.pskoko.tccping.packets.Packet;
import org.pskoko.tcpping.org.pskoko.tccping.packets.PacketReceiver;
import org.pskoko.tcpping.org.pskoko.tccping.packets.PacketSender;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class represents pitcher execution mode in which
 * program sends specified number of messages per second
 * and reports statistics of returned messages for every second
 * Created by pskoko on 7/31/17.
 */
public class Pitcher extends ExectutionMode{
    private int port;
    private int messagesPerSecond;
    private int messageSize;
    private String hostname;
    private PrintStream reportsWriter;
    private MessageStatistics messageStatistics;
    private Socket socket;
    private boolean messagesSent = false;


    /**
     * The constructor for pitcher
     * @param port port to which send the messages
     * @param messagesPerSecond number of messages per second to send
     * @param messageSize the size of message to send
     * @param hostname hostname to which send messages to
     * @param reportsWriter PrintStream to which write status and error messages to
     */
    public Pitcher(int port, int messagesPerSecond, int messageSize, String hostname, PrintStream reportsWriter) {
        this.port = port;
        this.messagesPerSecond = messagesPerSecond;
        this.messageSize = messageSize;
        this.hostname = hostname;
        this.reportsWriter = reportsWriter;
        this.messageStatistics = new MessageStatistics();
    }


    /**
     * Executes Pithcer mode
     */
    public void execute(){
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            reportsWriter.println("Error: Unable to connect to specified endpoint");
            return;
        }

        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
        scheduledService.scheduleAtFixedRate(() -> periodicSending(), 0, 1, TimeUnit.SECONDS );

        try {
            listenForAnswers();
        } catch (IOException e) {
            reportsWriter.println("Error: unable to receive answers");
            scheduledService.shutdownNow();

            try {
                scheduledService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e1) {

            }
        }
    }

    private void listenForAnswers() throws IOException{
        PacketReceiver packetReceiver = new PacketReceiver(socket.getInputStream(), Message.MAX_BYTE_LENGTH);

        while(true){
            //System.out.println((new Packet(rawMessage)).getNumber());
            Packet packet = null;
            try {
                packet = packetReceiver.getNextPacket();
            } catch (NoPacketsException e) {
                break;
            }

            if(packet.getPacketSize() != messageSize){
                reportsWriter.println("Invalid answer: received answer of length " + packet.getPacketSize() + ", expected " + messageSize);
                continue;
            }
            try {
                messageStatistics.recordAnswer(packet);
            } catch(NonExistentMessageException ex){
                continue;
            }
        }
    }

    private void sendMessages() throws IOException{
        PacketSender packetSender = new PacketSender(socket.getOutputStream());
        for(int i = 0; i < messagesPerSecond; i++){
            Message message = messageStatistics.createNewMessage();
            Packet packet = new Packet(message.getNumber(), messageSize, message.getTimeSentToCatcher());
            packetSender.sendPacket(packet);
        }
    }

    private void reportStatistics(){
        reportsWriter.println(messageStatistics.generateStatistics());
        messageStatistics.clear();
    }

    private void periodicSending(){
        if(messagesSent){
            reportStatistics();
        }
        try {
            sendMessages();
            messagesSent = true;
        } catch (IOException e) {
            reportsWriter.println("Error: unable to send messages over socket");
        }
    }

}
