package tel.skylab.skylabtel.apis.api;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Apiclient {

    //    public static final String BASE_URL="http://driveit.seowithexperts.online/";
    public static final String BASE_URL = "https://api.skylab.tel/";

    /*//For Set Proxy
    java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("http://35.176.111.241/", 443));
    OkHttpClient okHttpClient1 = new OkHttpClient.Builder().proxy(proxy).build();*/

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(
                                    BASE_URL) /*.client(getUnsafeOkHttpClient().newBuilder().build())*/
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        }
        return retrofit;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts =
                    new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(
                                    java.security.cert.X509Certificate[] chain, String authType)
                                    throws CertificateException {}

                            @Override
                            public void checkServerTrusted(
                                    java.security.cert.X509Certificate[] chain, String authType)
                                    throws CertificateException {}

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[0];
                            }
                        }
                    };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient =
                    okHttpClient
                            .newBuilder()
                            .sslSocketFactory(sslSocketFactory)
                            .hostnameVerifier(
                                    org.apache.http.conn.ssl.SSLSocketFactory
                                            .ALLOW_ALL_HOSTNAME_VERIFIER)
                            .build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
