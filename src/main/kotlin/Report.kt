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
data class Report(val type: ReportType, val location: String,
                  val localDateTime: LocalDateTime, val text: String, val username: String) {}
