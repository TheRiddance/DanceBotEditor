package ch.ethz.asl.dancebots.danceboteditor;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by andrin on 28.08.15.
 */
public class BeatElementAdapter extends ArrayAdapter<BeatElement> {

    private ArrayList<BeatElement> mBeatElements;
    private Toast mToast;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
    }

    public BeatElementAdapter(Context context, ArrayList<BeatElement> elems) {
        super(context, 0, elems);
        mBeatElements = elems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get beat grid element for this position
        final BeatElement elem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.beat_grid_element, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.txt_beat_grid_elem_type);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.name.setText(elem.getBeatPositionAsString());

        // Stylize list item according to type
        viewHolder.name.setBackgroundColor(elem.getColor());

        mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        final View.OnClickListener simpleClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mToast.setText("Item clicked: " + viewHolder.name.getText().toString());
                mToast.setText("Item clicked: " + elem.getBeatPositionAsString());
                mToast.show();
            }
        };

        viewHolder.name.setOnClickListener(simpleClickListener);

        convertView.setLongClickable(true);

        // Return the completed view to render on screen
        return convertView;
    }
}