package be.winagent.weba2.domain.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED(false), STARTED(false), CANCELLED(true), COMPLETED(true);
    private final boolean complete;

    OrderStatus(boolean complete) {
        this.complete = complete;
    }
}
