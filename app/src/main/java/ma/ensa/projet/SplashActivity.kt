package ma.ensa.projet

import android.content.Intent
import android.graphics.Color
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

        val layout = ConstraintLayout(this).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#FF5C5C"))
        }


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

        layout.addView(logoImageView)
        setContentView(layout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.centerHorizontally(logoImageView.id, ConstraintSet.PARENT_ID)
        constraintSet.connect(logoImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 120)

        constraintSet.applyTo(layout)

        // Effet lettre par lettre
        val textToDisplay = ""
        var index = 0
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                if (index < textToDisplay.length) {

                    index++
                    handler.postDelayed(this, 300)
                } else {
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

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
