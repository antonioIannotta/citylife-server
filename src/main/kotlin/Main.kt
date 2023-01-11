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

/**
 * Il metodo main è il punto d'accesso al server.
 * Prevede un ciclo infinito che, ripetutamente, controlla l'ultimo report inserito dagli utenti. In caso questo
 * report sia diverso da quello precedentemente memorizzato recupera la lista degli utenti interessati al report e
 * si occupa di aggiungere il report, con il tag dell'utente interessato, all'interno della collezione
 * 'userReportCollection' nel DB.
 */
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
            listOfInterestedUser.forEach {
                username -> createAndInsertUserReportDocument(username, lastLocalStoredReport)
            }
            Thread.sleep(10000)
        }
    }
}

/**
 * Metodo che si occupa di inserire un report, con il tag dello username dell'utente interessato, all'interno della
 * collezione 'userReportCollection'
 */
private fun createAndInsertUserReportDocument(username: String, lastLocalStoredReport: Report) =
    userReportCollection.insertOne(createUserReportDocument(username, lastLocalStoredReport))

/**
 * Metodo che si occupa di creare, a partire da un report passato come argomento e dallo username un nuovo documento,
 * da memorizzare nella collezione 'userReportCollection'
 */
private fun createUserReportDocument(username: String, lastReport: Report): Document {
    return Document()
        .append("interestedUsername", username)
        .append("type", lastReport.type)
        .append("location", lastReport.location)
        .append("localDateTime", lastReport.localDateTime)
        .append("text", lastReport.text)
        .append("username", lastReport.username)
}

/**
 * Metodo che a partire da un documento si occupa di costruire il relativo Report
 */
private fun createReport(document: Document): Report {
    val type = document["type"].toString()
    val location = document["location"].toString()
    val localDateTime =  document["localDateTime"].toString()
    val text = document["text"].toString()
    val username = document["username"].toString()

    return Report(type, location, localDateTime, text, username)
}
