package se.umu.cs.oi19aws.makrokoll.models

import android.os.Parcel
import android.os.Parcelable

// Model class to store ingredients as its own class
class IngredientsModel(
    private var weightVolume: String?,
    private var measurementUnit: String?,
    private var ingredientName: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    fun getWeightVolume(): String? {
        return weightVolume
    }
    fun getMeasurementUnit(): String? {
        return measurementUnit
    }
    fun getIngredientName(): String? {
        return ingredientName
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(weightVolume)
        parcel.writeString(measurementUnit)
        parcel.writeString(ingredientName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IngredientsModel> {
        override fun createFromParcel(parcel: Parcel): IngredientsModel {
            return IngredientsModel(parcel)
        }

        override fun newArray(size: Int): Array<IngredientsModel?> {
            return arrayOfNulls(size)
        }
    }
}