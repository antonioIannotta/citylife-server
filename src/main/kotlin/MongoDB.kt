import com.mongodb.MongoClient

class MongoDB {

    val url = "127.0.0.1"
    val port = 27017
    val dbName = "CityLife"
    val collectionName = "Location"

    val locationCollection = MongoClient(url, port)
        .getDatabase(dbName).getCollection(collectionName)

    fun readLocationForEveryUser() = locationCollection.find()
}