package tv.emby.embyatv.livetv;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import mediabrowser.model.livetv.ProgramInfoDto;
import tv.emby.embyatv.TvApp;
import tv.emby.embyatv.ui.ProgramGridCell;
import tv.emby.embyatv.util.Utils;

/**
 * Created by Eric on 5/5/2015.
 */
public class ProgramListAdapter extends BaseAdapter {

    LiveTvGuideActivity activity;
    LinearLayout view;
    List<List<ProgramInfoDto>> rows = new ArrayList<>();

    public ProgramListAdapter(LiveTvGuideActivity activity, LinearLayout view) {
        this.activity = activity;
        this.view = view;
    }

    public void addRow(int ndx, List<ProgramInfoDto> programs) {
        rows.add(ndx, programs);
        view.addView(getView(ndx, null, view));
        TvApp.getApplication().getLogger().Debug("Added "+programs.size()+ " programs at pos "+ndx);
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = new LinearLayout(activity);

        LinearLayout programRow = (LinearLayout) convertView;
        programRow.removeAllViews();

        for (ProgramInfoDto item : rows.get(position)) {
            long start = item.getStartDate() != null ? Utils.convertToLocalDate(item.getStartDate()).getTime() : activity.getCurrentLocalStartDate();
            if (start < activity.getCurrentLocalStartDate()) start = activity.getCurrentLocalStartDate();
            long end = item.getEndDate() != null ? Utils.convertToLocalDate(item.getEndDate()).getTime() : activity.getCurrentLocalEndDate();
            if (end > activity.getCurrentLocalEndDate()) end = activity.getCurrentLocalEndDate();
            Long duration = (end - start) / 60000;
            //TvApp.getApplication().getLogger().Debug("Duration for "+item.getName()+" is "+duration.intValue());
            if (duration > 0) {
                ProgramGridCell program = new ProgramGridCell(activity, item);
                program.setLayoutParams(new ViewGroup.LayoutParams(duration.intValue() * activity.PIXELS_PER_MINUTE, activity.ROW_HEIGHT));
                program.setFocusable(true);

                programRow.addView(program);

            }

        }

        return convertView;
    }
}