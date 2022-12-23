package com.example.citylife.model.report

import java.time.LocalDateTime

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

    public fun equals(report: Report) =
        this.type == report.type && this.location == report.location
                && this.localDateTime == report.localDateTime && this.username == report.username
                && this.text == report.text
}
