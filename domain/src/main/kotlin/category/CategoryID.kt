package category

import Identifier
import java.util.*

open class CategoryID(protected val _value: String) : Identifier() {
    companion object {
        fun unique(): CategoryID {
            return from(UUID.randomUUID())
        }

        fun from(anId: String): CategoryID {
            return CategoryID(anId)
        }

        fun from(anId: UUID): CategoryID {
            return CategoryID(anId.toString().lowercase())
        }
    }

    val value: String
        get() = _value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryID

        return _value == other._value
    }

    override fun hashCode(): Int {
        return _value.hashCode()
    }
}
