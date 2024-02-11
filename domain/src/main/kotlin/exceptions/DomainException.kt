package exceptions

import validation.Error

class DomainException private constructor(aMessage: String, private val anErrors: List<Error>) : NoStacktraceException(aMessage) {
    companion object {
        fun with(anError: Error): DomainException {
            return DomainException("", listOf(anError))
        }

        fun with(anErrors: List<Error>): DomainException {
            return DomainException("", anErrors)
        }
    }

    fun getErrors(): List<Error> {
        return anErrors
    }
}