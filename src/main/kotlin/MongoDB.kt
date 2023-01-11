import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import org.bson.Document

class MongoDB {

    private val url = "mongodb+srv://antonioIannotta:AntonioIannotta-26@citylife.f5vv5xs.mongodb.net/?retryWrites=true"
    private val dbName = "CityLife"
    private val collectionName = "location"

    /**
     * Variabile in cui viene memorizzata un'istanza della collezione 'location' sul DB
     */
    private val locationCollection: MongoCollection<Document> = MongoClient(MongoClientURI(url))
        .getDatabase(dbName).getCollection(collectionName)

    /**
     * Metodo che si occupa di fare un recupero di tutti i documenti che sono contenuti all'interno della collezione
     * 'location'
     */
    fun readLocationForEveryUser(): FindIterable<Document> = locationCollection.find()
}