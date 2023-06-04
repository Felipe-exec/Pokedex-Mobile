package br.com.fabricadesinapse.pokedex_android.domain

data class Pokemon(
    val number: Int,
    val name: String,
    val types: List<PokemonType>
) {
    val formattedName = name.capitalize()

    val formattedNumber = number.toString().padStart(3, '0')

    val imageUrl = "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/$formattedNumber.png"

    @JvmName("getName1")
    fun getName(): String {
        return formattedName
    }
<<<<<<< HEAD

    fun getNumber(): String {
        return formattedNumber
    }
>>>>>>> 061c4892079a8498e3f2cc7e9572dd4aa196c010
}
