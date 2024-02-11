package validation.handler

import exceptions.DomainException
import validation.Error as MyError
import validation.ValidationHandler

class ThrowsValidationHandler : ValidationHandler {
    override fun append(anError: MyError): ValidationHandler {
        throw DomainException.with(listOf(anError))
    }

    override fun append(anHandler: ValidationHandler): ValidationHandler {
        throw DomainException.with(anHandler.getErrors())
    }

    override fun validate(aValidation: ValidationHandler.Validation): ValidationHandler {
        try {
            aValidation.validate()
        } catch (ex: Exception) {
            throw DomainException.with(listOf(MyError(ex.message)))
        }

        return this
    }

    override fun getErrors(): List<MyError> {
        return listOf()
    }
}