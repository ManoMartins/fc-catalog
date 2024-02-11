import validation.ValidationHandler
import java.util.Objects

abstract class Entity<ID : Identifier>(private val _id: ID) {
    abstract fun validate(handler: ValidationHandler)

    val id: ID
        get() = _id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Entity<*>

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}