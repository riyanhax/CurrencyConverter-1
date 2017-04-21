package pl.edu.pwr.rubenvg.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;



import com.bumptech.glide.Glide;

public class GraphActivity extends AppCompatActivity {

    private ImageView graphView;
    private String currencyCode1;
    private String currencyCode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view);
        graphView = (ImageView) findViewById(R.id.graphView);
        Bundle mBundle = getIntent().getExtras();
        currencyCode1 = mBundle.getString("currency1");
        currencyCode2 = mBundle.getString("currency2");
        getSupportActionBar().setTitle("Exchange rate history of "+currencyCode1+" against "+currencyCode2);
        Glide.with(this).load("http://themoneyconverter.com/exchange-rate-chart/" + currencyCode1 + "/" + currencyCode1 + "-" + currencyCode2 + ".gif")
                .into(graphView);
    }
}
