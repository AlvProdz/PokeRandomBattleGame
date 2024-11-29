package com.example.pokerandombattle.data
import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: String,
    @SerializedName ("name") var name: String,
    @SerializedName ("sprites") var sprites: Sprite,
    @SerializedName ("types") var types: List<Type>,
    @SerializedName("damage_relations") var damageRelations: DamageRelations
) {
    fun getMainType() : TypeData {
        return types[0].typeData
    }
    fun getSubType() : TypeData? {
        return if (types.size > 1) {
            types[1].typeData
        } else {
            null
        }
    }
    fun getFrontImgUrl() : String {
        return sprites.otherSprites.officialArtWork.frontImgURL
    }
    fun getBackImgUrl() : String {
        return sprites.backImgURL
    }

}
data class Type (
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val typeData: TypeData
)

data class TypeData (
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class Sprite(
    @SerializedName("back_default") val backImgURL: String,
    @SerializedName("other") val otherSprites: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtWork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default") val frontImgURL: String
)