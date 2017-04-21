package pl.edu.pwr.rubenvg.currencyconverter.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import pl.edu.pwr.rubenvg.currencyconverter.XMLParse.HandlerDetails;

/**
 * Created by Rubén on 16/04/2017.
 */

public class DBAdapter {

    private static final String TAG = "DBCurrency";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dataBase.db";
    //Currency table definition
    private static final String TABLE_CURRENCIES = "CURRENCIES";

    public static final class Currencies implements BaseColumns {
        private Currencies() {
        }

        public static final String CODE = "code";
        public static final String NAME = "name";
        public static final String RATE = "rate";
        public static final String COUNTRY = "country";
        public static final String REGION = "region";
        public static final String SUBUNIT = "subunit";
        private static final String SYMBOL = "symbol";
        public static final String DESCIPTION = "desciption";
    }

    //Create Table
    private static final String CURRENCIES_TABLE_CREATE = "CREATE TABLE " + TABLE_CURRENCIES + " ("
            + Currencies.CODE + " TEXT PRIMARY KEY, " +
            Currencies.NAME + " TEXT NOT NULL, " +
            Currencies.RATE + " DOUBLE NOT NULL, " +
            Currencies.COUNTRY + " TEXT, " +
            Currencies.SYMBOL + " TEXT, " +
            Currencies.REGION + " TEXT, " +
            Currencies.SUBUNIT + " TEXT, " +
            Currencies.DESCIPTION + " TEXT);";

    private Context context;
    private SQLiteDatabase db;
    private DBHandler openHelper; //db gestor

    public DBAdapter(Context context) {
        this.context = context;
        this.openHelper = new DBHandler(this.context);
    }

    public DBAdapter open() {
        this.db = openHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.db.close();
    }

    private static class DBHandler extends SQLiteOpenHelper {

        private Context ctx;
        private ArrayList<Currency> currencies;

        DBHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            ctx = context;
            currencies = new ArrayList<Currency>();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = saxParserFactory.newSAXParser();
                HandlerDetails handlerDetails = new HandlerDetails();
                try {
                    saxParser.parse(ctx.getAssets().open("details.xml"), handlerDetails);
                    currencies = handlerDetails.getCurrencies();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            try {
                db.execSQL(CURRENCIES_TABLE_CREATE);
                for (int k = 0; k < currencies.size(); k++) {
                    Currency currency = currencies.get(k);
                    db.execSQL("INSERT INTO " + TABLE_CURRENCIES + " VALUES ('" + currency.getCode() + "', '" + currency.getName() + "', '" + currency.getRate() + "', " +
                            "'" + currency.getCountry() + "', '" + currency.getSymbol() + "','" + currency.getRegion() + "', " +
                            "'" + currency.getSubUnit() + "', '" + currency.getDescription() + "');");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCIES);
            onCreate(db);
        }
    }

    public Cursor getCurrenciesItemsSpinners() {
        return db.query(TABLE_CURRENCIES, new String[]{Currencies.CODE, Currencies.NAME}, null, null, null, null, null);
    }

    public Cursor getCurrencies() {
        return db.query(TABLE_CURRENCIES, new String[]{Currencies.CODE, Currencies.NAME, Currencies.RATE,
                Currencies.COUNTRY,
                Currencies.SYMBOL,
                Currencies.REGION,
                Currencies.SUBUNIT,
                Currencies.DESCIPTION}, null, null, null, null, null);
    }

    public Cursor getRate(String code) throws SQLException {
        Cursor mCursor = db.rawQuery("SELECT "+Currencies.RATE+" FROM "+TABLE_CURRENCIES+" WHERE "+Currencies.CODE+"='"+code+"'",null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateRate(String code, double rate) {
        ContentValues args = new ContentValues();
        args.put(Currencies.RATE, rate);
        return db.update(TABLE_CURRENCIES, args, ""+Currencies.CODE+"='"+code+"'", null) > 0;
    }


}
