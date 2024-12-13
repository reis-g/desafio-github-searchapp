import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String): Call<List<Repository>>
}

// Model: Repository.kt
data class Repository(
    val name: String,
    val html_url: String
