package com.example.travel;

import android.content.Context;
import android.sax.Element;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Travel {
    private List<Destination> destinations = new ArrayList<Destination>();
    public List<BoardingCard> boardingCards = new ArrayList<BoardingCard>();

    public Travel(Context context) throws IOException, SAXException {
        InputStream is;
        is = new BufferedInputStream(context.getAssets().open("cards.xml"));
        try {
            parse(is);
        } finally {
            is.close();
        }
    }

    public void parse(InputStream is) throws IOException, SAXException {
        RootElement root = new RootElement("travel");
        Element destinationsElement = root.getChild("destinations");
        Element destinationElement = destinationsElement.getChild("destination");
        Element boardingCardsElement = root.getChild("boardingcards");
        Element boardingCardElement = boardingCardsElement.getChild("boardingcard");

        destinationElement.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                Destination destination = new Destination();
                destination.id = Integer.parseInt(attributes.getValue("id"));
                destination.name = attributes.getValue("name");
                destinations.add(destination);
            }
        });

        boardingCardElement.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                if (attributes.getValue("type").equals("flight")) {
                    BoardingCardFlight boardingCard = new BoardingCardFlight();
                    boardingCard.baggage = attributes.getValue("baggage");
                    boardingCard.gate = attributes.getValue("gate");
                    boardingCardCommon(boardingCard, attributes);
                } else {
                    BoardingCard boardingCard = new BoardingCard();
                    boardingCardCommon(boardingCard, attributes);
                }
            }

            private void boardingCardCommon(BoardingCard boardingCard, Attributes attributes) {
                boardingCard.type = attributes.getValue("type");
                boardingCard.id = attributes.getValue("id");
                for (Destination destination : destinations) {
                    if (destination.id == Integer.parseInt(attributes.getValue("start"))) {
                        boardingCard.start = destination;
                    }
                }
                for (Destination destination : destinations) {
                    if (destination.id == Integer.parseInt(attributes.getValue("end"))) {
                        boardingCard.end = destination;
                    }
                }
                boardingCard.seat = attributes.getValue("seat");
                boardingCards.add(boardingCard);
            }
        });

        Xml.parse(is, Xml.Encoding.UTF_8, root.getContentHandler());
    }

}
