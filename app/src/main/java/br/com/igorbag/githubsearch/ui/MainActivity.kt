import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var buttonConfirm: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextUsername = findViewById(R.id.editTextUsername)
        buttonConfirm = findViewById(R.id.buttonConfirm)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RepositoryAdapter { repository, action ->
            when (action) {
                "open" -> openLink(repository.html_url)
                "share" -> shareLink(repository.html_url)
            }
        }
        recyclerView.adapter = adapter

        buttonConfirm.setOnClickListener {
            val username = editTextUsername.text.toString()
            if (username.isNotBlank()) {
                fetchRepositories(username)
            } else {
                Toast.makeText(this, "Por favor, insira um nome de usuário", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRepositories(username: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        service.listRepos(username).enqueue(object : Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.updateRepositories(it)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Erro ao buscar repositórios",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Falha ao se conectar: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun shareLink(url: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Confira este repositório: $url")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
    }
}
