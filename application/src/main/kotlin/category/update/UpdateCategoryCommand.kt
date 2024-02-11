package com.full.admin.catalog.application.category.update

data class UpdateCategoryCommand(val id: String, val name: String, val description: String, val isActive: Boolean) {
    companion object {
        fun with(anId: String, aName: String, aDescription: String, isActive: Boolean): UpdateCategoryCommand {
            return UpdateCategoryCommand(anId, aName, aDescription, isActive)
        }
    }
}
