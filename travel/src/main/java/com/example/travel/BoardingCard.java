package com.example.travel;

public class BoardingCard implements Comparable<BoardingCard> {
    public String id;
    public String type;
    public String seat;
    public Destination start;
    public Destination end;

    @Override
    public int compareTo(BoardingCard another) {
        if (this.end == another.start) {
            return -1;
        } else if (this.start == another.end) {
            return 1;
        } else {
            return 0;
        }
    }
}

