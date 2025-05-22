package com.example.lattelogic

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val instructButton : ImageButton = findViewById(R.id.instructButton)
        instructButton.setOnClickListener{
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.popup,null)
            val instructWindow = PopupWindow(popupView, 900,2000,true)
            instructWindow.showAtLocation(popupView, Gravity.CENTER,0,0)

            val closeButton : ImageButton = popupView.findViewById(R.id.closeButton)
            closeButton.setOnClickListener{
                instructWindow.dismiss()
            }
        }


        // Make sure tags are set on initial images somewhere, e.g.:
        findViewById<ImageView>(R.id.icedCoffeeImg).tag = "Iced Coffee"
        findViewById<ImageView>(R.id.hotCoffeeImg).tag = "Hot Coffee"
        findViewById<ImageView>(R.id.milkImg).tag = "Milk"
        findViewById<ImageView>(R.id.vanillaImg).tag = "Vanilla Syrup"
        findViewById<ImageView>(R.id.caramelImg).tag = "Caramel Syrup"

        // Make draggable
        fun makeDraggable(imageView: ImageView) {
            imageView.setOnLongClickListener {
                val data = ClipData.newPlainText("ingredient", it.tag.toString())
                val shadow = View.DragShadowBuilder(it)
                it.startDragAndDrop(data, shadow, it, 0)
                true
            }
        }

        makeDraggable(findViewById(R.id.icedCoffeeImg))
        makeDraggable(findViewById(R.id.hotCoffeeImg))
        makeDraggable(findViewById(R.id.milkImg))
        makeDraggable(findViewById(R.id.vanillaImg))
        makeDraggable(findViewById(R.id.caramelImg))

        val baseDrop = findViewById<ImageView>(R.id.baseDrop)
        val ingredientDrop = findViewById<ImageView>(R.id.ingredientDrop)
        val resultImage: ImageView = findViewById(R.id.resultImage)
        val mixButton: ImageButton = findViewById(R.id.mixButton)

        baseDrop.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val itemName = event.clipData.getItemAt(0).text.toString()
                    val draggedView = event.localState as? ImageView
                    if (draggedView?.drawable != null) {
                        baseDrop.setImageDrawable(draggedView.drawable)
                        baseDrop.tag = itemName
                        Toast.makeText(this, "$itemName set as base!", Toast.LENGTH_SHORT).show()
                    } else {
                        baseDrop.setImageDrawable(null)
                        baseDrop.tag = null
                        Toast.makeText(this, "Error: dragged item has no image.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        ingredientDrop.setOnDragListener { v, event ->
            if (event.action == DragEvent.ACTION_DROP) {
                val tag = event.clipData.getItemAt(0).text.toString()
                (v as ImageView).setImageDrawable((event.localState as ImageView).drawable)
                v.tag = tag
                Toast.makeText(this, "$tag added as ingredient!", Toast.LENGTH_SHORT).show()
            }
            true
        }

        val drinkImageMap = mapOf(
            "Iced Coffee + Milk" to R.drawable.icedlatte,
            "Hot Coffee + Milk" to R.drawable.latte,
            "Iced Coffee + Milk + Vanilla Syrup" to R.drawable.icedvanillalatte,
            "Iced Coffee + Milk + Caramel Syrup" to R.drawable.icedcaramellatte,
            "Hot Coffee + Milk + Vanilla Syrup" to R.drawable.vanillalatte,
            "Hot Coffee + Milk + Caramel Syrup" to R.drawable.caramellatte
        )

        mixButton.setOnClickListener {
            val base = baseDrop.tag?.toString()
            val addon = ingredientDrop.tag?.toString()

            if (base == null || addon == null) {
                Toast.makeText(this, "Please drag ingredients into both drop areas.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val comboString = "$base + $addon"

            val imageRes = drinkImageMap[comboString]

            if (imageRes != null) {
                resultImage.setImageResource(imageRes)
                resultImage.visibility = View.VISIBLE
                resultImage.tag = comboString

                // Clear drop zones
                baseDrop.setImageDrawable(null)
                baseDrop.tag = null
                ingredientDrop.setImageDrawable(null)
                ingredientDrop.tag = null

                // Make result draggable
                resultImage.setOnLongClickListener {
                    val drinkName = resultImage.tag?.toString()
                    if (drinkName != null) {
                        val clipData = ClipData.newPlainText("ingredient", drinkName)
                        val dragShadow = View.DragShadowBuilder(resultImage)
                        it.startDragAndDrop(clipData, dragShadow, resultImage, 0)
                        true
                    } else {
                        false
                    }
                }
            } else {
                resultImage.visibility = View.GONE
                Toast.makeText(this, "Unknown combo!", Toast.LENGTH_SHORT).show()
            }
        }
        val resetButton: ImageButton = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            //Clear base and ingredient drop zones
            baseDrop.setImageDrawable(null)
            baseDrop.tag = null

            ingredientDrop.setImageDrawable(null)
            ingredientDrop.tag = null

            //Clear result image
            resultImage.setImageDrawable(null)
            resultImage.tag = null
            resultImage.visibility = View.GONE

            Toast.makeText(this, "Reset!", Toast.LENGTH_SHORT).show()
        }
    }
}