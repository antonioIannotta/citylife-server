/**
 * Classe che si occupa di comporre una segnalazione, composta da:
 * tipologia di segnalazione,
 * posizione dell'utente che la invia,
 * data e ora dell'invio della segnalazione,
 * testo,
 * username dell'utente che la invia
 */
data class Report(val type: String, val location: String,
                  val localDateTime: String, val text: String, val username: String) {

    /**
     * Metodo che effettua il confronto tra due report sulla base
     */
    override fun equals(other: Any?) =
        other is Report && this.type == other.type && this.location == other.location
                && this.localDateTime == other.localDateTime && this.username == other.username
                && this.text == other.text

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + localDateTime.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}
