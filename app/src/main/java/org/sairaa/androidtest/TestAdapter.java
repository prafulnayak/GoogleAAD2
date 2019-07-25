package org.sairaa.androidtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
    private List<String> testList = new ArrayList<>();
    private Context context;

    public TestAdapter(Context mainActivity) {
        this.context = mainActivity;
    }
    public void updateList(List<String> list){
        this.testList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        String testString = testList.get(position);
        holder.set(testString);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }
}
