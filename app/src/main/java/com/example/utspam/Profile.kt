
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.utspam.LoginActivity
import com.example.utspam.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let {
            val username = it.displayName ?: "Unknown"
            val email = it.email ?: "Unknown"
            val githubUsername = "Your GitHub Username"

            binding.textViewUsername.text = username
            binding.textViewEmail.text = email
            binding.textViewGithubUsername.text = githubUsername
            val placeholderImage = createInitialsImage(username)
            binding.imageViewProfile.setImageBitmap(placeholderImage)
        }
        binding.logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun createInitialsImage(name: String): Bitmap {
        val width = 100
        val height = 100
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.BLUE
            isAntiAlias = true
            textSize = 40f
            typeface = Typeface.DEFAULT_BOLD
        }
        val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
        val textBounds = Rect()
        paint.getTextBounds(initials, 0, initials.length, textBounds)
        val textX = (width - textBounds.width()) / 2f
        val textY = (height + textBounds.height()) / 2f
        canvas.drawText(initials, textX, textY, paint)
        return bitmap
    }
}
