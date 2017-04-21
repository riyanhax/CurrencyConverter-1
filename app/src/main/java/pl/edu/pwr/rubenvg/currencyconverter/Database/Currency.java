package pl.edu.pwr.rubenvg.currencyconverter.Database;

/**
 * Created by Rubén on 17/04/2017.
 */

public class Currency {

    private String id;
    private String code;
    private String name;
    private double rate;
    private String country;
    private String region;
    private String subUnit;
    private String symbol;
    private String description;

    public Currency() {
    }

    public Currency(String id, String code, String name, double rate, String country, String region, String subUnit, String symbol, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.country = country;
        this.region = region;
        this.subUnit = subUnit;
        this.symbol = symbol;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubUnit() {
        return subUnit;
    }

    public void setSubUnit(String subUnit) {
        this.subUnit = subUnit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
