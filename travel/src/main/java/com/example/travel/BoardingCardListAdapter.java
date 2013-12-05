package com.example.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class BoardingCardListAdapter extends ArrayAdapter<BoardingCard> {
    private final LayoutInflater mInflater;

    public BoardingCardListAdapter(Context context, List<BoardingCard> boardingCards) {
        super(context, -1, boardingCards);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView  = mInflater.inflate(R.layout.travel_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.start = (TextView) convertView.findViewById(R.id.start);
            viewHolder.end = (TextView) convertView.findViewById(R.id.end);
            viewHolder.seat = (TextView) convertView.findViewById(R.id.seat);
            viewHolder.gate = (TextView) convertView.findViewById(R.id.gate);
            viewHolder.baggage = (TextView) convertView.findViewById(R.id.baggage);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BoardingCard boardingCard = getItem(position);
        viewHolder.type.setText(boardingCard.type);
        viewHolder.start.setText(boardingCard.start.name);
        viewHolder.end.setText(boardingCard.end.name);
        viewHolder.seat.setText(boardingCard.seat);
        if (boardingCard instanceof BoardingCardFlight) {
            viewHolder.gate.setText(((BoardingCardFlight) boardingCard).gate);
            viewHolder.baggage.setText(((BoardingCardFlight) boardingCard).baggage);
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView type;
        public TextView start;
        public TextView end;
        public TextView seat;
        public TextView gate;
        public TextView baggage;
    }
}
