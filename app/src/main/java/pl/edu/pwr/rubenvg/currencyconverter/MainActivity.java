package pl.edu.pwr.rubenvg.currencyconverter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.edu.pwr.rubenvg.currencyconverter.Database.Currency;
import pl.edu.pwr.rubenvg.currencyconverter.Database.DBAdapter;
import pl.edu.pwr.rubenvg.currencyconverter.MasterDetail.ContentList;
import pl.edu.pwr.rubenvg.currencyconverter.MasterDetail.ItemListActivity;
import pl.edu.pwr.rubenvg.currencyconverter.XMLParse.ParserSax;

public class MainActivity extends Activity {

    private Spinner spin1;
    private Spinner spin2;
    private TextView currencyDisplay;
    private EditText currInput;
    private TextView lastUpdate;
    private ImageView graphView;
    private DBAdapter db;
    private Cursor c;
    private ImageButton imgBtnRefresh;
    private Button btnCurrList;
    private ImageButton imBtnzoom;
    private ImageButton imBtnexchangeSpinners;
    private LoadXml task;
    private List<Currency> currencies;
    private double[] currConversion = new double[2];
    private String currencyCode1;
    private String currencyCode2;
    private SharedPreferences prefs;
    private ArrayList<Currency> arrayListCurrency;
    private int currencySelected1;
    private int currencySelected2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentList.setContext(this);
        arrayListCurrency = new ArrayList<>();
        setContentView(R.layout.activity_main);

        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        imBtnexchangeSpinners = (ImageButton) findViewById(R.id.exgangeImBtn);
        currencyDisplay = (TextView) findViewById(R.id.lastUpdateTv);
        imgBtnRefresh = (ImageButton) findViewById(R.id.ImgBtnRefresh);
        currInput = (EditText) findViewById(R.id.currencyInput);
        currencyDisplay = (TextView) findViewById(R.id.conversionDisplay);
        graphView = (ImageView) findViewById(R.id.graphImVw);
        imBtnzoom = (ImageButton) findViewById(R.id.zoomImBtn);
        lastUpdate = (TextView) findViewById(R.id.lastUpdateTv);
        btnCurrList = (Button) findViewById(R.id.btnList);

        prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String lastUp = prefs.getString("lastUpdate", "IMPORTANT! Update the currency rates");
        currencySelected1 = prefs.getInt("currencySelected1", 0);
        currencySelected2 = prefs.getInt("currencySelected2", 0);
        lastUpdate.setText(lastUp);
        //Check if the database exist, if not, call method copyDB
        try {
            String destPath = "/data/data/" + getPackageName()
                    + "/databases/MyDB";
            File f = new File(destPath);
            if (!f.exists()) {
                copyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db = new DBAdapter(this);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyCode1 = arrayListCurrency.get(position).getCode();
                db.open();
                c = db.getRate(currencyCode1);
                currConversion[0] = Double.parseDouble(c.getString(0));
                currencyDisplay.setText(convert(currInput.getText().toString()));
                db.close();
                prefs.edit().putInt("currencySelected1", position).commit();
                showImage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyCode2 = arrayListCurrency.get(position).getCode();
                db.open();
                c = db.getRate(currencyCode2);
                currConversion[1] = Double.parseDouble(c.getString(0));
                currencyDisplay.setText(convert(currInput.getText().toString()));
                db.close();
                prefs.edit().putInt("currencySelected2", position).commit();
                showImage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currInput.addTextChangedListener(txtWatcher);

        imBtnexchangeSpinners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                int pos1 = spin1.getSelectedItemPosition();
                int pos2 = spin2.getSelectedItemPosition();
                spin1.setSelection(pos2);
                spin2.setSelection(pos1);
                imBtnexchangeSpinners.startAnimation(animation);
            }

        });

        imgBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new LoadXml();
                task.execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            }
        });

        imBtnzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                intent.putExtra("currency1",currencyCode1);
                intent.putExtra("currency2",currencyCode2);
                startActivity(intent);
            }
        });

        btnCurrList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
                startActivity(intent);
            }
        });

        getAllCurrencies();
    }

    public void showImage(){
        //It has no sense to show the plot if both currencies are the same (Plot is a line)
        if(!currencyCode1.equals(currencyCode2)) {
            graphView.setVisibility(View.VISIBLE);
            imBtnzoom.setVisibility(View.VISIBLE);
            Glide.with(this).load("http://themoneyconverter.com/exchange-rate-chart/" + currencyCode1 + "/" + currencyCode1 + "-" + currencyCode2 + ".gif")
                    .into(graphView);
            return;
        }
        imBtnzoom.setVisibility(View.INVISIBLE);
        graphView.setVisibility(View.INVISIBLE);
    }

    /**
     * On firts run try to download the new currency rates from the Internet
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            imgBtnRefresh.performClick();
            prefs.edit().putBoolean("firstrun", false).commit();
        }

    }

    private final TextWatcher txtWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            currInput.removeTextChangedListener(txtWatcher);
            currencyDisplay.setText(convert(currInput.getText().toString()));
            //currencyDisplay.setText(currInput.getText().toString());
            currInput.addTextChangedListener(txtWatcher);
        }

        public void afterTextChanged(Editable s) {
            currInput.setSelection(s.length());
        }
    };

    private String convert(String valueToConvert) {
        if (valueToConvert.equals("") || valueToConvert.equals(".")) {
            return "";
        } else {
            Double result = Double.parseDouble(valueToConvert) * currConversion[1];
            DecimalFormat df=new DecimalFormat("#.###");
            result = result/currConversion[0];
            return df.format(result)+" "+currencyCode2;

        }
    }

    public void AddCurrencySpinner(Cursor c) {
        Currency curr = new Currency();
        curr.setCode(c.getString(0));
        curr.setName(c.getString(1));
        arrayListCurrency.add(curr);
    }

    public void copyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * This method just access to the db to add the elements on the spinners
     */
    public void getAllCurrencies() {
        db.open();
        c = db.getCurrenciesItemsSpinners();
        if (c.moveToFirst()) {
            do {
                AddCurrencySpinner(c);
            } while (c.moveToNext());
        }
        db.close();
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item,R.id.txt1,arrayListCurrency);
        spin1.setAdapter(adapter);
        spin2.setAdapter(adapter);
        spin1.setSelection(currencySelected1);
        spin2.setSelection(currencySelected2);
    }

    public class LoadXml extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            ParserSax saxparser =
                    new ParserSax(params[0]);
            try {
                currencies = saxparser.parse();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    sdf.format(new Date());
                    SharedPreferences.Editor editor = prefs.edit();
                    String lastUp = "Update from ECB (" + sdf.format(new Date()) + ").";
                    editor.putString("lastUpdate", lastUp);
                    lastUpdate.setText(lastUp);
                    db.open();
                    for (int k = 0; k < currencies.size(); k++) {
                        Currency currency = currencies.get(k);
                        db.updateRate(currency.getCode(), currency.getRate());
                        editor.commit();
                    }
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
