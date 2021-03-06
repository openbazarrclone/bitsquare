package io.bitsquare.p2p.network;

import io.bitsquare.common.UserThread;
import io.bitsquare.p2p.Message;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Statistic {
    private static final Logger log = LoggerFactory.getLogger(Statistic.class);


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Static
    ///////////////////////////////////////////////////////////////////////////////////////////

    private final static IntegerProperty totalSentBytes = new SimpleIntegerProperty(0);
    private final static IntegerProperty totalReceivedBytes = new SimpleIntegerProperty(0);

    public static int getTotalSentBytes() {
        return totalSentBytes.get();
    }

    public static IntegerProperty totalSentBytesProperty() {
        return totalSentBytes;
    }

    public static int getTotalReceivedBytes() {
        return totalReceivedBytes.get();
    }

    public static IntegerProperty totalReceivedBytesProperty() {
        return totalReceivedBytes;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Instance fields
    ///////////////////////////////////////////////////////////////////////////////////////////

    private final Date creationDate;
    private long lastActivityTimestamp = System.currentTimeMillis();
    private final IntegerProperty sentBytes = new SimpleIntegerProperty(0);
    private final IntegerProperty receivedBytes = new SimpleIntegerProperty(0);
    private final Map<String, Integer> receivedMessages = new ConcurrentHashMap<>();
    private final Map<String, Integer> sentMessages = new ConcurrentHashMap<>();
    private final IntegerProperty roundTripTime = new SimpleIntegerProperty(0);


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////////////

    public Statistic() {
        creationDate = new Date();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Update, increment
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void updateLastActivityTimestamp() {
        UserThread.execute(() -> lastActivityTimestamp = System.currentTimeMillis());
    }

    public void addSentBytes(int value) {
        UserThread.execute(() -> {
            sentBytes.set(sentBytes.get() + value);
            totalSentBytes.set(totalSentBytes.get() + value);
        });
    }

    public void addReceivedBytes(int value) {
        UserThread.execute(() -> {
            receivedBytes.set(receivedBytes.get() + value);
            totalReceivedBytes.set(totalReceivedBytes.get() + value);
        });
    }

    // TODO would need msg inspection to get useful information...
    public void addReceivedMessage(Message message) {
        String messageClassName = message.getClass().getSimpleName();
        int counter = 1;
        if (receivedMessages.containsKey(messageClassName))
            counter = receivedMessages.get(messageClassName) + 1;

        receivedMessages.put(messageClassName, counter);
    }

    public void addSentMessage(Message message) {
        String messageClassName = message.getClass().getSimpleName();
        int counter = 1;
        if (sentMessages.containsKey(messageClassName))
            counter = sentMessages.get(messageClassName) + 1;

        sentMessages.put(messageClassName, counter);
    }

    public void setRoundTripTime(int roundTripTime) {
        this.roundTripTime.set(roundTripTime);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////////////////

    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    public long getLastActivityAge() {
        return System.currentTimeMillis() - lastActivityTimestamp;
    }

    public int getSentBytes() {
        return sentBytes.get();
    }

    public IntegerProperty sentBytesProperty() {
        return sentBytes;
    }

    public int getReceivedBytes() {
        return receivedBytes.get();
    }

    public IntegerProperty receivedBytesProperty() {
        return receivedBytes;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public IntegerProperty roundTripTimeProperty() {
        return roundTripTime;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "creationDate=" + creationDate +
                ", lastActivityTimestamp=" + lastActivityTimestamp +
                ", sentBytes=" + sentBytes +
                ", receivedBytes=" + receivedBytes +
                '}';
    }

}
