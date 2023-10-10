package com.example.chatme.Adapter;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
    private OnItemClickListener mItemListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public PDFAdapter(List<modelDoc> pdfNamesList, Context context, OnItemClickListener mListener) {
        this.pdfNamesList = pdfNamesList;
        this.context=context;
        this.mItemListener=mListener;
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
        holder.itemView.setOnClickListener(view -> {
            mItemListener.onItemClick(position);//this will gwt the postiotion of item in rv
        });

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemListener = listener;
    }

    public static class PDFViewHolder extends RecyclerView.ViewHolder {
        TextView pdfNameTextView;
        private OnItemClickListener mlistener;
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mlistener = listener;
        }
        public PDFViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfNameTextView = itemView.findViewById(R.id.pdfNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        Log.d("rv","clicked");
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });
    }
}}
