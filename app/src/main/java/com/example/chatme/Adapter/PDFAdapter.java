package com.example.chatme.Adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatme.messagesModel;
import com.example.chatme.modelDoc;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatme.OnItemClickListener;
import com.example.chatme.R;
import com.example.chatme.pdfViewer;

import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.PDFViewHolder> {

 List<modelDoc> pdfNamesList;
    Context context;



    public PDFAdapter(List<modelDoc> pdfNamesList, Context context) {
        this.pdfNamesList = pdfNamesList;
        this.context=context;
    }
    @NonNull
    @Override
    public PDFAdapter.PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.documentationcard, parent, false);
        return new PDFViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PDFAdapter.PDFViewHolder holder, int position) {
       modelDoc model=pdfNamesList.get(position);
        String pdfName = model.getFilename();
        holder.pdfNameTextView.setText(pdfName);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, pdfName, Toast.LENGTH_SHORT).show();
//                Intent i1=new Intent(context, pdfViewer.class);
//               // i1.putExtra();
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return pdfNamesList.size();
    }
    public static class PDFViewHolder extends RecyclerView.ViewHolder {
        TextView pdfNameTextView;
        private OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
        public PDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfNameTextView = itemView.findViewById(R.id.pdfNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            OnItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
    }
}}
