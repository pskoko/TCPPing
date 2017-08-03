package org.pskoko.tcpping.messages;

import org.pskoko.tcpping.org.pskoko.tccping.packets.Packet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pskoko on 7/28/17.
 */
public class MessageStatistics {
    private HashMap<Integer, Message> unansweredMessages;
    private List<Message> processedMessages;
    private int temporaryMessageNumber = 0;

    private int totalMessageNumber = 0;


    public MessageStatistics(){
        unansweredMessages = new LinkedHashMap<>();
        processedMessages = new LinkedList<>();
    }

    public synchronized void recordAnswer(Packet packet) throws NonExistentMessageException {
        Message message = unansweredMessages.get(packet.getNumber());
        if(message == null) {
            throw new NonExistentMessageException();
        }
        message.setTimeProcessedAtCatcher(packet.getTimeProcessed());
        message.setTimeReceivedFromCatcher(System.currentTimeMillis());

        processedMessages.add(message);
        unansweredMessages.remove(packet.getNumber());
    }

    public synchronized Message createNewMessage(){
        Message message = new Message(totalMessageNumber+1, System.currentTimeMillis());
        totalMessageNumber++;
        temporaryMessageNumber++;

        unansweredMessages.put(message.getNumber(), message);
        return message;
    }

    public synchronized List<Message> getUnansweredMessages(){
        return new ArrayList<>(unansweredMessages.values());
    }

    public synchronized double getAverageFirstTripDuration() {
        OptionalDouble avg = processedMessages.stream().mapToDouble(Message::getFirstTripDuration).average();
        if(avg.isPresent()){
            return avg.getAsDouble();
        }
        return Double.NaN;
    }

    public synchronized double getAverageReturnTripDuration(){
        OptionalDouble avg = processedMessages.stream().mapToDouble(Message::getReturnTripDuration).average();
        if(avg.isPresent()){
            return avg.getAsDouble();
        }
        return Double.NaN;
    }

    public synchronized double getAverageRoundTripDuration(){
        OptionalDouble avg = processedMessages.stream().mapToDouble(m -> m.getFirstTripDuration() + m.getReturnTripDuration()).average();
        if(avg.isPresent()){
            return avg.getAsDouble();
        }
        return Double.NaN;

    }

    public synchronized long getMaxFirstTripDuration() {
        OptionalLong maxt = processedMessages.stream().mapToLong(Message::getFirstTripDuration).max();
        if(maxt.isPresent()){
            return maxt.getAsLong();
        }
        return 0;
    }

    public synchronized long getMaxReturnTripDuration() {
        OptionalLong maxt = processedMessages.stream().mapToLong(Message::getReturnTripDuration).max();
        if(maxt.isPresent()){
            return maxt.getAsLong();
        }
        return 0;
    }

    public synchronized long getMaxRoundTripDuration() {
        OptionalLong maxt = processedMessages.stream().mapToLong(m -> m.getReturnTripDuration() + m.getFirstTripDuration()).max();
        if(maxt.isPresent()){
            return maxt.getAsLong();
        }
        return 0;
    }

    public synchronized int getTotalNumberOfMessages(){
        return totalMessageNumber;
    }

    public synchronized int getTemporaryMessageNumber(){
        return temporaryMessageNumber;
    }

    public String generateStatistics(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timeString = dateFormat.format(Calendar.getInstance().getTime());

        String dataString = String.format("%5d %3d %4f %4f %4f %4d %4d %4d",
                totalMessageNumber,
                temporaryMessageNumber,
                getAverageFirstTripDuration(),
                getAverageReturnTripDuration(),
                getAverageRoundTripDuration(),
                getMaxFirstTripDuration(),
                getMaxReturnTripDuration(),
                getMaxReturnTripDuration()
        );

        return timeString + " " + dataString;
    }

    public synchronized void clear(){
        unansweredMessages.clear();
        processedMessages.clear();
        temporaryMessageNumber = 0;
    }


}
