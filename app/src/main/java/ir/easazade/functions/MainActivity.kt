package ir.easazade.functions

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ir.easazade.androidutils.bind
import ir.easazade.androidutils.localNumbers
import kotlinx.android.synthetic.main.activity_main.helloworldText

class MainActivity : AppCompatActivity() {

  private val textView: TextView by bind(R.id.helloworldText)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    textView.text = 23.localNumbers()
  }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(AppContextWrapper.wrap(newBase))
  }
}
