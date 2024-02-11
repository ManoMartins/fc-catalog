package category

import AggregateRoot
import validation.ValidationHandler
import java.time.Instant

class Category(
    anId: CategoryID,
    var name: String,
    var description: String?,
    var isActive: Boolean,
    val createdAt: Instant,
    var updatedAt: Instant,
    var deletedAt: Instant?
) : Cloneable, AggregateRoot<CategoryID>(anId) {
    companion object {
        fun newCategory(aName: String, aDescription: String?, isActive: Boolean): Category {
            val id = CategoryID.unique()
            val now = Instant.now()

            val deletedAt = if (isActive) null else now

            return Category(id, aName, aDescription, isActive, now, now, deletedAt)
        }
    }

    override fun validate(handler: ValidationHandler) {
        CategoryValidator(this, handler).validate()
    }

    fun deactivate(): Category {
        if (this.deletedAt == null) {
            this.deletedAt = Instant.now()
        }

        this.isActive = false
        this.updatedAt = Instant.now()

        return this
    }

    fun activate(): Category {
        this.deletedAt = null
        this.isActive = true
        this.updatedAt = Instant.now()

        return this
    }

    fun update(aName: String, aDescription: String, isActive: Boolean) : Category {
        if (isActive) {
            activate()
        } else {
            deactivate()
        }

        this.name = aName
        this.description = aDescription
        this.updatedAt = Instant.now()

        return this
    }

    public override fun clone(): Category {
        return super.clone() as Category
    }
}