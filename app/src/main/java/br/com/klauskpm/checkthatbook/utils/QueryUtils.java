package br.com.klauskpm.checkthatbook.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static android.content.ContentValues.TAG;

/**
 * Created by Kazlauskas on 22/10/2016.
 */
public class QueryUtils {
    /**
     * The constant CONNECT_TIMEOUT.
     */
    protected static int CONNECT_TIMEOUT = 15000;
    /**
     * The constant READ_TIMEOUT.
     */
    protected static int READ_TIMEOUT = 10000;
    /**
     * The constant CHARSET.
     */
    protected static String CHARSET = "UTF-8";

    private static final String GET = "GET";

    /**
     * Request string.
     *
     * @param stringUrl the string url
     * @param method    the method
     * @return the string
     */
    protected static String request(String stringUrl, String method) {
        URL url = createUrl(stringUrl);
        String response = null;

        try {
            response = makeHttpRequest(url, GET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Request get string.
     *
     * @param stringUrl the string url
     * @return the string
     */
    protected static String requestGet(String stringUrl) {
        return request(stringUrl, GET);
    }

    /**
     * Create url url.
     *
     * @param stringUrl the string url
     * @return the url
     */
    protected static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * Make http request string.
     *
     * @param url    the url
     * @param method the method
     * @return the string
     * @throws IOException the io exception
     */
    protected static String makeHttpRequest(URL url, String method) throws IOException {
        if (url == null) {
            return null;
        }

        String response = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(method);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (inputStream != null)
                inputStream.close();
        }

        return response;
    }

    /**
     * Read from stream string.
     *
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    protected static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream,
                    Charset.forName(CHARSET));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}
