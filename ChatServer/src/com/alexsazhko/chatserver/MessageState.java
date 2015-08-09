package com.alexsazhko.chatserver;

public enum MessageState {
	NEW(0),
    MESSAGE(1),
    END(2);

    private final int state;

    MessageState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }
}
