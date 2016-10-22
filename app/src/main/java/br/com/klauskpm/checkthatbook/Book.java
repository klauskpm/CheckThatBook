package br.com.klauskpm.checkthatbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kazlauskas on 22/10/2016.
 */

public class Book {
    private String mThumbnailLink;
    private String mTitle;
    private ArrayList<String> mAuthors;
    private Double mPrice;
    private Double mPreviousPrice;
    private String mBuyLink;
    private String mInfoLink;
    private String mCurrencyCode;

    private final static String BRL_CURRENCY_CODE = "BRL";

    public Book(String mThumbnailLink, String mTitle, ArrayList<String> mAuthors, Double mPrice,
                Double mPreviousPrice, String mBuyLink, String mInfoLink, String mCurrencyCode) {
        this.mThumbnailLink = mThumbnailLink;
        this.mTitle = mTitle.toUpperCase();
        this.mAuthors = mAuthors;
        this.mPrice = mPrice;
        this.mPreviousPrice = mPreviousPrice;
        this.mBuyLink = mBuyLink;
        this.mInfoLink = mInfoLink;
        this.mCurrencyCode = mCurrencyCode;
    }

    public String getThumbnailLink() {
        return mThumbnailLink;
    }

    public String getTitle() {
        return mTitle;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public Double getPrice() {
        return mPrice;
    }

    public Double getPreviousPrice() {
        return mPreviousPrice;
    }

    public String getBuyLink() {
        return mBuyLink;
    }

    public String getInfoLink() {
        return mInfoLink;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    public String getCurrencySymbol() {
        String symbol;
        switch (mCurrencyCode) {
            case BRL_CURRENCY_CODE:
                symbol = "R$";
                break;

            default:
                symbol = "$";
                break;
        }

        return symbol;
    }

    public static Book extractFromJSON(JSONObject bookJSON) throws JSONException {
        // Volume info
        JSONObject volumeInfo = bookJSON.getJSONObject("volumeInfo");
        String thumbnailLink = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail");
        String title = volumeInfo.getString("title");
        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
        ArrayList<String> authors = new ArrayList<String>();
        for (int i = 0; i < authorsArray.length(); i++) {
           authors.add(authorsArray.getString(i));
        }
        String infoLink = volumeInfo.getString("infoLink");

        // Sale info
        JSONObject saleInfo = bookJSON.getJSONObject("saleInfo");
        JSONObject listPrice = saleInfo.optJSONObject("listPrice");

        Double price = null;
        Double previousPrice = null;
        String buyLink = null;
        String currencyCode = null;

        if (listPrice != null) {
            price = listPrice.getDouble("amount");
            previousPrice = saleInfo.optJSONObject("retailPrice").getDouble("amount");
            buyLink = saleInfo.getString("buyLink");
            currencyCode = listPrice.getString("currencyCode");
        }

        return new Book(thumbnailLink, title, authors, price, previousPrice, buyLink, infoLink,
               currencyCode);
    }
}
