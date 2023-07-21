package com.example.onechamp.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.onechamp.R
import com.example.onechamp.databinding.CardImageLytBinding


class CardImageView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    lateinit var paint: Paint
    lateinit var imageView: ImageView

    init {
        paint = Paint()
        paint.color = Color.WHITE
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
        paint.style = Paint.Style.FILL
        inflate(context, R.layout.card_image_lyt, this)

        imageView = findViewById(R.id.imageview)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CardImageView)
        imageView.setImageDrawable(attributes.getDrawable(R.styleable.CardImageView_image))
        attributes.recycle()

    }

    fun setImage(img:Int){
        imageView.setImageResource(img)
    }


}