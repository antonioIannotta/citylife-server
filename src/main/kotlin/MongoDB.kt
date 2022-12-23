import com.mongodb.MongoClient
import com.mongodb.MongoClientURI

class MongoDB {

    val url = "mongodb+srv://admin:<password>@sctm.p6dkpwo.mongodb.net/?retryWrites=true&w=majority"
    val dbName = "CityLife"
    val collectionName = "Location"

    val locationCollection = MongoClient(MongoClientURI(url))
        .getDatabase(dbName).getCollection(collectionName)

    fun readLocationForEveryUser() = locationCollection.find()
}