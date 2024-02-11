package category

import validation.Error
import validation.ValidationHandler
import validation.Validator

private const val NAME_MAX_LENGTH = 255

private const val NAME_MIN_LENGTH = 3

class CategoryValidator(private val category: Category, aHandler: ValidationHandler) : Validator(aHandler) {
    override fun validate() {
        checkNameConstraints()
    }

    private fun checkNameConstraints() {
        val name = this.category.name
        if (name.isBlank()) {
            this.validationHandler().append(Error("'name' should not be empty"))
            return
        }

        val length = name.trim().length
        if (length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH) {
            this.validationHandler().append(Error("'name' must be between 3 and 255 characters"))
            return
        }
    }
}