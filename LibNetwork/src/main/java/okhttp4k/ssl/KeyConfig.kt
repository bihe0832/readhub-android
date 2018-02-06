package okhttp4k.ssl

import java.io.InputStream

/**
 * Created by enzowei on 2017/12/16.
 */
@SSLDslMarker
class KeyConfig(val inputStream: InputStream) {
  var algorithm: String? = null
  var password: CharArray? = null
  var fileType: String = "JKS"

  infix fun withPass(pass: String) = apply {
    password = pass.toCharArray()
  }

  infix fun ofType(type: String) = apply {
    fileType = type
  }

  infix fun using(algo: String) = apply {
    algorithm = algo
  }

}


