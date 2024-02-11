import validation.ValidationHandler

abstract class AggregateRoot<ID: Identifier>(id: ID) : Entity<ID>(id) {
    override fun validate(handler: ValidationHandler) {
        TODO("Not yet implemented")
    }
}