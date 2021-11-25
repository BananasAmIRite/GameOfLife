package org.BananasAmIRite;

public class LitResponse {
    private final boolean response;
    private final Coordinate coordinates;

    public LitResponse(boolean response, Coordinate coordinates) {
        this.response = response;
        this.coordinates = coordinates;
    }

    public final boolean getResponse() {
        return this.response;
    }

    public final Coordinate getCoordinates() {
        return this.coordinates;
    }
}
