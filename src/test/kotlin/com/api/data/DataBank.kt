package api.data

enum class DataBank {
    LOGIN_ADMIN("eve.holt@reqres.in"),
    PASSWORD_ADMIN("pistol"),
    LOGIN_URL("https://reqres.in/api/register");

    private lateinit var array: List<String>
    private lateinit var value: String

    constructor(strings: List<String>) {
        array = strings
    }

    constructor(text: String) {
        value = text
    }

    fun get(): String {
        return value
    }
}