import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Curencies(


    @SerialName("date") val date: String,
    @SerialName("eur") val eur: Eur,
)

@Serializable
data class Eur(


    @SerialName("gbp") val gbp: Double,

    )

