import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gm.cw.Comment
import com.gm.cw.R

class CommentAdapter(private val comments: List<Comment>, private val onLikeClickListener: OnLikeClickListener, private val nome: String) :


    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return CommentViewHolder(view, onLikeClickListener, nome)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    // Interface to handle clicks on the like button
    interface OnLikeClickListener {
        fun onLikeClick(commentId: Int)
    }

    class CommentViewHolder(itemView: View, private val onLikeClickListener: OnLikeClickListener, name:String) :
        RecyclerView.ViewHolder(itemView) {

        // References to layout elements
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val peopleTextView: TextView = itemView.findViewById(R.id.people)
        private val messageTextView: TextView = itemView.findViewById(R.id.message)
        private val heartTextView: TextView = itemView.findViewById(R.id.heart)
        private val heartImageView: ImageView = itemView.findViewById(R.id.curtida_1)
        private val nome: String = name
        fun bind(comment: Comment) {

            // Assign values to layout elements
            dateTextView.text = comment.date
            peopleTextView.text = comment.people
            messageTextView.text = comment.message
            heartTextView.text = comment.heartCount.toString()

            // Check if the user's name is in the list of likes
            var isUserLiked = false
            if (comment.peopleHeart != "") {
                isUserLiked = comment.peopleHeart.contains(nome)
            }
            // Add an OnClickListener to the like button
            heartImageView.setOnClickListener {
                // Call the like logic passing the comment ID
                onLikeClickListener.onLikeClick(comment.id)
                if (comment.peopleHeart != "") {
                    isUserLiked = comment.peopleHeart.contains(nome)
                }
                // Update the UI here, if necessary, for example, changing the heart image (liked)
                val updatedDrawable = if (isUserLiked) R.drawable.heart1 else R.drawable.heart2
                heartImageView.setImageResource(updatedDrawable)
            }

            // Logic to change the heart image (liked) based on the comment's state
            val drawableRes = if (isUserLiked) R.drawable.heart1 else R.drawable.heart2
            heartImageView.setImageResource(drawableRes)
        }
    }
}
