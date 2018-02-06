package okhttp4k.ssl

import okhttp4k.HttpDslMarker
import java.io.FileInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManager
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by enzowei on 2017/12/16.
 */
@SSLDslMarker
@HttpDslMarker
class SSLContextBuilder {

  private var trustManagers: Array<TrustManager>? = null
  private var keyManagers: Array<KeyManager>? = null

  internal fun createSSLContext(protocol: String = SSLContextBuilder.defaultTLSProtocol): Pair<SSLContext, X509TrustManager> {
    if (protocol.isEmpty()) {
      throw IllegalArgumentException("At least one protocol must be provided.")
    }
    val trustManager = chooseTrustManager(trustManagers)
    val context = SSLContext.getInstance(protocol).apply {
      init(keyManagers, arrayOf(trustManager), SecureRandom())
    }
    return Pair(context, trustManager)
  }

  private fun chooseTrustManager(trustManagers: Array<TrustManager>?): X509TrustManager {
    if (trustManagers == null || trustManagers.isEmpty()) {
      return UnsafeTrustManager()
    }
    trustManagers.forEach { trustManager ->
      if (trustManager is X509TrustManager) {
        return trustManager
      }
    }
    return UnsafeTrustManager()
  }

  @Suppress("unused")
  fun open(name: String) = KeyConfig(FileInputStream(name))

  @Suppress("unused")
  fun open(inputStream: InputStream) = KeyConfig(inputStream)

  @Suppress("unused")
  fun trustManager(store: () -> KeyConfig) {
    trustManagers = trustManagerFactory(store()).trustManagers
  }

  @Suppress("unused")
  fun x509TrustManager(trustManager: () -> X509TrustManager) {
    trustManagers = arrayOf(trustManager())
  }

  @Suppress("unused")
  fun trustManager(trustManagers: Array<TrustManager>) {
    this.trustManagers = trustManagers
  }

  @Suppress("unused")
  fun keyManager(store: () -> KeyConfig) {
    keyManagers = keyManagerFactory(store()).keyManagers
  }

  @Suppress("unused")
  fun keyManager(keyManagers: Array<KeyManager>) {
    this.keyManagers = keyManagers
  }

  private fun trustManagerFactory(store: KeyConfig): TrustManagerFactory {
    val algorithm = store.algorithm ?: TrustManagerFactory.getDefaultAlgorithm()
    val key = loadKeyStore(store)
    return TrustManagerFactory.getInstance(algorithm).apply {
      init(key)
    }
  }

  private fun keyManagerFactory(store: KeyConfig): KeyManagerFactory {
    val algorithm = store.algorithm ?: KeyManagerFactory.getDefaultAlgorithm()
    val key = loadKeyStore(store)
    return KeyManagerFactory.getInstance(algorithm).apply {
      init(key, store.password)
    }
  }

  private fun loadKeyStore(store: KeyConfig) = KeyStore.getInstance(store.fileType).apply {
    load(store.inputStream, store.password)
  }

  companion object {
    val defaultTLSProtocol = "TLSv1.2"
  }
}

@DslMarker
annotation class SSLDslMarker

class UnsafeTrustManager : X509TrustManager {

  @Throws(CertificateException::class)
  override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

  @Throws(CertificateException::class)
  override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

  override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}