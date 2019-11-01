package ir.easazade.functions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.easazade.androidutils.localNumbers
import kotlinx.android.synthetic.main.activity_main.helloworldText

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    helloworldText.text = 23.localNumbers()
  }

  override fun attachBaseContext(newBase: Context?) {
    super.attachBaseContext(AppContextWrapper.wrap(newBase))
  }
}
