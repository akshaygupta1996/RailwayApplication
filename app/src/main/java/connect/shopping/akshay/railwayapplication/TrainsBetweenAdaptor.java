package connect.shopping.akshay.railwayapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Akshay on 18-04-2017.
 */

public class TrainsBetweenAdaptor extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<TrainsBetween> trainsList;

    public TrainsBetweenAdaptor(Activity activity, List<TrainsBetween> trainsList) {
        this.activity = activity;
        this.trainsList = trainsList;
    }

    @Override
    public int getCount() {
        return trainsList.size();
    }

    @Override
    public Object getItem(int i) {
        return trainsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.trains_between_layout, null);

        TextView trainName = (TextView)convertView.findViewById(R.id.trainName);
        TextView trainNo = (TextView)convertView.findViewById(R.id.trainNumber);
        TextView travelTime = (TextView)convertView.findViewById(R.id.travelTime);
        TextView arrivalTime = (TextView)convertView.findViewById(R.id.arrivalTime);
        TextView departureTime = (TextView)convertView.findViewById(R.id.departureTime);

        TrainsBetween trainsBetween = trainsList.get(i);

        Log.d("TRAIN",trainsBetween.getTrain_name()+ " "+trainsBetween.getTrain_no());

        trainName.setText(trainsBetween.getTrain_name());
        trainNo.setText(trainsBetween.getTrain_no()+"");
        travelTime.setText(trainsBetween.getTravel_time());
        arrivalTime.setText(trainsBetween.getArrival_time());
        departureTime.setText(trainsBetween.getDeparture_time());




        return convertView;
    }
}
