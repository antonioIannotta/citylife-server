import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ServerReport
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import org.bson.Document

const val dbAddress = "mongodb+srv://antonioIannotta:AntonioIannotta-26@citylife.f5vv5xs.mongodb.net/?retryWrites=true"
const val dbName = "CityLife"
val clientCollection: MongoCollection<Document> = MongoClient(MongoClientURI(dbAddress)).getDatabase(dbName)
    .getCollection("clientReport")
val serverCollection: MongoCollection<Document> = MongoClient(MongoClientURI(dbAddress)).getDatabase(dbName)
    .getCollection("serverReport")
val userReportCollection: MongoCollection<Document> = MongoClient(MongoClientURI(dbAddress)).getDatabase(dbName)
    .getCollection("userReportDocument")
fun main() {


    var lastLocalStoredReport = Report("", "", "", "", "")

    while (true) {
        println("Checking!")
        val lastReportInsertedInDB = clientCollection.find().last()?.let { createReport(it) }
        println(lastReportInsertedInDB)
        if (lastLocalStoredReport == lastReportInsertedInDB || lastReportInsertedInDB == null) {
            continue
        } else {
            if (lastReportInsertedInDB != null) {
                lastLocalStoredReport = lastReportInsertedInDB
            }
            val listOfInterestedUser = ServerOperations().interestedUsersForReport(lastLocalStoredReport)
            /*val serverReport = ServerReport(lastLocalStoredReport.type, lastLocalStoredReport.location,
            lastLocalStoredReport.localDateTime, lastLocalStoredReport.text, listOfInterestedUser)*/
            listOfInterestedUser.forEach {
                username -> createAndInsertUserReportDocument(username, lastLocalStoredReport)
            }
            //createAndInsertServerReport(serverReport)
            Thread.sleep(10000)
        }
    }
}

private fun createAndInsertUserReportDocument(username: String, lastLocalStoredReport: Report) =
    userReportCollection.insertOne(createUserReportDocument(username, lastLocalStoredReport))

/*private fun createAndInsertServerReport(serverReport: ServerReport) {
    serverCollection.insertOne(createDocument(serverReport))
}*/



private fun createUserReportDocument(username: String, lastReport: Report): Document {
    return Document()
        .append("username", username)
        .append("type", lastReport.type)
        .append("location", lastReport.location)
        .append("localDateTime", lastReport.localDateTime)
        .append("text", lastReport.text)
}
private fun createReport(document: Document): Report {
    val type = document["type"].toString()
    val location = document["location"].toString()
    val localDateTime =  document["localDateTime"].toString()
    val text = document["text"].toString()
    val username = document["username"].toString()

    return Report(type, location, localDateTime, text, username)
}
