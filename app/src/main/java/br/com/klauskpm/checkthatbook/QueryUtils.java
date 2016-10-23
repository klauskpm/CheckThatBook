package br.com.klauskpm.checkthatbook;

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
    protected static int CONNECT_TIMEOUT = 15000;
    protected static int READ_TIMEOUT = 10000;
    protected static String CHARSET = "UTF-8";

    private static final String GET = "GET";

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

    protected static String requestGet(String stringUrl) {
        return request(stringUrl, GET);
    }

    protected static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

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
                Log.d(TAG, "Error response code " + urlConnection.getResponseCode());
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
