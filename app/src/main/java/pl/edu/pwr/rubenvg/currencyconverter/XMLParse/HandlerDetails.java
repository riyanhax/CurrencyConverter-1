package pl.edu.pwr.rubenvg.currencyconverter.XMLParse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import pl.edu.pwr.rubenvg.currencyconverter.Database.Currency;


public class HandlerDetails extends DefaultHandler {

    private StringBuilder buffer = new StringBuilder();
    private ArrayList<Currency> currencies;
    private Currency currency;

    @Override
    public void startDocument() throws SAXException {

        super.startDocument();
        currencies = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName){
            case "ITEM":
                currency = new Currency();
                currencies.add(currency);
                currency.setName(attributes.getValue("name"));
                currency.setCode(attributes.getValue("code"));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName){
            case "Country":
                currency.setCountry(buffer.toString());
                break;
            case "Rate":
                currency.setRate(Double.parseDouble(String.valueOf(buffer)));
                break;
            case "Region":
                currency.setRegion(buffer.toString());
                break;
            case "Subunit":
                currency.setSubUnit(buffer.toString());
                break;
            case "Symbol":
                currency.setSymbol(buffer.toString());
                break;
            case "Description":
                currency.setDescription(buffer.toString());
                break;
        }
        buffer.delete(0, buffer.length());
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

}
