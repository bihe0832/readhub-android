package okhttp4k

/**
 * Created by enzowei on 2017/12/14.
 */

@Suppress("unused")
sealed class Either<out L, out R>

data class Left<out T>(val value: T) : Either<T, Nothing>()
data class Right<out T>(val value: T) : Either<Nothing, T>()

inline fun <L, R, T> Either<L, R>.fold(left: (L) -> T, right: (R) -> T): T =
    when (this) {
      is Left -> left(value)
      is Right -> right(value)
    }

inline fun <L, R, T> Either<L, R>.fold(either: (any: Any) -> T): T =
    this.fold({ l -> either(l as Any) }, { r -> either(r as Any) })

inline fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> =
    fold({ this as Left }, f)

inline fun <L, R, T> Either<L, R>.map(f: (R) -> T): Either<L, T> =
    flatMap { Right(f(it)) }