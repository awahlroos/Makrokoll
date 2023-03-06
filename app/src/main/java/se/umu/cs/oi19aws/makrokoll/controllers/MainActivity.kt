package se.umu.cs.oi19aws.makrokoll.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import se.umu.cs.oi19aws.makrokoll.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button2)

        button.setOnClickListener(){
            button.isActivated = !button.isActivated
        }
    }
}