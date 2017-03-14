package de.bitsharesmunich.cyptocoincore.insightapi;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by henry on 13/02/2017.
 */

class InsightApiServiceGenerator {
    public static String TAG = "InsightApiServiceGenerator";
    private static String API_BASE_URL;
    private static HttpLoggingInterceptor logging;
    private static OkHttpClient.Builder clientBuilder;
    private static Retrofit.Builder builder;

    private static HashMap<Class<?>, Object> Services;

    InsightApiServiceGenerator(String apiBaseUrl) {
        try {
            API_BASE_URL= apiBaseUrl;
            logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
            final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                    }
                    
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)  {
                    }
                    
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            clientBuilder = new OkHttpClient.Builder().addInterceptor(logging).hostnameVerifier(new HostnameVerifier() {
                
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            }).sslSocketFactory(sslContext.getSocketFactory(),(X509TrustManager)trustAllCerts[0]);
            builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
            Services = new HashMap<>();
        } catch (KeyManagementException ex) {
            Logger.getLogger(InsightApiServiceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(InsightApiServiceGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static <T> void setService(Class<T> klass, T thing) {
        Services.put(klass, thing);
    }

    public <T> T getService(Class<T> serviceClass) {

        T service = serviceClass.cast(Services.get(serviceClass));
        if (service == null) {
            service = createService(serviceClass);
            setService(serviceClass, service);
        }
        return service;
    }

    private static <S> S createService(Class<S> serviceClass) {

        clientBuilder.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request original = chain.request();
                okhttp3.Request.Builder requestBuilder = original.newBuilder().method(original.method(), original.body());

                okhttp3.Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        clientBuilder.readTimeout(5, TimeUnit.MINUTES);
        clientBuilder.connectTimeout(5, TimeUnit.MINUTES);
        OkHttpClient client = clientBuilder.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);

    }

    public static InsightApiService Create() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request original = chain.request();

                // Customize the request
                okhttp3.Request request = original.newBuilder().method(original.method(), original.body()).build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL).client(client).build();

        return retrofit.create(InsightApiService.class);

    }
}
