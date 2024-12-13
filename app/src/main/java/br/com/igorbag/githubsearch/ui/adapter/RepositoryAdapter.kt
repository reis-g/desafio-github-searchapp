import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoryAdapter(
    private val onActionClick: (Repository, String) -> Unit
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private val repositories = mutableListOf<Repository>()

    fun updateRepositories(newRepositories: List<Repository>) {
        repositories.clear()
        repositories.addAll(newRepositories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositories[position]
        holder.bind(repository, onActionClick)
    }

    override fun getItemCount(): Int = repositories.size

    class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val buttonOpen: Button = itemView.findViewById(R.id.buttonOpen)
        private val buttonShare: Button = itemView.findViewById(R.id.buttonShare)

        fun bind(repository: Repository, onActionClick: (Repository, String) -> Unit) {
            textViewName.text = repository.name
            buttonOpen.setOnClickListener { onActionClick(repository, "open") }
            buttonShare.setOnClickListener { onActionClick(repository, "share") }
        }
    }
}
