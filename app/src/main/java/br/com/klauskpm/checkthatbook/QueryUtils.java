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
    public static int connectTimeout = 15000;
    public static int readTimeout = 10000;
    public static String charset = "UTF-8";

    private static final String GET = "GET";

    public static String request(String stringUrl, String method) {
        URL url = createUrl(stringUrl);
        String response = null;

        try {
            response = makeHttpRequest(url, GET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String requestGet(String stringUrl) {
        return request(stringUrl, GET);
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String makeHttpRequest(URL url, String method) throws IOException {
        if (url == null) {
            return null;
        }

        String response = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setConnectTimeout(connectTimeout);
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

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream,
                    Charset.forName(charset));
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
