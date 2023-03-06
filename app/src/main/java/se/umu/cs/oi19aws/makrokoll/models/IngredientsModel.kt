package se.umu.cs.oi19aws.makrokoll.models

class IngredientsModel(
    private var weightVolume: String,
    private var measurementUnit: String,
    private var ingredientName: String
) {
    fun getWeightVolume():String {
        return weightVolume
    }
    fun getMeasurementUnit():String {
        return measurementUnit
    }
    fun getIngredientName():String {
        return ingredientName
    }
}