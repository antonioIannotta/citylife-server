import com.example.citylife.model.report.Report
import com.mongodb.client.FindIterable
import org.bson.Document
import kotlin.math.*

class ServerOperations {

    private val usersLocationAndDistance: FindIterable<Document> = MongoDB().readLocationForEveryUser()

    private fun retrieveLocationForEveryUser(): MutableList<MutableMap<String, String>> {
        val mapUserLocationDistance = emptyList<MutableMap<String, String>>().toMutableList()
        usersLocationAndDistance.forEach {
            document -> mapUserLocationDistance.add(returnMapFromDocument(document))
        }

        return mapUserLocationDistance
    }

    fun interestedUsersForReport(report: Report): String {
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
        return usernameList.toString()
    }

    //fun send(username: String, report: Report) {}

    private fun returnMapFromDocument(document: Document): MutableMap<String, String> {
        val mapUserLocationDistance = mutableMapOf<String, String>()
        val entryList = document.entries.toList()
        mapUserLocationDistance["username"] = entryList[1].value.toString()
        mapUserLocationDistance["location"] = entryList[2].value.toString()
        mapUserLocationDistance["distance"] = entryList[3].value.toString()

        return mapUserLocationDistance
    }

    private fun userInDistanceOfInterest(userMap: Map<String, String>, reportLat: String, reportLon: String): Boolean {
        val userLatitude = userMap["location"]?.split(" - ")?.get(0)?.toDouble()
        val userLongitude = userMap["location"]?.split(" - ")?.get(1)?.toDouble()
        val userDistance = userMap["distance"]?.toDouble()

        return computeDistance(userLatitude, userLongitude, reportLat.toDouble(), reportLon.toDouble()) < userDistance!!

    }

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