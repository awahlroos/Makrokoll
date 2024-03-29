package se.umu.cs.oi19aws.makrokoll.models

// Model class for the recipe to be presented
class RecipeCardModel(
    private var id: Int,
    private var name: String,
    private var kcal: Int,
    private var protein: Int,
    private var fat: Int,
    private var carbs: Int,
    private var icon: String,
    private var image: String
) {
    fun getId(): Int {
        return id
    }

    fun getName(): String {
        return name
    }

    fun getKCAL(): Int {
        return kcal
    }

    fun getProtein(): Int {
        return protein
    }

    fun getFat(): Int {
        return fat
    }

    fun getCarbs(): Int {
        return carbs
    }

    fun getIcon(): String {
        return icon
    }

    fun getImage(): String {
        return image
    }
}
