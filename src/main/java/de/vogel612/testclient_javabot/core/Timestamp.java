package de.vogel612.testclient_javabot.core;

import java.util.concurrent.atomic.AtomicLong;

@Deprecated
public final class Timestamp implements Comparable<Timestamp> {

    private static final AtomicLong generator = new AtomicLong(0);

    private final long              value;

    public Timestamp() {
        value = generator.incrementAndGet();
    }

    public Timestamp(long messageId) {
        value = messageId;
    }

    public long asLong() {
        return value;
    }

    @Override
    public int compareTo(Timestamp arg0) {
        return ((Long) value).compareTo(arg0.value);
    }
}
