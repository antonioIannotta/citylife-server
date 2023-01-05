import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import org.bson.Document

class MongoDB {

    private val url = "mongodb+srv://antonioIannotta:AntonioIannotta-26@citylife.f5vv5xs.mongodb.net/?retryWrites=true"
    private val dbName = "CityLife"
    private val collectionName = "location"

    private val locationCollection: MongoCollection<Document> = MongoClient(MongoClientURI(url))
        .getDatabase(dbName).getCollection(collectionName)

    fun readLocationForEveryUser() = locationCollection.find()
}