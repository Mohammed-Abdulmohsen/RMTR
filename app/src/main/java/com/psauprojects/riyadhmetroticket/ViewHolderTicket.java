package com.psauprojects.riyadhmetroticket;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderTicket extends RecyclerView.ViewHolder {
    CardView cardView;
    TextView textView_ticketid,textView_passid,textView_ticket_type,textView_trip_type,textView_dep_time;
    TextView textView_from,textView_to,textView_dep_date,textView_ret_date;
    TextView textView_a,textView_c,textView_i,textView_seats;

    public ViewHolderTicket(@NonNull View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.ticket_card);
        textView_ticketid = (TextView) itemView.findViewById(R.id.textView_ticketid);
        textView_passid = (TextView) itemView.findViewById(R.id.textView_passid);
        textView_ticket_type = (TextView) itemView.findViewById(R.id.textView_ticket_type);
        textView_trip_type = (TextView) itemView.findViewById(R.id.textView_trip_type);
        textView_dep_time = (TextView) itemView.findViewById(R.id.textView_dep_time);
        textView_from = (TextView) itemView.findViewById(R.id.textView_from);
        textView_to =(TextView) itemView.findViewById(R.id.textView_to);
        textView_dep_date = (TextView) itemView.findViewById(R.id.textView_dep_date);
        textView_ret_date =(TextView) itemView.findViewById(R.id.textView_ret_date);
        textView_a = (TextView) itemView.findViewById(R.id.textView_a);
        textView_c = (TextView) itemView.findViewById(R.id.textView_c);
        textView_i = (TextView) itemView.findViewById(R.id.textView_i);
        textView_seats = (TextView) itemView.findViewById(R.id.textView_seats);
    }
}
