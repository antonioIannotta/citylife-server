class ServerOperations {

    val usersLocationAndDistance = MongoDB().readLocationForEveryUser()

    fun retrieveLocationForEveryUser() {
        val mapUserLocationDistance = emptyList<Map<String, String>>().toMutableList()
    }
}