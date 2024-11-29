package com.example.pokerandombattle.data

import com.google.gson.annotations.SerializedName

data class TypeDamageRelation(
    @SerializedName("id") val typeId: String,
    @SerializedName("name") var typeName: String,
    @SerializedName("damage_relations") var damageRelations: DamageRelations
)
data class DamageRelations(
    @SerializedName("double_damage_from") var vulnerabilities: List<Vulnerability>,
    @SerializedName("double_damage_to") var strongAgainstList: List<StrongAgainst>,
    @SerializedName("half_damage_from") var badAgainstList: List<BadAgainst>,
    @SerializedName("no_damage_from") var immuneToList: List<ImmuneAgainst>,
)
data class Vulnerability(
    @SerializedName("name") var name: String
)
data class StrongAgainst(
    @SerializedName("name") var name: String
)

data class BadAgainst(
    @SerializedName("name") var name: String
)
data class ImmuneAgainst(
    @SerializedName("name") var name: String
)
