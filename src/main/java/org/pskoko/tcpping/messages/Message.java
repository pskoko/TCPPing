package org.pskoko.tcpping.messages;

/**
 * This class represents record of message on pitcher
 * side and is used for calculating statistics
 * Created by pskoko on 7/28/17.
 */
public class Message {
    /**
     * Minimum message length
     */
    public static final int MIN_BYTE_LENGTH = 50;
    /**
     * Maximum message length
     */
    public static final int MAX_BYTE_LENGTH = 3000;

    private int number;
    private long timeSentToCatcher;
    private long timeProcessedAtCatcher;
    private long timeReceivedFromCatcher;
    private long firstTripDuration;
    private long returnTripDuration;

    /**
     * Constructor for message
     * @param number number of message
     * @param timeSentToCatcher time sent to catcher
     */
    public Message(int number, long timeSentToCatcher){
        this.number = number;
        this.timeSentToCatcher = timeSentToCatcher;
    }

    public Message(int number, long timeSentToCatcher, long timeProcessedAtCatcher, long timeReceivedFromCatcher) {
        this.number = number;
        this.timeSentToCatcher = timeSentToCatcher;
        this.timeProcessedAtCatcher = timeProcessedAtCatcher;
        this.timeReceivedFromCatcher = timeReceivedFromCatcher;
    }

    public void setTimeSentToCatcher(long timeSentToCatcher) {
        this.timeSentToCatcher = timeSentToCatcher;
        updateDurations();
    }

    public void setTimeProcessedAtCatcher(long timeProcessedAtCatcher) {
        this.timeProcessedAtCatcher = timeProcessedAtCatcher;
        updateDurations();
    }

    public void setTimeReceivedFromCatcher(long timeReceivedFromCatcher) {
        this.timeReceivedFromCatcher = timeReceivedFromCatcher;
        updateDurations();
    }

    protected void updateDurations(){
        firstTripDuration = timeProcessedAtCatcher - timeSentToCatcher;
        returnTripDuration = timeReceivedFromCatcher - timeProcessedAtCatcher;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public long getTimeSentToCatcher() {
        return timeSentToCatcher;
    }

    public long getTimeProcessedAtCatcher() {
        return timeProcessedAtCatcher;
    }

    public long getTimeReceivedFromCatcher() {
        return timeReceivedFromCatcher;
    }

    public long getFirstTripDuration() {
        return firstTripDuration;
    }

    public long getReturnTripDuration() {
        return returnTripDuration;
    }

}
