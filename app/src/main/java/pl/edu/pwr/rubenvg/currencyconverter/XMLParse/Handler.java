package pl.edu.pwr.rubenvg.currencyconverter.XMLParse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import pl.edu.pwr.rubenvg.currencyconverter.Database.Currency;

public class Handler extends DefaultHandler {

    private List<Currency> currencies;
    private Currency currency;
    //private StringBuilder sbText;

    public List<Currency> getCurrencies() {
        return currencies;
    }


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        currencies = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName,
                             String name, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (localName.equals("Cube")) {
            if (attributes.getIndex("time") != -1) {
            } else if (attributes.getIndex("currency") != -1) {
                currency = new Currency();
                currency.setCode(attributes.getValue("currency"));
                currency.setRate(Double.parseDouble(attributes.getValue("rate")));
                currencies.add(currency);
            }
        }
    }


    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
    }


}
