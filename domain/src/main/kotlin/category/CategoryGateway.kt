package category

import pagination.Pagination

interface CategoryGateway {
    fun create(aCategory: Category): Category

    fun deleteById(anId: CategoryID)

    fun findById(anId: CategoryID): Category?

    fun update(aCategory: Category): Category

    fun findAll(aQuery: CategorySearchQuery): Pagination<Category>
}