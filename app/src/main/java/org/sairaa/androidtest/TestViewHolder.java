package org.sairaa.androidtest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestViewHolder extends RecyclerView.ViewHolder {
    private TextView testS;
    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
        testS = itemView.findViewById(R.id.textView2);
    }

    public void set(String testString) {
        testS.setText(testString);
    }
}
