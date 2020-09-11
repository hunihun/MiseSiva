package com.example.misesiva.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * HTTP통신
 * @since 2018.08.13
 * @author 김효일
 * @version 1.0
 *
 * <pre>
 * << 개정 이력 >>
 *
 * 수정일                                        수정자                                                 수정내용
 * ----------------		---------------------			  --------------------------------------------------------
 * 2018.08.13                 김효일                                                 최초작성
 *
 * </pre>
 */
public class Curl {

    private static final String TAG = Curl.class.getSimpleName();

    public static String getInSeparateThread(String strUrl) {

        GetAsyncTask async = new GetAsyncTask();
        async.execute(strUrl);
        while ( !async.isDone() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "[getInSeparateThread] " + e.getMessage());
            }
        }

        return async.getResult();

    }

    public static String getInSeparateThread(String strUrl, String header) {

        GetAsyncTask async = new GetAsyncTask();
        async.execute(strUrl, header);
        while ( !async.isDone() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "[getInSeparateThread] " + e.getMessage());
            }
        }

        return async.getResult();

    }

    public static String postInSeparateThread(String strUrl, String header, String data) {

        PostAsyncTask async = new PostAsyncTask();
        async.execute(strUrl, header, data);
        while ( !async.isDone() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "[postInSeparateThread] " + e.getMessage());
            }
        }

        return async.getResult();

    }

    public static String deleteInSeparateThread(String strUrl, String header, String data) {

        DeleteAsyncTask async = new DeleteAsyncTask();
        async.execute(strUrl, header, data);
        while ( !async.isDone() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "[postInSeparateThread] " + e.getMessage());
            }
        }

        return async.getResult();

    }

    /**
     * http get방식 통신
     * @param strUrl
     * @param header
     * @return
     */
    public static String get(String strUrl, String header) {
        return get(strUrl, header.split(" "));
    }

    /**
     * http get방식 통신
     * @param strUrl
     * @param headers
     * @return
     */
    public static String get(String strUrl, String[] headers) {

        String ret = "";

        /*
        OkHttpClient client = new OkHttpClient();

        // URL에 포함할 Query문 작성
        HttpUrl.Builder urlBuilder = HttpUrl.parse(strUrl).newBuilder();
        String requestUrl = urlBuilder.build().toString();

        // Query문이 들어간 URL을 토대로 Request 생성
        Request request = new Request.Builder().url(requestUrl).build();

        // 만들어진 Request를 서버로 요청할 Client 생성
        */

        ///*
        HttpURLConnection hurlc = null;
        BufferedReader br = null;

        try {

            // 연결 설정
            hurlc = getUrlConnection(strUrl);
            hurlc.setRequestMethod("GET");
            hurlc.setDoInput(true);
            //hurlc.setDoOutput(true);
            hurlc.setConnectTimeout(5 * 1000);

            if ( headers != null ) {
                for ( String h : headers ) {
                    String[] split = h.split(":");
                    hurlc.setRequestProperty(split[0], split[1]);
                }
            }

            // 서버 연결
            hurlc.connect();

            int resposneCode = hurlc.getResponseCode();
            String responseMessage = hurlc.getResponseMessage();

            Log.d(TAG, "resposneCode: " + resposneCode);
            Log.d(TAG, "responseMessage: " + responseMessage);

            // 서버로 받은 응답 결과 처리
            if ( resposneCode == HttpURLConnection.HTTP_OK ) {

                StringBuffer sb = new StringBuffer();
                br = new BufferedReader( new InputStreamReader(hurlc.getInputStream()) );
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                ret = sb.toString();

            }

        } catch (ProtocolException e) {
            Log.e(TAG, "[get] " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "[get] " + e.getMessage());
        } finally {
            if (br != null) try { br.close(); } catch (IOException e) {}
            if (hurlc != null) hurlc.disconnect();
        }
        //*/

        return ret;

    }

    /**
     * http post방식 통신
     * @param strUrl
     * @param header
     * @param data
     * @return
     */
    public static String post(String strUrl, String header, String data) {
        return post(strUrl, header.split(" "), data);
    }

    /**
     * http post방식 통신
     * @param strUrl
     * @param headers
     * @param data
     * @return
     */
    public static String post(String strUrl, String[] headers, String data) {


        String ret = "";

        ///*
        HttpURLConnection hurlc = null;
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {

            // 연결 설정
            hurlc = getUrlConnection(strUrl);
            hurlc.setRequestMethod("POST");
            hurlc.setDoInput(true);
            hurlc.setDoOutput(true);
            //hurlc.setConnectTimeout(5 * 1000);

            if ( headers != null ) {
                for ( String h : headers ) {
                    String[] split = h.split(":");
                    hurlc.setRequestProperty(split[0], split[1]);
                }
            }

            Log.d(TAG, "Data: " + data);

            // 서버로 넘길 데이터 설정
            bw = new BufferedWriter( new OutputStreamWriter(hurlc.getOutputStream()) );
            bw.write(data);
            bw.flush();
            bw.close();

            // 서버 연결
            hurlc.connect();

            Log.d(TAG, "Response Code: " + hurlc.getResponseCode());
            Log.d(TAG, "Response Message: " + hurlc.getResponseMessage());

            // 서버로 받은 응답 결과 처리
            if ( hurlc.getResponseCode() == HttpURLConnection.HTTP_OK ) {

                StringBuffer sb = new StringBuffer();
                br = new BufferedReader( new InputStreamReader(hurlc.getInputStream()) );
                String line = null;

                while ( (line = br.readLine()) != null ) {
                    sb.append(line);
                }

                ret = sb.toString();

            }

        } catch (ProtocolException e) {
            Log.e(TAG, "[post] " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "[post] " + e.getMessage());
        } finally {
            if ( br != null ) try { br.close(); } catch (IOException e) {}
            if ( bw != null ) try { bw.close(); } catch (IOException e) {}
            if ( hurlc != null ) hurlc.disconnect();
        }
        //*/

        return ret;

    }

    /**
     * http delete방식 통신
     * @param strUrl
     * @param header
     * @param data
     * @return
     */
    public static String delete(String strUrl, String header, String data) {
        return delete(strUrl, header.split(" "), data);
    }

    /**
     * http delete방식 통신
     * @param strUrl
     * @param headers
     * @param data
     * @return
     */
    public static String delete(String strUrl, String[] headers, String data) {

        String ret = "";

        HttpURLConnection hurlc = null;
        BufferedWriter bw = null;
        BufferedReader br = null;

        try {

            // 연결 설정
            hurlc = getUrlConnection(strUrl);
            hurlc.setRequestMethod("DELETE");
            hurlc.setDoInput(true);
            //hurlc.setDoOutput(false);
            hurlc.setConnectTimeout(5 * 1000);

            if ( headers != null ) {
                for ( String h : headers ) {
                    String[] split = h.split(":");
                    hurlc.setRequestProperty(split[0], split[1]);
                }
            }

            // 서버로 넘길 데이터 설정
            bw = new BufferedWriter( new OutputStreamWriter(hurlc.getOutputStream()) );
            bw.write(data);
            bw.flush();
            bw.close();

            // 서버 연결
            hurlc.connect();

            int responseCode = hurlc.getResponseCode();
            String responseMessage = hurlc.getResponseMessage();

            Log.d(TAG, "[delete] Response Code: " + responseCode);
            Log.d(TAG, "[delete] Response Message: " + responseMessage);

            // 서버로 받은 응답 결과 처리
            if ( hurlc.getResponseCode() == HttpURLConnection.HTTP_OK ) {

                StringBuffer sb = new StringBuffer();
                br = new BufferedReader( new InputStreamReader(hurlc.getInputStream()) );
                String line = null;

                while ( (line = br.readLine()) != null ) {
                    sb.append(line);
                }

                ret = sb.toString();

            }

        } catch (ProtocolException e) {
            Log.e(TAG, "[delete] " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "[delete] " + e.getMessage());
        } finally {
            if ( br != null ) try { br.close(); } catch (IOException e) {}
            if ( bw != null ) try { bw.close(); } catch (IOException e) {}
            if ( hurlc != null ) hurlc.disconnect();
        }

        return ret;

    }

    /**
     * http 통신을 위한 HttpURLConnection 객체 생성
     *
     * @param strUrl
     * @return
     */
    private static HttpURLConnection getUrlConnection(String strUrl) {

        if ( strUrl.toLowerCase().startsWith("https") ) {
            return getHttpsUrlConnection(strUrl);
        }

        HttpURLConnection hurlc = null;

        try {

            URL url = new URL(strUrl);
            hurlc = (HttpURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            Log.e(TAG, "[getUrlConnection] " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "[getUrlConnection] " + e.getMessage());
        }

        return hurlc;

    }

    /**
     * https 통신을 위한 HttpsURLConnection 객체 생성
     *
     * @param strUrl
     * @return
     */
    private static HttpsURLConnection getHttpsUrlConnection(String strUrl) {

        HttpsURLConnection hurlc = null;

        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };

        try {

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            URL url = new URL(strUrl);
            hurlc = (HttpsURLConnection) url.openConnection();
            hurlc.setSSLSocketFactory(sslContext.getSocketFactory());
            hurlc.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (MalformedURLException e) {
            Log.e(TAG, "[getHttpsUrlConnection] " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "[getHttpsUrlConnection] " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "[getHttpsUrlConnection] " + e.getMessage());
        } catch (KeyManagementException e) {
            Log.e(TAG, "[getHttpsUrlConnection] " + e.getMessage());
        }

        return hurlc;

    }
}

class GetAsyncTask extends AsyncTask<String, Integer, String> {

    private String result = null;
    private boolean done = false;
    public String getResult() { return result; }
    public boolean isDone() { return done; }

    @Override
    protected String doInBackground(String... args) {
        result = Curl.get(args[0], args[1]);
        done = true;
        return result;
    }

    protected void onPostExecute(String result) {
        this.result = result;
        done = true;
    }

}

class PostAsyncTask extends AsyncTask<String, Integer, String> {

    private String result = null;
    private boolean done = false;
    public String getResult() { return result; }
    public boolean isDone() { return done; }

    @Override
    protected String doInBackground(String... args) {
        result = Curl.post(args[0], args[1], args[2]);  // url, header, data
        done = true;
        return result;
    }

    protected void onPostExecute(String result) {
        this.result = result;
        done = true;
    }

}

class DeleteAsyncTask extends AsyncTask<String, Integer, String> {

    private String result = null;
    private boolean done = false;
    public String getResult() { return result; }
    public boolean isDone() { return done; }

    @Override
    protected String doInBackground(String... args) {
        result = Curl.delete(args[0], args[1], args[2]);  // url, header, data
        done = true;
        return result;
    }

    protected void onPostExecute(String result) {
        this.result = result;
        done = true;
    }

}