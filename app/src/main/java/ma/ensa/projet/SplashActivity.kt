package ma.ensa.projet

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Supprimer le titre de l'activité
        supportActionBar?.hide()

        // Création du layout principal
        val layout = ConstraintLayout(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )

            // Ajout d'un dégradé de couleurs vives
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(0xFFFF5722.toInt(), 0xFFE91E63.toInt(), 0xFF3F51B5.toInt())
            )
            background = gradientDrawable
        }

        // Logo centré avec animation de rebond
        val logoImageView = ImageView(this).apply {
            setImageResource(R.drawable.img)
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                200.dpToPx(),
                200.dpToPx()
            )

            // Animation du logo
            val bounceAnimation = ScaleAnimation(
                0.8f, 1f,
                0.8f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 1000
                interpolator = BounceInterpolator()
                repeatCount = Animation.INFINITE
            }
            startAnimation(bounceAnimation)
        }

        // Message de salutation
        val greetingTextView = TextView(this).apply {
            id = View.generateViewId()
            text = ""
            textSize = 24f
            setTextColor(0xFFFFFFFF.toInt())
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Ajout des vues au layout
        layout.addView(logoImageView)
        layout.addView(greetingTextView)
        setContentView(layout)

        // Contraintes pour centrer le logo et positionner le message
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.centerHorizontally(logoImageView.id, ConstraintSet.PARENT_ID)
        constraintSet.centerVertically(logoImageView.id, ConstraintSet.PARENT_ID)
        constraintSet.centerHorizontally(greetingTextView.id, ConstraintSet.PARENT_ID)
        constraintSet.connect(greetingTextView.id, ConstraintSet.TOP, logoImageView.id, ConstraintSet.BOTTOM, 32)
        constraintSet.applyTo(layout)

        // Effet lettre par lettre pour le message de salutation
        val textToDisplay = "Planifiez votre séjour idéal avec nous."
        var index = 0
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (index < textToDisplay.length) {
                    greetingTextView.text = textToDisplay.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 100) // Intervalle entre chaque lettre
                } else {
                    // Transition vers l'activité principale après un délai
                    handler.postDelayed({
                        layout.animate().alpha(0f).setDuration(800).withEndAction {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }, 1500)
                }
            }
        }
        handler.post(runnable)
    }

    // Extension pour convertir dp en pixels
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
