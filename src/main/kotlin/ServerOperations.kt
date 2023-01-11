import com.mongodb.client.FindIterable
import org.bson.Document
import kotlin.math.*

class ServerOperations {

    /**
     * Variabile in cui vengono memorizzati tutti i documenti che sono presenti all'interno della collezione 'location'
     * sul DB
     */
    private val usersLocationAndDistance: FindIterable<Document> = MongoDB().readLocationForEveryUser()

    /**
     * Metodo che ritorna una lista di mappe che tengono i dati degli utenti circa la propria posizione e la propria
     * distanza di interesse impostata.
     */
    private fun retrieveLocationForEveryUser(): MutableList<MutableMap<String, String>> {
        val mapUserLocationDistance = emptyList<MutableMap<String, String>>().toMutableList()
        usersLocationAndDistance.forEach {
            document -> mapUserLocationDistance.add(returnMapFromDocument(document))
        }

        return mapUserLocationDistance
    }

    /**
     * Metodo che ritorna la lista degli utenti che, in virtù della propria posizione e della propria distanza di
     * interesse, sono interessati ad un certo report.
     */
    fun interestedUsersForReport(report: Report): MutableList<String> {
        lateinit var usernameList: MutableList<String>
        if (report.location == "") {
            usernameList = emptyList<String>().toMutableList()
        } else {
            val reportLocation = report.location
            val reportLatitude = reportLocation.split(" - ")[0]
            val reportLongitude = reportLocation.split(" - ")[1]
            usernameList = emptyList<String>().toMutableList()
            retrieveLocationForEveryUser().filter { entry ->
                (userInDistanceOfInterest(entry, reportLatitude, reportLongitude))

            }.forEach { entry ->
                entry["username"]?.let { usernameList.add(it) }
            }
        }
        return usernameList
    }


    /**
     * Metodo che prende in ingresso un documento e ritorna una mappa con le informazioni relative allo username,
     * alla posizione dell'utente e alla distanza di interesse impostata.
     */
    private fun returnMapFromDocument(document: Document): MutableMap<String, String> {
        val mapUserLocationDistance = mutableMapOf<String, String>()
        mapUserLocationDistance["username"] = document["username"].toString()
        mapUserLocationDistance["location"] = document["location"].toString()
        mapUserLocationDistance["distance"] = document["distance"].toString()

        return mapUserLocationDistance
    }

    /**
     * Metodo che verifica se, dato un utente ed un report, la distanza di interesse di questo utente comprenda la
     * posizione presente all'interno del report
     */
    private fun userInDistanceOfInterest(userMap: Map<String, String>, reportLatitude: String, reportLongitude: String): Boolean {
        val userLatitude = userMap["location"]?.split(" - ")?.get(0)?.toDouble()
        val userLongitude = userMap["location"]?.split(" - ")?.get(1)?.toDouble()
        val userDistance = userMap["distance"]?.toDouble()

        return computeDistance(userLatitude, userLongitude, reportLatitude.toDouble(), reportLongitude.toDouble()) < userDistance!!

    }

    /**
     * Metodo che si occupa di calcolare la distanza tra due posizioni espresse in coordinate geografiche.
     */
    private fun computeDistance(userLatitude: Double?, userLongitude: Double?, reportLatitude: Double, reportLongitude: Double): Double {
        val earthRadius = 6371
        val dLat = deg2rad(reportLatitude - userLatitude!!)
        val dLon = deg2rad(reportLongitude - userLongitude!!)
        val a =
            sin(dLat / 2) * sin(dLon / 2) +
                    cos(deg2rad(reportLatitude)) * cos(deg2rad(userLatitude)) *
                    sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun deg2rad(deg: Double) = deg * (PI / 180)
}