package app.aaps.plugins.sync.nsShared

import android.content.Context
import app.aaps.plugins.sync.R
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SslSocketHelper {

    fun createOkHttpClient(context: Context): OkHttpClient {
        val cf = CertificateFactory.getInstance("X.509")
        val ca = context.resources.openRawResource(R.raw.setonez_home_ca).use { cf.generateCertificate(it) }
        val ks = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("setonez_home_ca", ca)
        }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply { init(ks) }
        val tm = tmf.trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("TLS").apply { init(null, tmf.trustManagers, null) }
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, tm)
            .build()
    }
}
