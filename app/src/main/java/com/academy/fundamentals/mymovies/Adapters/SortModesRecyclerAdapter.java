package com.academy.fundamentals.mymovies.Adapters;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academy.fundamentals.mymovies.Models.SortMode;
import com.academy.fundamentals.mymovies.Models.SortType;
import com.academy.fundamentals.mymovies.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SortModesRecyclerAdapter extends RecyclerView.Adapter<SortModesRecyclerAdapter.SortModesRecyclerAdapterViewHolder> {
    private static int DEFAULT_SORT_MODE = 0;

    private final SortModesAdapterOnClickHandler mClickHandler;
    private List<SortMode> sortModes = new ArrayList<>();
    private TextView currentSortModeNameTV;
    private ImageView currentSortingDirectionIV;
    private int currentSortModePos = DEFAULT_SORT_MODE;
    private boolean currentAscendSort;

    public interface SortModesAdapterOnClickHandler {
        void onSortModeClick(SortType sortType, boolean ascendingSort);
    }

    public SortModesRecyclerAdapter(SortModesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

        sortModes.add(new SortMode("Name", SortType.NAME, true));
        sortModes.add(new SortMode("Release date", SortType.RELEASE_DATE, false));
        sortModes.add(new SortMode("Rating", SortType.RATING, false));
        sortModes.add(new SortMode("Popularity", SortType.POPULARITY, false));

        currentAscendSort = sortModes.get(DEFAULT_SORT_MODE).isDefaultAscendSort();
    }

    @NonNull
    @Override
    public SortModesRecyclerAdapterViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        int layoutId = R.layout.recycler_item_sort_mode;

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(layoutId, parent, false);

        return new SortModesRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortModesRecyclerAdapterViewHolder sortModesRecyclerAdapterViewHolder, int position) {
        sortModesRecyclerAdapterViewHolder.bind(sortModes.get(position));
    }

    @Override
    public int getItemCount() {
        return sortModes.size();
    }

    class SortModesRecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.sortModeNameTV) TextView sortModeNameTV;
        @BindView(R.id.sortingDirectionIV) ImageView sortingDirectionIV;

        private SortModesRecyclerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(final SortMode sortMode) {
            sortModeNameTV.setText(sortMode.getName());

            if (currentSortModePos == getAdapterPosition()) {
                sortModeNameTV.setPaintFlags(sortModeNameTV.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
                sortingDirectionIV.setVisibility(View.VISIBLE);

                currentSortModeNameTV = sortModeNameTV;
                currentSortingDirectionIV = sortingDirectionIV;

                if (currentAscendSort)
                    sortingDirectionIV.setImageResource(R.drawable.ic_sort_ascending);
                else
                    sortingDirectionIV.setImageResource(R.drawable.ic_sort_descending);
            }
            else {
                sortModeNameTV.setPaintFlags(0);  //Remove underline
                sortingDirectionIV.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedSortModePos = getAdapterPosition();

            if (currentSortModePos == clickedSortModePos) {
                if (currentAscendSort) {
                    currentAscendSort = false;
                    sortingDirectionIV.setImageResource(R.drawable.ic_sort_descending);
                }
                else {
                    currentAscendSort = true;
                    sortingDirectionIV.setImageResource(R.drawable.ic_sort_ascending);
                }
            }
            else {
                currentSortModeNameTV.setPaintFlags(0);
                sortModeNameTV.setPaintFlags(sortModeNameTV.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);

                currentSortingDirectionIV.setVisibility(View.INVISIBLE);
                sortingDirectionIV.setVisibility(View.VISIBLE);

                currentSortModeNameTV = sortModeNameTV;
                currentSortingDirectionIV = sortingDirectionIV;
                currentSortModePos = clickedSortModePos;

                if (sortModes.get(clickedSortModePos).isDefaultAscendSort()) {
                    currentAscendSort = true;
                    sortingDirectionIV.setImageResource(R.drawable.ic_sort_ascending);
                }
                else {
                    currentAscendSort = false;
                    sortingDirectionIV.setImageResource(R.drawable.ic_sort_descending);
                }
            }

            SortType sortType = sortModes.get(clickedSortModePos).getType();
            mClickHandler.onSortModeClick(sortType, currentAscendSort);
        }
    }
}