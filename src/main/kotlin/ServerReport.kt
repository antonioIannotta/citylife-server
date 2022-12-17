package com.example.citylife.model.report

/**
 * Classe che rappresenta il report depositato dal server ul database.
 * Contiene il tipo, la posizione, la data, il testo della segnalazione e la lista
 * degli utenti che sono interessati a quella segnalazione
 */
data class ServerReport(val type: String, val location: String,
                  val localDateTime: String, val text: String, val listOfUsername: String) {

    /**
     * Funzione che ritorna se due report depositati dal server sono uguali
     */
    fun equals(serverReport: ServerReport): Boolean {
        return this.type == serverReport.type &&
                this.location == serverReport.location &&
                this.localDateTime == serverReport.localDateTime &&
                this.text == serverReport.text &&
                this.listOfUsername == serverReport.listOfUsername
    }

    /**
     * Ritorna un Report con lo specifico username
     */
    fun toReport(username: String): Report {
        return Report(this.type, this.location, this.localDateTime, this.text, username)
    }
}

//TODO: discutere su questa classe