package br.com.klauskpm.checkthatbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Kazlauskas on 22/10/2016.
 */
public class Book {
    private String mThumbnailLink = null;
    private String mTitle;
    private ArrayList<String> mAuthors = new ArrayList<String>();
    private Double mPrice = null;
    private Double mPreviousPrice = null;
    private String mBuyLink = null;
    private String mInfoLink;
    private String mCurrencyCode;

    private String mFormattedPrice = null;
    private String mFormattedPreviousPrice = null;

    private final static String BRL_CURRENCY_CODE = "BRL";

    /**
     * Instantiates a new Book.
     *
     * @param mThumbnailLink the thumbnail link
     * @param mTitle         the title
     * @param mAuthors       the authors
     * @param mPrice         the price
     * @param mPreviousPrice the previous price
     * @param mBuyLink       the buy link
     * @param mInfoLink      the info link
     * @param mCurrencyCode  the currency code
     */
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

        if (mPrice != null) {
            String currencySymbol = getCurrencySymbol();
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMinimumFractionDigits(2);

            mFormattedPrice = String.format("%s%s", currencySymbol, decimalFormat.format(mPrice));

            if (mPreviousPrice != null)
                mFormattedPreviousPrice = String.format("%s%s", currencySymbol,
                        decimalFormat.format(mPreviousPrice));
        }
    }

    /**
     * Gets thumbnail link.
     *
     * @return the thumbnail link
     */
    public String getThumbnailLink() {
        return mThumbnailLink;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Gets authors.
     *
     * @return the authors
     */
    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public Double getPrice() {
        return mPrice;
    }

    /**
     * Gets previous price.
     *
     * @return the previous price
     */
    public Double getPreviousPrice() {
        return mPreviousPrice;
    }

    /**
     * Gets buy link.
     *
     * @return the buy link
     */
    public String getBuyLink() {
        return mBuyLink;
    }

    /**
     * Gets info link.
     *
     * @return the info link
     */
    public String getInfoLink() {
        return mInfoLink;
    }

    /**
     * Gets currency code.
     *
     * @return the currency code
     */
    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    /**
     * Gets currency symbol.
     *
     * @return the currency symbol
     */
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

    /**
     * Gets formatted price.
     *
     * @return the formatted price
     */
    public String getFormattedPrice() {
        return mFormattedPrice;
    }

    /**
     * Gets formatted previous price.
     *
     * @return the formatted previous price
     */
    public String getFormattedPreviousPrice() {
        return mFormattedPreviousPrice;
    }

    /**
     * Extract from json book.
     *
     * @param bookJSON the book json
     * @return the book
     * @throws JSONException the json exception
     */
    public static Book extractFromJSON(JSONObject bookJSON) throws JSONException {
        // Volume info
        JSONObject volumeInfo = bookJSON.getJSONObject("volumeInfo");

        JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
        String thumbnailLink = null;
        if (imageLinks != null)
            thumbnailLink = imageLinks.getString("smallThumbnail");

        String title = volumeInfo.getString("title");

        JSONArray authorsArray = volumeInfo.optJSONArray("authors");
        ArrayList<String> authors = new ArrayList<String>();
        if (authorsArray != null) {
            for (int i = 0; i < authorsArray.length(); i++) {
                authors.add(authorsArray.getString(i));
            }
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
