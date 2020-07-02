package bkp.com.todo.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bkp.com.todo.Interface.ItemClickListener;
import bkp.com.todo.R;

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView txtNoteTag, txtNoteDetail, txtNoteTime;
    public ItemClickListener listener;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        txtNoteTag = (TextView)itemView.findViewById(R.id.notes_tag_nil);
        txtNoteDetail = (TextView)itemView.findViewById(R.id.notes_detail_nil);
        txtNoteTime = (TextView)itemView.findViewById(R.id.notes_time_nil);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
