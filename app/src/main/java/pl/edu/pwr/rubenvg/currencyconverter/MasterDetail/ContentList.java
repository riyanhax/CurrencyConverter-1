package pl.edu.pwr.rubenvg.currencyconverter.MasterDetail;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.edu.pwr.rubenvg.currencyconverter.Database.Currency;
import pl.edu.pwr.rubenvg.currencyconverter.Database.DBAdapter;

public class ContentList {

    public static final List<Currency> ITEMS = new ArrayList<Currency>();
    public static final Map<String, Currency> ITEM_MAP = new HashMap<>();
    private static DBAdapter db;
    private static Cursor c;

    public static void setContext(Context context) {
        if (db == null) db = new DBAdapter(context);
        if (ContentList.ITEMS.size() == 0) {
            db.open();
            c = db.getCurrencies();
            if (c.moveToFirst()) {
                do {
                    Currency currency = new Currency(generateId(), c.getString(0), c.getString(1), c.getDouble(2), c.getString(3),
                            c.getString(5), c.getString(6), c.getString(4), c.getString(7));
                    ContentList.addItem(currency);
                } while (c.moveToNext());
            }
        }
    }


    private static String generateId() {
        return UUID.randomUUID().toString();
    }

    private static void addItem(Currency item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

}
