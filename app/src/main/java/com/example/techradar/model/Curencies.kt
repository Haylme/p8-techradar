import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Curencies(
    @SerialName("date")
    val date: String,

    @SerialName("to")
    val to: Int,


)
