package com.example.externalstorage

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val writeButton: Button = findViewById(R.id.writeButton)
        val readButton: Button = findViewById(R.id.readButton)
        textView = findViewById(R.id.textView)


        writeButton.setOnClickListener {
            if (isExternalStorageWritable()) {
                writeFileToExternalStorage("example.txt", "Hello from External Storage!")
            } else {
                Toast.makeText(this, "External storage not available", Toast.LENGTH_SHORT).show()
            }
        }

        readButton.setOnClickListener {
            if (isExternalStorageReadable()) {
                val content = readFileFromExternalStorage("example.txt")
                if (content != null) {
                    textView.text = content
                } else {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Cannot read external storage", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED || state == Environment.MEDIA_MOUNTED_READ_ONLY
    }

    private fun writeFileToExternalStorage(fileName: String, content: String) {
        val externalStorageDir = getExternalFilesDir(null)
        val file = File(externalStorageDir, fileName)
        try {
            val fos = FileOutputStream(file)
            fos.write(content.toByteArray())
            fos.close()
            Toast.makeText(this, "File written to external storage", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to write file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFileFromExternalStorage(fileName: String): String? {
        val externalStorageDir = getExternalFilesDir(null)
        val file = File(externalStorageDir, fileName)
        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }


}