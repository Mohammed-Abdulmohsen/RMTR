package com.psauprojects.riyadhmetroticket;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterTickets extends RecyclerView.Adapter<ViewHolderTicket>  {
    List<Ticket> list = Collections.emptyList();
    Context context;

    public RecyclerViewAdapterTickets(List<Ticket> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderTicket onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pass_tickets_card,parent,false);
        ViewHolderTicket viewHolder = new ViewHolderTicket(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderTicket holder, final int position) {
        holder.textView_ticketid.setText(String.valueOf(list.get(position).getTicketid()));
        holder.textView_passid.setText(String.valueOf(list.get(position).getPassid()));
        holder.textView_ticket_type.setText(list.get(position).getClass_type());
        if(list.get(position).getJournyType() == 0 ){
            holder.textView_trip_type.setText("One Way");
        }else{
            holder.textView_trip_type.setText("RoundTrip");
        }
        holder.textView_dep_time.setText(list.get(position).getDep_time());
        holder.textView_from.setText(list.get(position).getDep_station());
        holder.textView_to.setText(list.get(position).getArr_station());
        holder.textView_dep_date.setText(list.get(position).getDep_date());
        if(list.get(position).getRet_date().equals("")){
            holder.textView_ret_date.setText("-");
        }else{
            holder.textView_ret_date.setText(list.get(position).getRet_date());
        }
        holder.textView_a.setText(String.valueOf(list.get(position).getAdult()));
        holder.textView_c.setText(String.valueOf(list.get(position).getChild()));
        holder.textView_i.setText(String.valueOf(list.get(position).getInfant()));
        holder.textView_seats.setText(list.get(position).getSeats());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Ticket data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Ticket data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }
}
