package pl.edu.pwr.rubenvg.currencyconverter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.pwr.rubenvg.currencyconverter.Database.Currency;

/**
 * Created by Rubén on 21/04/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<Currency> {

    private int groupid;
    private Context context;
    private ArrayList<Currency> list;
    private LayoutInflater inflater;


    public SpinnerAdapter(Context context, int groupid, int id, ArrayList<Currency>
            list){
        super(context,id,list);
        this.context = context;
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        Resources res = context.getResources();
        Currency curr = list.get(position);
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        String flag =curr.getCode().toLowerCase() + "_flag";
        int resID = res.getIdentifier(flag, "drawable", context.getPackageName());
        Drawable drawable = res.getDrawable(resID);
        imageView.setImageDrawable(drawable);
        TextView textView=(TextView)itemView.findViewById(R.id.txt1);
        textView.setText(curr.getName()+" ("+curr.getCode()+")");
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }
}
