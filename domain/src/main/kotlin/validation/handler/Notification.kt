package validation.handler

import exceptions.DomainException
import validation.Error
import validation.ValidationHandler

class Notification private constructor(private val errors: MutableList<Error>): ValidationHandler {
    companion object {
        fun create(): Notification {
            return Notification(ArrayList())
        }

        fun create(anError: Error): Notification {
            return Notification(ArrayList()).append(anError)
        }

        fun create(t: Throwable): Notification {
            return create(Error(t.message))
        }
    }

    override fun append(anError: Error): Notification {
        this.errors.add(anError)
        return this
    }

    override fun append(anHandler: ValidationHandler): Notification {
        this.errors.addAll(anHandler.getErrors())
        return this
    }

    override fun validate(aValidation: ValidationHandler.Validation): Notification {
        try {
            aValidation.validate()
        } catch (ex: DomainException) {
            this.errors.plus(ex.getErrors())
        } catch (t: Throwable) {
            this.errors.plus(Error(t.message))
        }

        return this
    }

    override fun getErrors(): List<Error> {
        return this.errors
    }
}