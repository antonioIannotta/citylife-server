import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ServerReport
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.Document

val dbAddress = "mongodb+srv://admin:<password>@sctm.p6dkpwo.mongodb.net/?retryWrites=true&w=majority"
val dbName = "CityLife"
val clientCollection = MongoClient(MongoClientURI(dbAddress)).getDatabase(dbName)
    .getCollection("Client collection")
val serverCollection = MongoClient(MongoClientURI(dbAddress)).getDatabase(dbName)
    .getCollection("Server collection")

fun main() {


    var lastLocalStoredReport = Report("", "", "", "", "")

    while (true) {
        val lastReportInsertedInDB = clientCollection.find().first()?.let { createReport(it) }
        if (lastLocalStoredReport == lastReportInsertedInDB) {
            continue
        } else {
            if (lastReportInsertedInDB != null) {
                lastLocalStoredReport = lastReportInsertedInDB
            }
            val listOfInterestedUser = ServerOperations().interestedUsersForReport(lastLocalStoredReport)
            val serverReport = ServerReport(lastLocalStoredReport.type, lastLocalStoredReport.location,
            lastLocalStoredReport.localDateTime, lastLocalStoredReport.text, listOfInterestedUser)

            createAndInsertServerReport(serverReport)
        }
    }
}

private fun createAndInsertServerReport(serverReport: ServerReport) {
    serverCollection.insertOne(createDocument(serverReport))
}

private fun createDocument(serverReport: ServerReport): Document {
    return Document()
        .append("Type", serverReport.type)
        .append("Location", serverReport.location)
        .append("LocalDateTime", serverReport.localDateTime)
        .append("Text", serverReport.text)
        .append("ListOfUsername", serverReport.listOfUsername)
}
private fun createReport(document: Document): Report {
    val type = document["Type"].toString()
    val location = document["Location"].toString()
    val localDateTime =  document["Type"].toString()
    val text = document["Text"].toString()
    val username = document["Username"].toString()

    return Report(type, location, localDateTime, text, username)
}
