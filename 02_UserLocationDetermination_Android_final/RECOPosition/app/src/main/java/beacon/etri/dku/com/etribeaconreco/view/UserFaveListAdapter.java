package beacon.etri.dku.com.etribeaconreco.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import beacon.etri.dku.com.etribeaconreco.R;

/**
 * Created by user on 2016-02-17.
 */
public class UserFaveListAdapter extends BaseAdapter {

    private Context artifactCon;
    private List<UserFaveVo> artifactList;
    private int layout;
    private LayoutInflater inflater;
    CheckBox artifactCheckBox;
    private ArrayList<UserFaveVo> dataList = new ArrayList<UserFaveVo>();

    public UserFaveListAdapter() {

    }

    public UserFaveListAdapter(Context context, int layout, List<UserFaveVo> artifactList) {
        this.artifactCon = context;
        this.layout = layout;
        this.artifactList = artifactList;

        inflater = (LayoutInflater) artifactCon.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return artifactList.size();
    }

    @Override
    public Object getItem(int i) {
        return artifactList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        artifactCheckBox = (CheckBox) view.findViewById(R.id.artifactCheckBox);
        artifactCheckBox.setText(artifactList.get(i).getContentName());
        artifactCheckBox.setFocusable(false);
        artifactCheckBox.setClickable(false);

        artifactCheckBox.setChecked(artifactList.get(i).getIsSelect());

        return view;
    }
}
